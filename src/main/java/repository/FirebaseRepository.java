package repository;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.UserModel;


public class FirebaseRepository implements Repository {

	private UserModel user;
	private ArrayList<UserModel> users;
	private String userRef = "/USERS";
	private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
	
	private FutureTask<Void> future = new FutureTask<Void>(() -> null);
	private ExecutorService ex = Executors.newFixedThreadPool(1);
	
	@Override
	public User getUserByUsername(String username) {
		ref.child(userRef).child("/" + username).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				user = snapshot.getValue(UserModel.class);
				ex.execute(future);
			}

			@Override
			public void onCancelled(DatabaseError error) {System.out.println("Query is Cancelled");}
		});
		
		try {
			future.get();
		} catch (Throwable e) {System.out.println(e);}
		
		ArrayList<GrantedAuthority> authorities = parseAuthorities(user.getAuth());
		
		UserDetails userDetails = User.withUsername(user.getUsername())
				.password("{noop}" + user.getPassword())
				.authorities(authorities)
				.build();
		
		user = null;
		
		return (User) userDetails;
	}
	
	private ArrayList<GrantedAuthority> parseAuthorities(ArrayList<String> s) {
		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		s.forEach(e -> authorities.add(new SimpleGrantedAuthority("ROLE_" + e)));
		return authorities;
	}

	@Override
	public ArrayList<UserModel> getUsersList() {
		ref.child(userRef).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				users = new ArrayList<UserModel>();
				snapshot.getChildren().forEach(i -> users.add(i.getValue(UserModel.class)));
				ex.execute(future);
			}

			@Override
			public void onCancelled(DatabaseError error) {System.out.println("Query is Cancelled");}
		});
		
		try {
			future.get();
		} catch (Throwable e) {System.out.println(e);}
		
		return users;
	}

	@Override
	public void addUser(UserModel model) {
		ref.child(userRef).child(model.getUsername()).setValueAsync(model);
	}
}
