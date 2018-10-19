package model;

import java.util.ArrayList;

public class UserModel {
	
		private String username;
		private String password;
		private ArrayList<String> auth;
		
		private UserModel() {}
		
		public UserModel(String username, String password, ArrayList<String> auth) {
			this.username = username;
			this.password = password;
			this.auth = auth;
		}

		public String getUsername() {return username;}

		public String getPassword() {return password;}

		public ArrayList<String> getAuth() {return auth;}

}
