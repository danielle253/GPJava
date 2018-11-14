package spring.web.test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult.Callback;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import model.UserModel;
import repository.FirebaseRepository;
import service.BookingManagmentService;


public class Tester {


	
	public Tester(){
		
		FirebaseDatabase.getInstance().getReference().child(FirebaseRepository.USERS_REF)
			.child("32yhyPZOADN8gOXFGVerN146fnR2").addListenerForSingleValueEvent(new ValueEventListener() {

				@Override
				public void onDataChange(DataSnapshot snapshot) {
					UserModel user = snapshot.getValue(UserModel.class);
					
					// This registration token comes from the client FCM SDKs.
					String registrationToken = user.getToken();

					// See documentation on defining a message payload.
					Message message = Message.builder()
					    .putData("Hello", "Boi")
					    .putData("Key", "Value")
					    //.setTopic("TestBoi")
					    .setToken(user.getToken())
					    .setNotification(new Notification("Test Notification", "Body Here"))
					    .build();
					
					
					// Send a message to the device corresponding to the provided
					// registration token.	
					try {
						String response = FirebaseMessaging.getInstance().send(message);
						System.out.println(response);
					} catch (FirebaseMessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onCancelled(DatabaseError error) {
					// TODO Auto-generated method stub
					
				}
				
			});
		
	}
}
