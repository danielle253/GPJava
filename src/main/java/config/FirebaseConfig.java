package config;

import java.io.FileInputStream;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import repository.FirebaseRepository;
import repository.Repository;
import service.FirebaseAuthenticationService;
import service.FirebaseUserDetailsService;

@Configuration
public class FirebaseConfig {
	
	@Value("${firebase.url}")
	private String firebaseUrl;
	
	@Value("${firebase.credentials.path}")
	private String firebaseCredentialsPath;
	
	@PostConstruct
	public void init() {
		try {
			FileInputStream inp = new FileInputStream(
					System.getProperty("user.dir") + firebaseCredentialsPath);
			
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(inp))
					.setDatabaseUrl(firebaseUrl)
					.build();

			FirebaseApp.initializeApp(options);
		} catch (Throwable e) {
			System.out.println(e);
		}
	}
	
	@Bean
	public FirebaseAuthenticationService firebaseAuthenticationService() {
		return new FirebaseAuthenticationService();
	}
	
	@Bean
	public DatabaseReference firebaseDatabase() {
		return FirebaseDatabase.getInstance().getReference();
	}
	
	@Bean
	public Repository firebase() {
		return new FirebaseRepository();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new FirebaseUserDetailsService();
	}
}
