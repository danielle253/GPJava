package repository;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;

import model.UserModel;

public interface Repository {
	
	User getUserByUsername(String username);
	ArrayList<UserModel> getUsersList();
	void addUser(UserModel model);
}
