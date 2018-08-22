package com.strongest.savingdata.AService;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.strongest.savingdata.AModels.UserModel.User;
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

    public void registerUser(ProgressBar progressBar, String email, String pass, String name, CallBacks.OnFinish onFinish) {


        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                HashMap<String, Object> dateJoined = new HashMap<>();
                dateJoined.put("dateJoined", ServerValue.TIMESTAMP);
                User user = new User("", email, dateJoined);

                Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show();

                saveUsernameToSharedPreferences(name, firebaseAuth.getCurrentUser().getUid());
                onFinish.onFinish(user);
            } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                Toast.makeText(context,
                        "User with this email already exist.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Something went wrong!" + task.getException(), Toast.LENGTH_SHORT).show();
                //TODO: make another request incase of no internet


            }
            progressBar.setVisibility(View.INVISIBLE);


        });

    }

    public void logInUser(String email, String pass, ProgressBar progressBar, CallBacks.OnFinish onFinish){
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(task->{

            progressBar.setVisibility(View.GONE);
            if(task.isSuccessful()){
                Toast.makeText(context, "logged in!", Toast.LENGTH_SHORT).show();
                onFinish.onFinish(1);
            }else{
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

    public String getUserUID(){
        return firebaseAuth.getCurrentUser().getUid();
    }



}
