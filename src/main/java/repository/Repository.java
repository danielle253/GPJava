package repository;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;

import model.UserModel;

public interface Repository {
	
	//Security Related method
	User getUserByUsername(String username);
	
	<T> T getObject(String ref, String child);
	void add(String ref, String child, Object obj);
	<T> ArrayList<T> getObjectList(String ref, Class<T> c);
	void delete(String reference, String child);
}
