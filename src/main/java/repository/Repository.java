package repository;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;

import model.Entity;

public interface Repository {
	
	//Security Related method
	User getUserByUsername(String username);
	
	<T extends Entity> T getObject(String ref, String child);
	void add(String ref, String child, Object obj);
	<T extends Entity> ArrayList<T> getObjectList(String ref, Class<T> c);
	void delete(String reference, String child);

	void push(String referece, Object obj);
}
