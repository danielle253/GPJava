package test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

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

import model.Car;
import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;
import service.BookingManagmentService;

@Component
public class Tester {
	
	@Autowired 
	private Repository repository;
	
	public Tester(){
		
	}
	
	public void test() {
		UserModel user = repository.getObject(FirebaseRepository.USERS_REF, "LXCLjAtsdqSJmmMLCrne8n0hJpX2");
		user.getBookingHistory().add("-LSfaSqX1Jp0-IoyHO69");
		user.getBookingInProgress().add("-LSfVRo6RNtDnP1TN32C");
		repository.set(FirebaseRepository.USERS_REF + "/" + user.getKey(), user);
	}
}
