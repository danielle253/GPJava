package manager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.maps.GeoApiContext;

import model.Booking;
import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;

@Component
public class NotificationManager {
	
	@Autowired
	private Repository repositoryWired;
	private static Repository repository;
	
	@PostConstruct     
	private void initStaticFields () {
		repository = repositoryWired;
	}
	
	//Move to External File!!
	public static final String CONFIRMED_TEXT = "Your vehicle is in 15 min from you.";
	public static final String IN_PROGRESS_TEXT = "You will reach the destination point in 15 min.";
	public static final String ARRIVAL_TEXT = "Your vehicle just arrived.";
	public static final String BUSY_TEXT = "All vehicles are busy at the moment, we will notify you when any available.";
	public static final String CONFIRMATION_TEXT = "Your booking has been Confirmed.";
	public static final String CANCEL_TEXT = "Your booking has been Canceled.";
	public static final String CAR_SUSPEND_TEXT = "Your car is no more available, a new car will be allocated shortly";
	
	private static final String PUSH_TAG = "Transport AI";
	
	public static void sendPushMessage(Booking booking, String notification){
		UserModel user = repository.getObject(FirebaseRepository.USERS_REF, booking.getUserID());
		Message message = Message.builder()
				.setToken(user.getToken())
				.setNotification(new Notification(PUSH_TAG, notification))
				.build();
		
		try {
			FirebaseMessaging.getInstance().send(message);
			BookingManager.notified(booking);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
	}
}	
