package com.strongest.savingdata.AService;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.strongest.savingdata.AModels.UserModel.User;
import com.strongest.savingdata.AndroidServices.FireBaseMessageService;
import com.strongest.savingdata.Controllers.CallBacks;

import java.util.HashMap;

import javax.inject.Inject;

public class UserService {

    public final static String USERNAME = "username";
    public final static String UID = "uid";
    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    Context context;

    @Inject
    public UserService(SharedPreferences sharedPreferences, SharedPreferences.Editor editor,
                       Context context, FirebaseAuth firebaseAuth, DatabaseReference databaseReference) {
        this.sharedPreferences = sharedPreferences;
        this.editor = editor;

        this.context = context;
        this.firebaseAuth = firebaseAuth;
        this.databaseReference = databaseReference;
    }

    public void registerUser(String email, String pass, String name, CallBacks.OnFinish onFinish) {


        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                HashMap<String, Object> dateJoined = new HashMap<>();
                dateJoined.put("dateJoined", ServerValue.TIMESTAMP);
                User user = new User(name, email, dateJoined);
                String userToken = FireBaseMessageService.getToken(context);
                saveUsernameToSharedPreferences(name, firebaseAuth.getCurrentUser().getUid());
                user.setUserToken(userToken);
                onFinish.onFinish(user);
                Log.d("aviv", "registerUser: " + userToken);
                Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show();


            } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                Toast.makeText(context,
                        "User with this email already exist.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Something went wrong!" + task.getException(), Toast.LENGTH_SHORT).show();
                //TODO: make another request incase of no internet


            }


        });

    }

    public void logInUser(String email, String pass, CallBacks.OnFinish onFinish) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                findUserByEmail(firebaseAuth.getCurrentUser().getEmail(), serverUser -> {
                    User u = (User) serverUser;
                    saveUsernameToSharedPreferences(u.getName(), u.getUID());
                    String userToken = FireBaseMessageService.getToken(context);
                    u.setUserToken(userToken);
                    Log.d("aviv", "logInUser: " + userToken);
                    saveUserToServer(u, result -> {
                        onFinish.onFinish(1);
                    });
                });
            } else {
                Toast.makeText(context, "Wrong credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUsernameToSharedPreferences(String username, String uid) {
        editor.putString(USERNAME, username);
        editor.putString(UID, uid);
        editor.commit();
    }

    public void saveUserToServer(User user, CallBacks.OnFinish onFinish) {
        FirebaseUser fUser = firebaseAuth.getCurrentUser();
        databaseReference.child("users").child(fUser.getUid()).setValue(user).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                onFinish.onFinish(1);
            } else {
                Toast.makeText(context, "User did NOT save on the server!", Toast.LENGTH_SHORT).show();

                //TODO: make another request to save the user incase of no internet
            }
        });

    }

    public String getUserUID() {
        if (firebaseAuth.getCurrentUser() != null)
            return firebaseAuth.getCurrentUser().getUid();
        else
            return "";
    }

    public String getUsername() {
        return sharedPreferences.getString(USERNAME, "");
    }

    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }


    public void findUserByEmail(String email, CallBacks.OnFinish onFinish) {
        databaseReference.child("users")
                .orderByChild("email")
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = null;
                        if (dataSnapshot.getValue() != null) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {

                                user = d.getValue(User.class);
                                user.setUID(d.getKey());
                            }
                        }
                        onFinish.onFinish(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public boolean firstTimeClient() {

        if (!sharedPreferences.getString("firstTime", "").equals("no") && !isUserLoggedIn()) {
            editor.putString("firstTime", "no").commit();
            return true;
        }
        return false;
    }

    public String getEmail() {
        if (firebaseAuth.getCurrentUser() == null) {
            return "";
        } else {
            return firebaseAuth.getCurrentUser().getEmail();
        }
    }
}
