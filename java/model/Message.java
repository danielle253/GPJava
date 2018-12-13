package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Message extends Entity {
	private List<MessageForm> messages;
	private String user_ref;
	private String state;
	
	private Message() {
		messages = new ArrayList<MessageForm>();
	}
	
	public Message(String user_ref, String message) {
		this.user_ref = user_ref;
		messages = new ArrayList<MessageForm>();
		messages.add(new MessageForm(user_ref, message));
		
		state = "IN_PROGRESS";
	}
	
	public List<MessageForm> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageForm> messages) {
		this.messages = messages;
	}

	public String getUser_ref() {
		return user_ref;
	}

	public void setUser_ref(String user_ref) {
		this.user_ref = user_ref;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
