package controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Car;
import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;

@RestController
@RequestMapping("rest")
public class SpringRestController {
	
	@Autowired
	private Repository repository;
	
	@RequestMapping(path="newUser")
	public UserModel addUser(@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("role") String role) {
		
		UserModel user = new UserModel(username, password,
				new ArrayList<String>(Arrays.asList(role.split(","))));
		
		repository.add(FirebaseRepository.USERS_REF, user.getUsername(), user);
		
		return user;
	}
	
	@RequestMapping(path="delete")
	public String deleteUser(@RequestParam("ref") String ref, 
			@RequestParam("username") String username) {
		repository.delete(ref, username);
		return username;
	}
	
	@RequestMapping(path="deleteUser")
	public String deleteUser(@RequestParam("username") String username) {
		repository.delete(FirebaseRepository.USERS_REF, username);
		return username;
	}
	
	@RequestMapping(path="getUser")
	public User getUser(@RequestParam("username") String username) {
		return repository.getUserByUsername(username);
	}
	
	@RequestMapping(path="getCar")
	public Car getCar(@RequestParam("carID") String carID) {
		return repository.getObject(FirebaseRepository.CARS_REF, "Car1");
	}
	
	@RequestMapping(path="get")
	public Object get(@RequestParam("ref") String ref, 
			@RequestParam("child") String child) {
		return repository.getObject(ref, child);
	}
	
}
