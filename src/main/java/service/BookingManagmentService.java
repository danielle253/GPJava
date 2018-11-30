package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

import org.assertj.core.util.Arrays;
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
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.LatLng;

import model.Booking;
import model.Car;
import model.Coordinate;
import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;

public class BookingManagmentService extends Thread{

	@Autowired 
	private Repository repository;

	@Autowired
	private GeoApiContext context;

	//Move to External File!!
	private final String CONFIRMED_TEXT = "Your vehicle is in 15 min from you.";
	private final String IN_PROGRESS_TEXT = "You will reach the destination point in 15 min.";
	private final String ARRIVAL_TEXT = "Your vehicle just arrived.";
	private final String BUSY_TEXT = "All vehicles are busy at the moment, we will notify you when any available.";
	private final String CONFIRMATION_TEXT = "Your booking has been Confirmed.";
	private final String CANCEL_TEXT = "Your booking has been Canceled.";

	private final String PUSH_TAG = "Transport AI";

	private final int DURATION_LIMIT = 900; // Limit to send a push notification
	private final int DURATION_ARRIVAL_LIMIT = 30;

	public BookingManagmentService(){
		setDaemon(true);
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {System.out.println(e);}

			bookingsRequestScan();
			bookingsConfirmedScan();
			bookingsInProgressScan();
		}
	}

	//Main Scanners
	private void bookingsRequestScan() {
		Queue<Booking> queue = new LinkedList<Booking>(
				repository.getObjectList(FirebaseRepository.BOOKING_REF, Booking.class));

		queue.forEach(this::confirmation);
	}

	private void bookingsConfirmedScan() {
		List<Booking> list = repository.getObjectList(FirebaseRepository.BOOKING_CONFIRMED_REF, Booking.class);

		list.forEach(book -> {
			updateDistanceMatrix(book, book.getSource());
			repository.set(FirebaseRepository.BOOKING_CONFIRMED_REF + "/" + book.getKey(), book);

			if(book.isNotificationSent())
				arrivalScan(book);
			else
				pushNotificationThrowingScan(book, CONFIRMED_TEXT,
						FirebaseRepository.BOOKING_CONFIRMED_REF);
		});
	}

	private void bookingsInProgressScan() {
		List<Booking> list = repository.getObjectList(FirebaseRepository.BOOKING_IN_PROGRESS_REF, Booking.class);

		list.forEach(book -> {
			if(book.isInProgress()) {
				updateDistanceMatrix(book, book.getDestination());
				repository.set(FirebaseRepository.BOOKING_IN_PROGRESS_REF + "/" + book.getKey(), book);

				if(book.isNotificationSent()) 
					complitionScan(book);
				else 
					pushNotificationThrowingScan(book, IN_PROGRESS_TEXT, 
							FirebaseRepository.BOOKING_IN_PROGRESS_REF + "/" + book.getKey());

			} else if((System.currentTimeMillis() - book.getHold()) / 1000 > DURATION_LIMIT){
				try {
					moveToHistory(book);
					sendPushMessage(book.getUserID(), new Notification(PUSH_TAG , CANCEL_TEXT));
				} catch (FirebaseMessagingException e) {e.printStackTrace();}
			}
		});
	}

	//Scanners Components
	private void pushNotificationThrowingScan(Booking booking, String notificationText, String reference) {
		if(booking.getDuration().inSeconds < DURATION_LIMIT) {
			try {
				sendPushMessage(booking.getUserID(), new Notification(PUSH_TAG , notificationText));
				notificationSent(reference);
			} catch (FirebaseMessagingException e) {e.printStackTrace();}
		}
	}
	
	private void moveToHistory(Booking book) {
		book.setDistance(null);
		book.setDuration(null);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("available", true);

		repository.update(FirebaseRepository.CARS_REF + "/"+ book.getCarID(), map);
		repository.set(FirebaseRepository.BOOKING_HISTORY_REF + "/" + book.getKey(), book);
		repository.delete(FirebaseRepository.BOOKING_IN_PROGRESS_REF, book.getKey());
	}

	private boolean carAllocation(Booking book) {
		List<Car> cars = repository.getObjectList(FirebaseRepository.CARS_REF, Car.class);

		Car allocatedCar = null;
		DistanceMatrixElement allocatedResult = null;
		long allocatedCarDistance = Long.MAX_VALUE;

		for(Car car : cars) {
			if(car.isAvailable()) {
				try {
					DistanceMatrixElement results = getDistanceMatrixResult(car.getPosition(), book.getSource());

					if(results.distance.inMeters < allocatedCarDistance) {
						allocatedCar = car;
						allocatedResult = results;
						allocatedCarDistance = results.distance.inMeters;
					}
				} catch (Throwable e) {System.out.println(e);}	
			}
		}

		if(allocatedCar != null) {
			book.setCarID(allocatedCar.getKey());
			book.setNotificationSent(false);
			updateDistanceMatrix(book, allocatedResult);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("available", false);

			repository.update(FirebaseRepository.CARS_REF + "/"
					+ allocatedCar.getKey(), map);
		} else
			return false;

		return true;
	}	

	private void complitionScan(Booking book) {
		if(book.isComplete()) 
			moveToHistory(book);
		else if(book.getDuration().inSeconds < DURATION_ARRIVAL_LIMIT) {
			book.setInProgress(false);
			book.setHold(System.currentTimeMillis());
			
			repository.set(FirebaseRepository.BOOKING_IN_PROGRESS_REF + "/" + book.getKey(), book);
		}
	}

	private void arrivalScan(Booking book) {
		if(book.getDuration().inSeconds < DURATION_ARRIVAL_LIMIT) {
			try {
				DistanceMatrixElement results = getDistanceMatrixResult(book.getSource(), book.getDestination());

				ArrayList<Coordinate> list = new ArrayList<Coordinate>();
				list.add(book.getSource());

				book.setPath(list);
				book.setSource(null);
				book.setNotificationSent(false);
				book.setInProgress(false);
				book.setHold(System.currentTimeMillis());

				repository.set(FirebaseRepository.BOOKING_IN_PROGRESS_REF + "/"
						+ book.getKey(), book);

				repository.delete(FirebaseRepository.BOOKING_CONFIRMED_REF, book.getKey());

				sendPushMessage(book.getUserID(), new Notification(PUSH_TAG , ARRIVAL_TEXT));
			} catch (FirebaseMessagingException | ApiException | InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void confirmation(Booking book) {
		try {
			if(carAllocation(book)) {
				repository.set(FirebaseRepository.BOOKING_CONFIRMED_REF + "/" + book.getKey(), book);
				repository.delete(FirebaseRepository.BOOKING_REF, book.getKey());
				sendPushMessage(book.getUserID(), new Notification(PUSH_TAG, CONFIRMATION_TEXT));
			} else 
				if(!book.isNotificationSent()) {
					sendPushMessage(book.getUserID(), new Notification(PUSH_TAG, BUSY_TEXT));
					notificationSent(FirebaseRepository.BOOKING_REF + "/" + book.getKey());
				}
		} catch (FirebaseMessagingException e) {e.printStackTrace();}
	}

	// Service Methods
	private DistanceMatrixElement getDistanceMatrixResult(Coordinate source, Coordinate destination) throws ApiException, InterruptedException, IOException {
		return DistanceMatrixApi.newRequest(context)
				.origins(source.toLatLng())
				.destinations(destination.toLatLng())
				.await().rows[0].elements[0];
	}

	private void sendPushMessage(String userID, Notification notification) throws FirebaseMessagingException {
		UserModel user = repository.getObject(FirebaseRepository.USERS_REF, userID);

		Message message = Message.builder()
				.setToken(user.getToken())
				.setNotification(notification)
				.build();

		FirebaseMessaging.getInstance().send(message);
	}

	private void notificationSent(String reference) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("notificationSent", true);
		repository.update(reference, map);
	}

	private void updateDistanceMatrix(Booking book, Coordinate destination){
		Car car = repository.getObject(FirebaseRepository.CARS_REF, book.getCarID());
		updateDistanceMatrix(book, car, destination);
	}

	private void updateDistanceMatrix(Booking book, Car car, Coordinate destination){
		try {
			DistanceMatrixElement result = getDistanceMatrixResult(car.getPosition(), destination);
			updateDistanceMatrix(book, result);
		} catch (Throwable e) {e.printStackTrace();}
	}

	private void updateDistanceMatrix(Booking book, DistanceMatrixElement result) {
		book.setDuration(result.duration);
		book.setDistance(result.distance);
	}
}
