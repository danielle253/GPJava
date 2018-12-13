package test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import manager.SupportManager;
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
	}
}
