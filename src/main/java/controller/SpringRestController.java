package controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.util.Data;

import manager.BookingManager;
import manager.CarManager;
import manager.PaymentManager;
import manager.SupportManager;
import manager.UserManager;
import model.Alert;
import model.Car;
import model.Message;
import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;
import service.AuthenticationService;

@RestController
@RequestMapping("rest")
public class SpringRestController {
	
	@Autowired
	private Repository repository;
	
	@Autowired
	private AuthenticationService authService;
	
	@RequestMapping(path="newUser")
	public boolean addUser(@RequestParam("email") String email,
			@RequestParam("password") String password){
		authService.createUser(email, password);
		return true;
	}
	
	@RequestMapping(path="delete")
	public String deleteUser(@RequestParam("ref") String ref, 
			@RequestParam("username") String username) {
		repository.delete(ref, username);
		return username;
	}
	
	@RequestMapping(path="getUser")
	public User getUser(@RequestParam("username") String username) {
		return repository.getUserByUsername(username);
	}
	
	@RequestMapping(path="getCar")
	public Car getCar(@RequestParam("carID") String carID) {
		return repository.getObject(FirebaseRepository.CARS_REF, carID);
	}
	
	@RequestMapping(path="get")
	public Object get(@RequestParam("ref") String ref, 
			@RequestParam("child") String child) {
		return repository.getObject(ref, child);
	}
	
	@RequestMapping(path="booking_suspend")
	public void bookingSuspend(@RequestParam("bookingRef") String bookingRef) {
		BookingManager.suspend(repository.getObject(FirebaseRepository.BOOKING_REF, bookingRef));
	}
	
	@RequestMapping(path="booking_unsuspend")
	public void bookingUnsuspend(@RequestParam("bookingRef") String bookingRef) {
		BookingManager.unsuspend(repository.getObject(FirebaseRepository.BOOKING_REF, bookingRef));
	}
	
	@RequestMapping(path="booking_abort")
	public void bookingAbort(@RequestParam("bookingRef") String bookingRef) {
		BookingManager.abortation(repository.getObject(FirebaseRepository.BOOKING_REF, bookingRef));
	}
	
	@RequestMapping(path="booking_new")
	public void booking_new(@RequestParam("user_ref") String user_ref, @RequestParam("source") String source,
			@RequestParam("destination") String destination) {
		BookingManager.booking(source, destination, user_ref);
	}

	@RequestMapping(path="car_suspend")
	public void carSuspend(@RequestParam("carRef") String carRef) {
		CarManager.suspend(repository.getObject(FirebaseRepository.CARS_REF, carRef));
	}
	
	@RequestMapping(path="car_unsuspend")
	public void carUnsuspend(@RequestParam("carRef") String carRef) {
		CarManager.unsuspend(repository.getObject(FirebaseRepository.CARS_REF, carRef));
	}
	
	@RequestMapping(path="car_new")
	public void carNew() {
		CarManager.addNew();
	}
	
	@RequestMapping(path="car_delete")
	public void carDelete(@RequestParam("carRef") String carRef) {
		CarManager.delete(carRef);
	}
	
	@RequestMapping(path="user_delete")
	public void userDelete(@RequestParam("uid") String uid) {
		UserManager.delete(repository.getObject(FirebaseRepository.USERS_REF, uid));
	}
	
	@RequestMapping(path="user_suspend")
	public void userSuspend(@RequestParam("uid") String uid) {
		UserManager.suspend(repository.getObject(FirebaseRepository.USERS_REF, uid));
	}
	
	@RequestMapping(path="user_unsuspend")
	public void userUnsuspend(@RequestParam("uid") String uid) {
		UserManager.unsuspend(repository.getObject(FirebaseRepository.USERS_REF, uid));
	}
	
	@RequestMapping(path="user_new")
	public void userNew(@RequestParam("email") String email, @RequestParam("password") String password) {
		UserManager.addNew(email, password);
	}
	
	@RequestMapping(path="message_new")
	public void messageNew(@RequestParam("user_ref") String user_ref, @RequestParam("msg") String msg) {
		SupportManager.newMessage(user_ref, msg);	
	}
	
	@RequestMapping(path="create_invoice")
	public void newInvoice(@RequestParam("email") String email, @RequestParam("amount") String amount) {
		PaymentManager.sendInvoice(email, amount);
	}
	
	@RequestMapping(path="alert")
	public void alert(@RequestParam("description") String description) {
		Alert alert = new Alert(description,
				new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(Calendar.getInstance().getTime()));
		repository.push(FirebaseRepository.ALERT_REF, alert);
	}
	
}
