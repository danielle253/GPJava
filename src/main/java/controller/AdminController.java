package controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import manager.SupportManager;
import model.Alert;
import model.Booking;
import model.Car;
import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;

@Controller
@RequestMapping("/")
public class AdminController {
	
	@Autowired
	private Repository repository;
	
	AdminController(){
		System.out.println("Controller Ready");
	}

	@RequestMapping(path= "admin")
	public String admin(Authentication auth, Model model) {
		model.addAttribute("name", "Welcome Back, " + auth.getName());
		return "admin";
	}
	
	@RequestMapping(path= "accounts")
	public String accounts() {
		return "accounts";
	}
	
	@RequestMapping(path= "registration")
	public String registration() {
		return "registration.html";
	}
	
	@RequestMapping(path= "table_accounts")
	public String table_accounts(Model model) {
		List list = repository.getObjectList(FirebaseRepository.USERS_REF);
		model.addAttribute("users", list);
		
		return "table_accounts";
	}
	
	@RequestMapping(path= "map")
	public String map(Model model) {
		model.addAttribute("cars", repository.getObjectList(
				FirebaseRepository.CARS_REF));
		return "map";
	}
	
	@RequestMapping(path= "booking_details")
	public String booking_details(@RequestParam("ref") String ref, @RequestParam("bookingRef") String bookingRef, Model model) {
		if(ref.equals("0")) 
			model.addAttribute("booking", repository.getObject(FirebaseRepository.BOOKING_REF, bookingRef));
		else
			model.addAttribute("booking", repository.getObject(FirebaseRepository.BOOKING_LOG_REF, bookingRef));
		
		return "booking_details";
	}
	
	@RequestMapping(path= "bookings")
	public String bookings(Model model) {
		model.addAttribute("bookingInProgress", repository.getObjectList(FirebaseRepository.BOOKING_REF));
		model.addAttribute("bookingHistory", repository.getObjectList(FirebaseRepository.BOOKING_LOG_REF));
		return "bookings";
	}
	
	@RequestMapping(path= "booking")
	public String newBooking() {
		return "booking";
	}
	
	@RequestMapping(path= "messages")
	public String messages(Model model) {
		model.addAttribute("messages", repository.getObjectList(FirebaseRepository.MESSAGE_REF));
		return "messages";
	}
	
	@RequestMapping(path= "message_chat")
	public String message_chat(@RequestParam("ref")String ref, Model model) {
		model.addAttribute("message", repository.getObject(FirebaseRepository.MESSAGE_REF, ref));
		return "message_chat";
	}
	
	@RequestMapping(path = "send_msg")		
	public String send_message(@RequestParam("ref")String ref, @RequestParam("msg") String msg, Model model) {
		SupportManager.writeMessage(ref, msg);
		return message_chat(ref, model);
	}
	
	@RequestMapping(path = "alertcheck")
	public String alertcheck(Model model) {
		List<Alert> alertList = repository.getObjectList(FirebaseRepository.ALERT_REF);
		for(int i = 0; i < alertList.size(); i++) 
			if(alertList.get(i).isDisplayed()) 
				alertList.remove(i);
		
		for(Alert a: alertList) {
			a.setDisplayed(true);
			repository.set(FirebaseRepository.ALERT_REF + "/" + a.getKey(), a);
		}
			
		model.addAttribute("alerts", alertList);
		return "alertNotification";
	}
}
