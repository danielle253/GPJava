package model;

import java.util.ArrayList;

public class Admin{

	private String username;
	private String password;
	private ArrayList<String> auth;
	
	private Admin() {}
	
	public Admin(String username, String password, ArrayList<String> auth) {
		this.username = username;
		this.password = password;
		this.auth = auth;
	}

	public String getUsername() {return username;}

	public String getPassword() {return password;}

	public ArrayList<String> getAuth() {return auth;}
}
