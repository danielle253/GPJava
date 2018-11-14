package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import model.Booking;
import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;

public class NotificationThrowerService extends Thread{

	@Autowired
	private Repository repository;
	private final int DURATION_LIMIT = 900; // Limit to send a push notification

	public NotificationThrowerService() {
		setDaemon(true);
	}

	@Override
	public void run() {

		scan();

		try {
			sleep(5000);
		} catch (InterruptedException e) {System.out.println(e);}
	}

	private void scan() {
		List<Booking> list = repository.getObjectList(FirebaseRepository.BOOKING_CONFIRMED_REF, Booking.class);

		list.forEach(booking -> {
			if(booking.getDuration().inSeconds < DURATION_LIMIT) {
				UserModel user = repository.getObject(FirebaseRepository.USERS_REF, booking.getUserID());
				
				Message message = Message.builder()
					    .setToken(user.getToken())
					    .setNotification(new Notification("Transport AI", "Your vehicle is in 15 min from you."))// 15 min to variable
					    .build();
				
				try {
					FirebaseMessaging.getInstance().send(message);
				} catch (FirebaseMessagingException e) {System.out.println(e);}
			}
		});
	}
}
