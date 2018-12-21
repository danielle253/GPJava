package config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;

@Configuration
public class PaypalConfig {
	
	@Value("${paypal.client.id}")
	private String id;
	
	@Value("${paypal.client.secret}")
	private String secret;
	
	@Value("${paypal.environment}")
	private String environment;
	
	private APIContext apiContext;
	
	@PostConstruct
	public void init() {
		apiContext = new APIContext(id, secret, environment);
	}
	
	@Bean
	public APIContext paypalContext() {
		return apiContext;
	}
}
