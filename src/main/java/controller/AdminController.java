package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String booking_details(@RequestParam("bookingRef") String bookingRef, Model model) {
		model.addAttribute("booking", repository.getObject(FirebaseRepository.BOOKING_REF, bookingRef));
		return "booking_details";
	}
}

