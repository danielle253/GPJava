package core;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import service.BookingManagmentService;
import test.Tester;

@SpringBootApplication
@ComponentScan(basePackages={"controller", "config", "core", "manager", "test"})
public class SpringAdmin {
		
	@Autowired
	private BookingManagmentService service;
	
	@Autowired
	private Tester tester;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringAdmin.class, args);
	}
	
	@PostConstruct
	private void serviceStart() {
		service.start();
		//tester.test();
	}
}
