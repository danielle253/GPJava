package spring.web.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
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

@ComponentScan(basePackages={"controller", "config", "core"})
public class Tester {
	UserModel boi;
	public Tester(){
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("/USERS");
		
		/*ArrayList<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ADMIN"));
		roles.add(new SimpleGrantedAuthority("USER"));
		*/
		
	/*	ArrayList<String> roles = new ArrayList<String>();
		roles.add("ADMIN");
		roles.add("USER");
		
		UserModel user = new UserModel("admin", "123", roles);

		ref.child("/admin").setValueAsync(user);
		ref.child("/user1").setValueAsync(new UserModel("user1", "123", roles));
		ref.child("/user2").setValueAsync(new UserModel("user2", "123", roles));
		
		*/
		/*ref.child("/admin").addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				UserModel user = snapshot.getValue(UserModel.class);
				
				System.out.println(user.getPassword() + " " + user.getUsername());
				System.out.println(snapshot.getKey() + " " + snapshot.getValue());
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
				
			}
			
		});*/
		
		UserDetails user = User.withUsername("admin").password("pass").authorities("ADMIN", "USER").build();
		Collection<? extends GrantedAuthority> auth = user.getAuthorities();
		System.out.println();
	}
}
