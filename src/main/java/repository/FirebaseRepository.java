package repository;

import java.util.ArrayList;
import java.util.List;
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

import model.Admin;
import model.Booking;
import model.Car;
import model.Entity;
import model.Message;
import model.UserModel;

public class FirebaseRepository implements Repository {

	public static String 
	USERS_REF = "USERS", 
	CARS_REF = "CARS", 
	ADMIN_REF = "ADMIN_USERS",
	BOOKING_REF = "BOOKINGS",
	BOOKING_LOG_REF = "BOOKINGS_LOG",
	MESSAGE_REF = "MESSAGE";

	private final Map<String, Class> CLASS_REF = ImmutableMap.<String, Class>builder()
			.put(USERS_REF, UserModel.class)
			.put(CARS_REF, Car.class)
			.put(ADMIN_REF, Admin.class)
			.put(BOOKING_REF, Booking.class)
			.put(BOOKING_LOG_REF, Booking.class)
			.put(MESSAGE_REF, Message.class)
			.build();

	private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

	@Override
	public User getUserByUsername(String username) {
		final Waiter waiter = new Waiter();

		ref.child(ADMIN_REF).child(username).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				waiter.setAdmin(snapshot.getValue(Admin.class));
				waiter.respond();
			}

			@Override
			public void onCancelled(DatabaseError error) {System.out.println("Query is Cancelled");}
		});

		waiter.waitRespond();
		Admin user = waiter.getAdmin();

		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		user.getAuth().forEach(e -> authorities.add(new SimpleGrantedAuthority("ROLE_" + e)));

		UserDetails userDetails = User.withUsername(user.getUsername())
				.password("{sha256}" + user.getPassword())
				.authorities(authorities)
				.build();

		return (User) userDetails;
	}

	@Override
	public void add(String referece, String child, Object obj) {
		ref.child(USERS_REF).child(child).setValueAsync(obj);
	}

	@Override
	public <T extends Entity> String push(String reference, T obj) {
		DatabaseReference pushRef = ref.child(reference).push();
		String key = pushRef.getKey();
		pushRef.setValueAsync(obj);
		
		return key;
	}

	@Override
	public <T extends Entity> void set(String reference, T obj) {
		ref.child(reference).setValueAsync(obj);
	}

	@Override
	public void delete(String reference, String child) {
		ref.child(reference).child(child).removeValueAsync();
	}

	@Override
	public <T extends Entity> List<T> getObjectList(String reference) {
		final Waiter waiter = new Waiter();

		ref.child(reference).addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {

				snapshot.getChildren().forEach(i -> {
					T item = (T) i.getValue(CLASS_REF.get(reference));
					item.setKey(i.getKey());
					waiter.getList().add(item);
				});
				waiter.respond();
			}

			@Override
			public void onCancelled(DatabaseError error) {
				System.out.println("Query is Cancelled");
			}

		});	

		waiter.waitRespond();

		return waiter.getList();
	}

	@Override
	public <T extends Entity> T getObject(String reference, String child) {
		final Waiter waiter = new Waiter();
		ref.child(reference).child(child).addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				if(snapshot.getValue() != null) {
					waiter.setObject(snapshot.getValue(CLASS_REF.get(reference)));
					((T) waiter.getObject()).setKey(snapshot.getKey());
				}
				waiter.respond();
			}

			@Override
			public void onCancelled(DatabaseError error) {
				System.out.println("Failed To Get " + child);
				waiter.respond();
			}

		});

		waiter.waitRespond();

		return (T) waiter.object;
	}

	@Override
	public <T extends Entity> void update(String reference, Map map) {
		ref.child(reference).updateChildrenAsync(map);
	}

	private class Waiter {
		private final int TIMEOUT = 10000;

		private Admin admin;
		private final List list;
		private Object object;

		private final FutureTask<Void> future;
		private final ExecutorService exec;

		public Waiter() {
			future = new FutureTask<Void>(() -> null);
			exec = Executors.newFixedThreadPool(1);
			list = new ArrayList();
		}

		public void waitRespond() {
			try {
				future.get(TIMEOUT, TimeUnit.MILLISECONDS);
			} catch (Throwable e) {System.out.println(e);}
		}

		public void respond() {
			exec.execute(future);	
		}

		public void setAdmin(Admin admin) {
			this.admin = admin;
		}

		public void setObject(Object object) {
			this.object = object;
		}

		public Admin getAdmin() {
			return admin;
		}

		public List getList() {
			return list;
		}

		public Object getObject() {
			return object;
		}
	}
}
