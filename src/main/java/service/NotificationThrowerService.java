package service;

import java.util.HashMap;
import java.util.List;import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableMap;
import com.google.firebase.database.FirebaseDatabase;
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
		while(true) {
			try {
				sleep(3000);
			} catch (InterruptedException e) {System.out.println(e);}
			
			scan();
		}
	}

	private void scan() {
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
