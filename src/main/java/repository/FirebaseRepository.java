package repository;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.collect.ImmutableMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.Car;
import model.UserModel;


public class FirebaseRepository implements Repository {
	
	public static String USERS_REF = "USERS", CARS_REF = "COORDS";
	
	private final Map<String, Class> CLASS_REF = ImmutableMap.<String, Class>builder()
			.put(USERS_REF, UserModel.class)
			.put(CARS_REF, Car.class)
			.build(); 
	
	private UserModel user;
	private ArrayList list;
	private Object object;
	
	private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
	private final Waiter waiter = new Waiter();
	
	@Override
	public User getUserByUsername(String username) {
		
		ref.child(USERS_REF).child("/" + username).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				user = snapshot.getValue(UserModel.class);
				waiter.respond();
			}

			@Override
			public void onCancelled(DatabaseError error) {System.out.println("Query is Cancelled");}
		});
		
		waiter.waitRespond();
		
		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		user.getAuth().forEach(e -> authorities.add(new SimpleGrantedAuthority("ROLE_" + e)));
		
		UserDetails userDetails = User.withUsername(user.getUsername())
				.password("{noop}" + user.getPassword())
				.authorities(authorities)
				.build();
		
		user = null;
		
		return (User) userDetails;
	}

	@Override
	public void add(String referece, String child, Object obj) {
		ref.child(USERS_REF).child(child).setValueAsync(obj);
	}
	
	@Override
	public void delete(String reference, String child) {
		ref.child(reference).child(child).removeValueAsync();
	}

	@Override
	public <T> ArrayList<T> getObjectList(String reference, Class<T> c) {
		
		ref.child(reference).addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				list = new ArrayList<T>();
				snapshot.getChildren().forEach(i -> list.add(i.getValue(c)));
				waiter.respond();
			}

			@Override
			public void onCancelled(DatabaseError error) {
				System.out.println("Query is Cancelled");
			}

		});	
			
		waiter.waitRespond();
		return list;
	}

	@Override
	public <T> T getObject(String reference, String child) {
	
		ref.child(reference).child(child).addListenerForSingleValueEvent(new ValueEventListener() {
		
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				object = snapshot.getValue(CLASS_REF.get(reference));
				waiter.respond();
			}

			@Override
			public void onCancelled(DatabaseError error) {
				System.out.println("Failed To Get " + child);
			}
			
		});
		
		waiter.waitRespond();
		
		return (T) object;
	}
	
	private class Waiter {
		private final int TIMEOUT = 10000;
		private FutureTask<Void> future = new FutureTask<Void>(() -> null);
		private ExecutorService exec = Executors.newFixedThreadPool(1);
		
		public void waitRespond() {
			try {
				future.get(TIMEOUT, TimeUnit.MILLISECONDS);
				future = new FutureTask<Void>(() -> null);
			} catch (Throwable e) {System.out.println(e);}
		}
		
		public void respond() {
			exec.execute(future);
		}
	}
}
