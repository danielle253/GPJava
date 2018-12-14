package manager;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import model.Message;
import model.MessageForm;
import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;

@Component
public class SupportManager {

	@Autowired
	private Repository repositoryWired;
	
	private static Repository repository;
	
	@PostConstruct     
	private void initStaticFields () {
		repository = repositoryWired;
	}

	private static final String ref = FirebaseRepository.MESSAGE_REF + "/";

	public static void updateMessage(Message message) {
		repository.set(ref + message.getKey(), message);
	}

	public static void writeMessage(String key, String msg) {
		Message message = repository.getObject(FirebaseRepository.MESSAGE_REF, key);
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		message.getMessages().add(new MessageForm(userDetails.getUsername(), msg));
		repository.set(ref + key, message);
	}
	
	public static void newMessage(String user_ref, String msg) {
		Message message = new Message(user_ref, msg);
		UserModel user = repository.getObject(FirebaseRepository.USERS_REF, user_ref);
		user.getMessages().add(message);
		
		repository.push(FirebaseRepository.MESSAGE_REF, message);
		repository.set(FirebaseRepository.USERS_REF, user);
	}
}
