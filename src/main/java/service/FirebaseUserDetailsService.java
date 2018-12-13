package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import repository.Repository;

public class FirebaseUserDetailsService implements UserDetailsService {
	
	@Autowired
	private Repository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = repository.getUserByUsername(username);
		//User.withUsername("admin").password("{noop}123").roles("ADMIN").build()
		return userDetails;
	}
}
