package controller;

import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import model.UserModel;
import repository.Repository;

@Controller
@RequestMapping("/")
public class BoiController {
	
	@Autowired
	private Repository repository;
	
	BoiController(){
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
			path="addUser",
			method=RequestMethod.GET)
	@ResponseBody
	public String addUser(@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("role") String role) {
		ArrayList<String> s = new ArrayList<String>();
		
		for(String str: role.split(","))
			s.add(str);
		
		repository.addUser(new UserModel(username, password, s));
		return username;
	}
	
	@RequestMapping(
			path= "accounts",
			method= RequestMethod.GET)
	public String accounts(Authentication auth, Model model) {
		model.addAttribute("users", repository.getUsersList());
		return "accounts";
	}
}

