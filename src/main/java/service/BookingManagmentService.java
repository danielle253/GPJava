package service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.LatLng;

import model.Booking;
import model.Car;
import repository.FirebaseRepository;
import repository.Repository;

public class BookingManagmentService extends Thread{
	
	@Autowired 
	private Repository repository;
	
	@Autowired
	private GeoApiContext context;
	
	public BookingManagmentService(){
		setDaemon(true);
	}

	@Override
	public void run() {
		//while(true){
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {System.out.println(e);}
		
		scanBookingsRequest();
		
	//}
	}
	
	private void scanBookingsRequest() {
		Queue<Booking> queue = new LinkedList<Booking>(
				repository.getObjectList(FirebaseRepository.BOOKING_REF, Booking.class));
		
		if(!queue.isEmpty())
			queue.forEach(this::allocation);
		
	}

	private void allocation(Booking book) {
		List<Car> cars = repository.getObjectList(FirebaseRepository.CARS_REF, Car.class);
		Car allocatedCar = null;
		DistanceMatrixElement allocatedResult = null;
		long allocatedCarDistance = Long.MAX_VALUE;
		
		for(Car car : cars) {
			DistanceMatrixElement results = null;
			try {
				results = DistanceMatrixApi.newRequest(context)
					.origins(new LatLng(car.getLatitude(), car.getLongitude()))
						.destinations(book.getSource())
							.await().rows[0].elements[0];
			} catch (Throwable e) {System.out.println(e);}
			
			if(results.distance.inMeters < allocatedCarDistance) {
				allocatedCar = car;
				allocatedResult = results;
				allocatedCarDistance = results.distance.inMeters;
			}
		}
		
		//Could be replaced with builder
		book.setCarID(allocatedCar.getKey());
		book.setDuration(allocatedResult.duration);
		book.setDistance(allocatedResult.distance);
		book.setNotificationSent(false);
		
		repository.set(FirebaseRepository.BOOKING_CONFIRMED_REF + "/"
				+ book.getKey(), book);
	}
	
}
