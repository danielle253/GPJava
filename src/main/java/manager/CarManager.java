package manager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.maps.GeoApiContext;

import model.Booking;
import model.Car;
import repository.FirebaseRepository;
import repository.Repository;

@Component
public class CarManager {

	@Autowired
	private Repository repositoryWired;
	private static Repository repository;

	@PostConstruct     
	private void initStaticFields () {
		repository = repositoryWired;
	}

	public static void delete(String carRef) {
		Car car = repository.getObject(FirebaseRepository.CARS_REF, carRef);
		
		if(car.getBookingID() != null)
			BookingManager.suspend(repository.getObject(FirebaseRepository.BOOKING_REF, car.getBookingID()));
			
		repository.delete(FirebaseRepository.CARS_REF, carRef);
	}
	
	public static void addNew() {
		repository.push(FirebaseRepository.CARS_REF, new Car());
	}
	
	public static void suspend(Car car) {
		car.setSuspended(true);
		repository.set(FirebaseRepository.CARS_REF + "/" + car.getKey(), car);
	}
	
	public static void unsuspend(Car car) {
		car.setSuspended(false);
		repository.set(FirebaseRepository.CARS_REF + "/" + car.getKey(), car);
	}
	
	public static void freeCar(String carID) {
		if(carID != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("available", true);
			map.put("bookingID", null);

			repository.update(FirebaseRepository.CARS_REF + "/"+ carID, map);
		}
	}
}
