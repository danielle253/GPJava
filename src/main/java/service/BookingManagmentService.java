package service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.LatLng;

import model.Booking;
import model.Car;
import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;

public class BookingManagmentService extends Thread{

	@Autowired 
	private Repository repository;

	@Autowired
	private GeoApiContext context;

	private final int DURATION_LIMIT = 900; // Limit to send a push notification

	public BookingManagmentService(){
		setDaemon(true);
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {System.out.println(e);}

			bookingsRequestScan();
			pushNotificationThrowingScan();
		}
	}

	private void bookingsRequestScan() {
		List list = repository.getObjectList(FirebaseRepository.BOOKING_REF, Booking.class);


		Queue<Booking> queue = new LinkedList<Booking>(list);

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
						.destinations(book.getSource().toLatLng())
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

		repository.delete(FirebaseRepository.BOOKING_REF, book.getKey());
	}

	private void pushNotificationThrowingScan() {
		List<Booking> list = repository.getObjectList(FirebaseRepository.BOOKING_CONFIRMED_REF, Booking.class);


		list.forEach(booking -> {
			if(!booking.isNotificationSent())
				if(booking.getDuration().inSeconds < DURATION_LIMIT) {
					UserModel user = repository.getObject(FirebaseRepository.USERS_REF, booking.getUserID());

					Message message = Message.builder()
							.setToken(user.getToken())
							.setNotification(new Notification("Transport AI", "Your vehicle is in 15 min from you."))// 15 min to variable
							.build();

					try {
						FirebaseMessaging.getInstance().send(message);

						Map<String, Object> map = new HashMap<String, Object>();
						map.put("notificationSent", true);

						FirebaseDatabase.getInstance().getReference().child(FirebaseRepository.BOOKING_CONFIRMED_REF + "/"
								+ booking.getKey()).updateChildrenAsync(map);

					} catch (FirebaseMessagingException e) {System.out.println(e);}
				}
		});

	}

}
