package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import manager.SupportManager;
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

	@RequestMapping(
			path= "admin",
			method= RequestMethod.GET)
	public String admin(Authentication auth, Model model) {
		model.addAttribute("name", "Welcome Back, " + auth.getName());
		return "admin";
	}
	
	@RequestMapping(
			path= "accounts",
			method= RequestMethod.GET)
	public String accounts() {
		return "accounts";
	}
	
	@RequestMapping(
			path= "registration",
			method= RequestMethod.GET)
	public String registration() {
		return "registration.html";
	}
	
	@RequestMapping(
			path= "table_accounts",
			method= RequestMethod.GET)
	public String table_accounts(Model model) {
		List list = repository.getObjectList(
				FirebaseRepository.USERS_REF, UserModel.class);
		
		model.addAttribute("users", list);
		
		return "table_accounts";
	}
	
	@RequestMapping(
			path= "map",
			method= RequestMethod.GET)
	public String map(Model model) {
		model.addAttribute("cars", repository.getObjectList(
				FirebaseRepository.CARS_REF, Car.class));
		return "map";
	}
	
	@RequestMapping(
			path= "booking_details",
			method= RequestMethod.GET)
	public String booking_details(@RequestParam("ref") String ref, @RequestParam("bookingRef") String bookingRef, Model model) {
		if(ref.equals("0")) 
			model.addAttribute("booking", repository.getObject(FirebaseRepository.BOOKING_REF, bookingRef));
		else
			model.addAttribute("booking", repository.getObject(FirebaseRepository.BOOKING_LOG_REF, bookingRef));
		
		return "booking_details";
	}
	
	@RequestMapping(
			path= "bookings",
			method= RequestMethod.GET)
	public String bookings(Model model) {
		model.addAttribute("bookingInProgress", repository.getObjectList(FirebaseRepository.BOOKING_REF, Booking.class));
		model.addAttribute("bookingHistory", repository.getObjectList(FirebaseRepository.BOOKING_LOG_REF, Booking.class));
		return "bookings";
	}
	
	@RequestMapping(
			path= "messages",
			method= RequestMethod.GET)
	public String messages(Model model) {
		model.addAttribute("messages", SupportManager.getMessagesList());
		return "messages";
	}
	
	@RequestMapping(
			path= "message_chat",
			method= RequestMethod.GET)
	public String message_chat(@RequestParam("ref")String ref, Model model) {
		model.addAttribute("message", repository.getObject(FirebaseRepository.MESSAGE_REF, ref));
		return "message_chat";
	}
	
	@RequestMapping(
			path = "send_msg",
			method = RequestMethod.GET)
	public String send_message(@RequestParam("ref")String ref, @RequestParam("msg") String msg, Model model) {
		SupportManager.writeMessage(ref, msg);
		return message_chat(ref, model);
	}
}

