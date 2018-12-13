package manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.GeocodingResult;

import model.Booking;
import model.Car;
import model.Coordinate;
import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;

@Component
public class BookingManager {

	@Autowired
	private Repository repositoryWired;

	@Autowired
	private GeoApiContext contextWired;

	private static Repository repository;
	private static GeoApiContext context;

	@PostConstruct     
	private void initStaticFields () {
		repository = repositoryWired;
		context = contextWired;
	}

	private static final String ref = FirebaseRepository.BOOKING_REF + "/";

	public static void booking(String source, String destination, String userRef) {
		UserModel user = repository.getObject(FirebaseRepository.USERS_REF, userRef);
		if(user != null) {
			try {
				GeocodingResult sourceResult = GeocodingApi.newRequest(context).address(source).await()[0];
				GeocodingResult destinationResult = GeocodingApi.newRequest(context).address(destination).await()[0];

				Booking booking = new Booking(
						new Coordinate(sourceResult.geometry.location), 
						new Coordinate(destinationResult.geometry.location), userRef);

				setFormatedAddress(booking);
				booking.setStage("SUSPENDED");
				
				user.getBookingInProgress().add(
						repository.push(ref, booking));
				
				repository.set(FirebaseRepository.USERS_REF + "/" + user.getKey(), user);
			} catch (ApiException | InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void suspend(Booking booking) {
		CarManager.freeCar(booking.getCarID());
		booking.setStage("SUSPENDED");
		booking.setState(null);
		repository.set(ref + booking.getKey(), booking);
	}

	public static void unsuspend(Booking booking){
		booking.setStage("REQUEST");
		repository.set(ref + booking.getKey(), booking);
	}

	public static boolean confirmation(Booking booking) {
		try {
			if(allocateCar(booking)) {
				booking.setNotificationSent(false);
				booking.setStage("CONFIRMED");
				booking.setState("WORK");
				setFormatedAddress(booking);
				repository.set(ref + booking.getKey(), booking);
				return true;
			}
		}catch(Throwable e) {e.printStackTrace();}

		return false;
	}

	public static void arrival(Booking booking) {
		ArrayList<Coordinate> list = new ArrayList<Coordinate>();
		list.add(booking.getSource());

		booking.setPath(list);
		booking.setNotificationSent(false);
		booking.setStage("IN_PROGRESS");
		wait(booking);
	}

	public static void cancelation(Booking booking) {
		booking.setState("CANCELED");
		log(booking);
	}

	public static void abortation(Booking booking) {
		booking.setState("ABORTED");
		log(booking);
	}

	public static void completion(Booking booking) {
		booking.setState("COMPLETED");
		log(booking);
	}

	public static void updateDistanceDuration(List<Booking> bookings) {
		bookings.forEach(booking ->{
			try {
				if(booking.getStage().equals("CONFIRMED"))
					updateDistanceMatrix(booking.getSource(), booking);
				else if(booking.getStage().equals("IN_PROGRESS"))
					updateDistanceMatrix(booking.getDestination(), booking);
			} catch (ApiException | InterruptedException | IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static void notified(Booking booking) {
		booking.setNotificationSent(true);
		repository.set(ref + booking.getKey(), booking);
	}

	public static void wait(Booking booking) {
		booking.setState("WAIT");
		booking.setHold(System.currentTimeMillis());

		repository.set(ref + booking.getKey(), booking);
	}

	private static void updateDistanceMatrix(Coordinate destination, Booking booking) throws ApiException, InterruptedException, IOException {
		Car car = repository.getObject(FirebaseRepository.CARS_REF, booking.getCarID());
		DistanceMatrixElement results = DistanceMatrixApi.newRequest(context)
				.origins(car.getPosition().toLatLng())
				.destinations(destination.toLatLng())
				.await().rows[0].elements[0];

		booking.setDistance(results.distance);
		booking.setDuration(results.duration);
		repository.set(ref + booking.getKey(), booking);
	}

	public static void log(Booking booking) {
		booking.setDistance(null);
		booking.setDuration(null);

		UserModel user = repository.getObject(FirebaseRepository.USERS_REF, booking.getUserID());
		user.getBookingHistory().add(booking.getKey());
		user.getBookingInProgress().remove(booking.getKey());

		CarManager.freeCar(booking.getCarID());
		repository.set(FirebaseRepository.USERS_REF + "/" + user.getKey(), user);
		repository.set(FirebaseRepository.BOOKING_LOG_REF + "/" + booking.getKey(), booking);
		repository.delete(ref, booking.getKey());
	}

	//Services
	private static boolean allocateCar(Booking booking) throws ApiException, InterruptedException, IOException {
		List<Car> cars = repository.getObjectList(FirebaseRepository.CARS_REF, Car.class);

		Car allocatedCar = null;
		DistanceMatrixElement allocatedResult = null;
		long allocatedCarDistance = Long.MAX_VALUE;

		for(Car car : cars) {
			if(car.isAvailable() && !car.isSuspended()) {
				DistanceMatrixElement results = DistanceMatrixApi.newRequest(context)
						.origins(car.getPosition().toLatLng())
						.destinations(booking.getSource().toLatLng())
						.await().rows[0].elements[0];

				if(results.distance.inMeters < allocatedCarDistance) {
					allocatedCar = car;
					allocatedResult = results;
					allocatedCarDistance = results.distance.inMeters;
				}
			}
		}

		if(allocatedCar != null) {
			booking.setCarID(allocatedCar.getKey());
			booking.setDuration(allocatedResult.duration);
			booking.setDistance(allocatedResult.distance);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("available", false);
			map.put("bookingID", booking.getKey());

			repository.update(FirebaseRepository.CARS_REF + "/"
					+ allocatedCar.getKey(), map);
		} else
			return false;

		return true;
	}


	private static void setFormatedAddress(Booking booking) throws ApiException, InterruptedException, IOException {
		GeocodingResult[] source = GeocodingApi.newRequest(context).latlng(
				booking.getSource().toLatLng()).await();

		GeocodingResult[] destination = GeocodingApi.newRequest(context).latlng(
				booking.getDestination().toLatLng()).await();

		booking.setSourceAddress(source[0].formattedAddress);
		booking.setDestinationAddress(destination[0].formattedAddress);
	}
}
