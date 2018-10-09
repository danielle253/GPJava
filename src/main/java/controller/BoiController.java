package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class BoiController {
	
	BoiController(){
		System.out.println("Controller Upload");
	}

	@RequestMapping(
			path= "",
			method= RequestMethod.GET)
	public String home() {
		return "index.html";
	}
	
	@RequestMapping(
			path="login",
			method= RequestMethod.GET)
	public String login() {
		return "login.html";
	}
	
	@RequestMapping(
			path= "admin",
			method= RequestMethod.GET)
	public String admin() {
		return "admin.html";
	}
	
	/*@GetMapping
	public String home1() {
		return "11";
	}*/
}
