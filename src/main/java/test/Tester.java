package test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import manager.SupportManager;
import model.Car;
import model.Coordinate;
import model.Message;
import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;


@Component
public class Tester {
	@Autowired
	Repository rep;
	
	public Tester(){
		
	}
	
	public void test() {
		UserModel user = rep.getObject(FirebaseRepository.USERS_REF, "LXCLjAtsdqSJmmMLCrne8n0hJpX2");
		System.out.println();
		
		//Car car = new Car();
		/*car.setAvailable(false);
		car.setSuspended(false);
		car.getPosition().add(new Coordinate(5.000, -6.000));
		car.getPosition().add(new Coordinate(5.000, -6.000));
		rep.set(FirebaseRepository.CARS_REF + "/123" , car);*/
		
		/*List<Car> carList = rep.getObjectList(FirebaseRepository.CARS_REF);
		System.out.println();*/
	}
}
