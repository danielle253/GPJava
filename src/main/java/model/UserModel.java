package model;

import java.util.ArrayList;

public class UserModel extends Entity{
		
		private String email;
		private double balance;
		private ArrayList<String> bookings;
		private boolean active;
		private String token;
		
		private UserModel () {}
		
		public UserModel(String email, ArrayList<String> bookings, double balance, boolean active) {
			this.email = email;
			this.balance = balance;
			this.bookings = bookings;
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

		public ArrayList<String> getBookings() {
			return bookings;
		}

		public void setBookings(ArrayList<String> bookings) {
			this.bookings = bookings;
		}
}
