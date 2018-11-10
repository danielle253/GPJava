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
	
	@RequestMapping(path="deleteUserAccount")
	public boolean deleteUser(@RequestParam("uid") String uid) {
		authService.deleteUser(uid);;
		return true;
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
