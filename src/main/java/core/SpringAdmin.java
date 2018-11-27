package core;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import service.BookingManagmentService;
import spring.web.test.Tester;

@SpringBootApplication
@ComponentScan(basePackages={"controller", "config", "core"})
public class SpringAdmin {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringAdmin.class, args);
	}
	
	@PostConstruct
	private void serviceStart() {
		bookingManagmentService.start();
	}
	
	@Autowired
	private BookingManagmentService bookingManagmentService;
	
}
