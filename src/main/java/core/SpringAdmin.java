package core;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import service.BookingManagmentService;
import spring.web.test.Tester;

@SpringBootApplication
@ComponentScan(basePackages={"controller", "config", "core", "manager"})
public class SpringAdmin {
	
	/*@Autowired
	private BookingManagmentService bookingManagmentService;
	*/
	@Autowired
	private BookingManagmentService service;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringAdmin.class, args);
		//new Tester();
	}
	
	@PostConstruct
	private void serviceStart() {
		//service.start();
	}
}
