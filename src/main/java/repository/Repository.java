package repository;

import org.springframework.security.core.userdetails.User;

public interface Repository {
	
	User getUserByUsername(String username);
}
