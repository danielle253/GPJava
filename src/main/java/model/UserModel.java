package model;

import java.util.ArrayList;

public class UserModel extends Entity{
		
		private double balance;
		private ArrayList<String> bookings;
		
		private UserModel () {}
		
		public UserModel(ArrayList<String> bookings, double balance) {
			this.balance = balance;
			this.bookings = bookings;
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
