package repository;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;

import model.UserModel;

public interface Repository {
	
	User getUserByUsername(String username);
	void addUser(UserModel model);
	<T> ArrayList<T> getObjectList(String ref, Class<T> c);
}
