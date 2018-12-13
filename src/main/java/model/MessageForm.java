package model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MessageForm {
		private String sender;
		private String message;
		private String date;
		
		private MessageForm() {}
		
		public MessageForm(String sender, String message) {
			this.sender = sender;
			this.message = message;
			date = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(Calendar.getInstance().getTime());
		}

		public String getSender() {
			return sender;
		}

		public void setSender(String sender) {
			this.sender = sender;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		
		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}
	}