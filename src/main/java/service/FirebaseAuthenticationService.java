package service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.NonNull;

import model.UserModel;
import repository.FirebaseRepository;
import repository.Repository;

public class FirebaseAuthenticationService implements AuthenticationService{
	private FirebaseAuth mAuth;

	public FirebaseAuthenticationService(){
		mAuth = FirebaseAuth.getInstance();
	}

	public void createUser(String email, String password) {
		CreateRequest request = new CreateRequest()
			    .setEmail(email)
			    .setEmailVerified(false)
			    .setPassword(password)
			    .setDisabled(false);
		
		try {
			UserRecord userRecord = mAuth.createUser(request);
			System.out.println("Successfully created new user: " + userRecord.getUid());
			createDatabaseRef(userRecord.getUid());
		} catch (FirebaseAuthException e) {
			System.out.println("Failed to create account: " + e);
		}
		
	}
	
	private void createDatabaseRef(final String uid) {
		DatabaseReference ref = FirebaseDatabase.getInstance()
				.getReference().child(FirebaseRepository.USERS_REF);
		
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            	if (!dataSnapshot.hasChild(uid))     
                    ref.child(uid).setValueAsync(new UserModel(new ArrayList<String>(), 0.0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}

        });
	}

	public void deleteUser(String uid) {
		try {
			deleteDatabaseRef(uid);
			mAuth.deleteUser(uid);
			System.out.println("Successfully deleted user.");
		} catch (FirebaseAuthException e) {
			System.out.println("Failed to delete account: " + e);
		}
	}
	
	private void deleteDatabaseRef(final String uid) {
		FirebaseDatabase.getInstance().getReference()
			.child(FirebaseRepository.USERS_REF)
					.child(uid).removeValueAsync();
	}
}
