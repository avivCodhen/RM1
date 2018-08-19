package com.strongest.savingdata.DependencyInjection;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.strongest.savingdata.AService.UserService;
import com.strongest.savingdata.Utils.FireBaseUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    private MainApplication application;

    public UserModule(MainApplication application) {

        this.application = application;

    }


    @Provides
    @Singleton
    public UserService getUserService(SharedPreferences sharedPreferences,
                                      SharedPreferences.Editor editor,
                                      Context context,
                                      FirebaseAuth firebaseAuth,
                                      DatabaseReference databaseReference) {
        return new UserService(sharedPreferences, editor, context, firebaseAuth, databaseReference);
    }
    @Provides
    @Singleton
    public FirebaseAuth getFireBaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public DatabaseReference getUsersDataBaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }
}
