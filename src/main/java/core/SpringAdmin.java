package core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import spring.web.test.Tester;

@SpringBootApplication
@ComponentScan(basePackages={"controller", "config", "core"})
public class SpringAdmin {
	public static void main(String[] args) {
		SpringApplication.run(SpringAdmin.class, args);
		
		new Tester();
	}
}
