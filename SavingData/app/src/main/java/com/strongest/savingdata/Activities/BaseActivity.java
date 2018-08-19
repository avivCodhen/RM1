package com.strongest.savingdata.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AService.UserService;
import com.strongest.savingdata.AViewModels.ProgramViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModelFactory;
import com.strongest.savingdata.BaseWorkout.Programmer;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.DependencyInjection.MainApplication;

import javax.inject.Inject;

/**
 * Created by Cohen on 6/20/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public static String TAG = "aviv";
    public static final String ROUTINE = "routine";
    public static final String PREFS = "sharedprefs";
    private SharedPreferences.Editor editor;

    public WorkoutsModel workoutsModel;

    @Inject
    public DataManager dataManager;

    @Inject
    WorkoutsViewModelFactory workoutsViewModelFactory;

    @Inject
    UserService userService;


    public WorkoutsViewModel workoutsViewModel;

    public SharedPreferences getPrefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getApplication()).getAppComponent().inject(this);

    }

    public void addFragmentToHomeActivity(int id, Fragment f, String tag){

        getSupportFragmentManager().beginTransaction()
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
//                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(id, f, tag)
                .addToBackStack(tag)
                .commit();
    }

    public SharedPreferences.Editor getPrefsEditor() {
        return editor = getPrefs().edit();
    }

    public DataManager getDataManager() {
        return dataManager;
    }


}
