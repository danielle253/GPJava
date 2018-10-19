package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	WebSecurityConfig(){
		super();
		System.out.println("Security Config Upload");
	}
	
	@Override
 	public void configure(WebSecurity web) throws Exception {
 		/*web.ignoring()
 		// Spring Security should completely ignore URLs starting with /resources/
 				.antMatchers("/**");*/
 	}

 	@Override
 	protected void configure(HttpSecurity http) throws Exception {
 		http
 			.csrf().disable()	
 			.formLogin()
	 			.loginPage("/login")
	 			.permitAll()
 				.and()
 			.authorizeRequests().antMatchers("/admin").hasRole("ADMIN");
 	}

 	@Override
 	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
 		auth.userDetailsService(userDetailsService);
 	// enable in memory based authentication with a user named "user" and "admin"
 		/*.inMemoryAuthentication()
 			.withUser("user").password("{noop}password").roles("USER")
 				.and()
 			.withUser("admin").password("{noop}123").roles("USER", "ADMIN");*/
 	}
}
