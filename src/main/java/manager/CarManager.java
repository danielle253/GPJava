package manager;

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
		repository.delete(FirebaseRepository.CARS_REF, carRef);
	}
	
	public static void addNew() {
		repository.push(FirebaseRepository.CARS_REF, new Car());
	}
	
	public static void suspend(Car car) {
		car.setSuspended(true);
		
		/*if(car.getBookingID() != null ) {
			Booking booking = repository.getObject(FirebaseRepository.BOOKING_REF, car.getBookingID());
			if(!booking.getStage().equals("IN_PROGRESS")) {
				NotificationManager.sendPushMessage(booking, NotificationManager.CAR_SUSPEND_TEXT);
				BookingManager.unsuspend(booking);
			} else 
				return;
		}*/
		
		repository.set(FirebaseRepository.CARS_REF + "/" + car.getKey(), car);
	}
	
	public static void unsuspend(Car car) {
		car.setSuspended(false);
		repository.set(FirebaseRepository.CARS_REF + "/" + car.getKey(), car);
	}
}
