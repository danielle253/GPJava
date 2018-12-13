package manager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

import model.Booking;
import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;
import service.AuthenticationService;

@Component
public class UserManager {

	@Autowired
	private AuthenticationService authServiceWired;
	private static AuthenticationService authService;
	
	@Autowired
	private Repository repositoryWired;
	private static Repository repository;

	@PostConstruct     
	private void initStaticFields () {
		repository = repositoryWired;
		authService = authServiceWired;
	}

	public static void delete(UserModel user) {
		if(user.getBookingInProgress() != null) {
			user.getBookingInProgress().forEach(bookingRef ->{
				Booking booking = repository.getObject(FirebaseRepository.BOOKING_REF, bookingRef);
				if(booking != null) {
					CarManager.freeCar(booking.getCarID());
					BookingManager.abortation(booking);
				}
			});
		}

		authService.deleteUser(user.getKey());
		repository.delete(FirebaseRepository.USERS_REF, user.getKey());
	}

	public static void addNew(String email, String password) {
		CreateRequest request = new CreateRequest()
			    .setEmail(email)
			    .setPassword(password)
			    .setDisabled(false);
		try {
			String uid = FirebaseAuth.getInstance().createUser(request).getUid();
			repository.set(FirebaseRepository.USERS_REF + "/" + uid, new UserModel(email, 0, false));
		} catch (FirebaseAuthException e) {
			e.printStackTrace();
		}
	}

	public static void suspend(UserModel user) {
		user.setActive(false);
		repository.set(FirebaseRepository.USERS_REF + "/" + user.getKey(), user);
	}

	public static void unsuspend(UserModel user) {
		user.setActive(true);
		repository.set(FirebaseRepository.USERS_REF + "/" + user.getKey(), user);
	}
}
