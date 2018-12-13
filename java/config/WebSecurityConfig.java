package config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
    public PasswordEncoder passwordEncoder() {
		String encodingId = "sha256";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put(encodingId, new BCryptPasswordEncoder());
		encoders.put("noop", NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
		encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
		encoders.put("sha256", new StandardPasswordEncoder());
		
		PasswordEncoder enc = new DelegatingPasswordEncoder(encodingId, encoders);
		return new DelegatingPasswordEncoder(encodingId, encoders);
    }

	WebSecurityConfig(){
		super();
		System.out.println("Security Config Upload");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		//web.ignoring()
		// Spring Security should completely ignore URLs starting with /resources/
		//	.antMatchers("/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()	
		.formLogin()
		.loginPage("/login.html")
		.permitAll()
		.and()
		.authorizeRequests().antMatchers("/admin").hasRole("ADMIN");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		// enable in memory based authentication with a user named "user" and "admin"
		/*.inMemoryAuthentication()
 			.withUser("user").password("{noop}password").roles("USER")
 				.and()
 			.withUser("admin").password("{noop}123").roles("USER", "ADMIN");*/
	}
}
