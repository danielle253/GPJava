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

import model.Car;
import model.UserModel;


public class FirebaseRepository implements Repository {
	
	public static String USERS_REF = "/USERS", CARS_REF = "/COORDS";
	
	private UserModel user;
	private ArrayList list;
	private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
	private FutureTask<Void> future = new FutureTask<Void>(() -> null);
	private ExecutorService ex = Executors.newFixedThreadPool(1);
	
	@Override
	public User getUserByUsername(String username) {
		ref.child(USERS_REF).child("/" + username).addValueEventListener(new ValueEventListener() {
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
	public void addUser(UserModel model) {
		ref.child(USERS_REF).child(model.getUsername()).setValueAsync(model);
	}

	@Override
	public <T> ArrayList<T> getObjectList(String reference, Class<T> c) {
		ref.child(reference).addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				list = new ArrayList<T>();
				snapshot.getChildren().forEach(i -> list.add(i.getValue(c)));
				ex.execute(future);
			}

			@Override
			public void onCancelled(DatabaseError error) {
				System.out.println("Query is Cancelled");
			}
		
		});	
			try {
				future.get();
			} catch (Throwable e) {System.out.println(e);}
				
		
		return list;
	}
}
