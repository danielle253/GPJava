package service;

public interface AuthenticationService {
	public void createUser(String email, String password);
	public void deleteUser(String uid);
}
