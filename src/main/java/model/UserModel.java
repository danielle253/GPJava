package model;

import java.util.ArrayList;
import java.util.List;

public class UserModel extends Entity{
		
		private String email;
		private double balance;
		private List<String> bookingInProgress;
		private List<String> bookingHistory;
		private boolean active;
		private String token;
		
		private UserModel () {
			setBookingInProgress(new ArrayList<String>());
			setBookingHistory(new ArrayList<String>());
		}
		
		public UserModel(String email, double balance, boolean active) {
			this.email = email;
			this.balance = balance;
			setBookingInProgress(new ArrayList<String>());
			setBookingHistory(new ArrayList<String>());
			this.active = active;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public double getBalance() {
			return balance;
		}

		public void setBalance(double balance) {
			this.balance = balance;
		}

		public List<String> getBookingHistory() {
			return bookingHistory;
		}

		public void setBookingHistory(List<String> bookingHistory) {
			this.bookingHistory = bookingHistory;
		}

		public List<String> getBookingInProgress() {
			return bookingInProgress;
		}

		public void setBookingInProgress(List<String> bookingInProgress) {
			this.bookingInProgress = bookingInProgress;
		}
}
