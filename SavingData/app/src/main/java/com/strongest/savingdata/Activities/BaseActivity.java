package com.strongest.savingdata.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModelFactory;
import com.strongest.savingdata.BaseWorkout.Programmer;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.DependencyInjection.MainApplication;
import com.strongest.savingdata.R;

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

    public Programmer programmer;
    @Inject
    public DataManager dataManager;

    @Inject
    WorkoutsViewModelFactory workoutsViewModelFactory;


    public WorkoutsViewModel workoutsViewModel;

    public SharedPreferences getPrefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getApplication()).getAppComponent().inject(this);

    }

    public void addFragmentToHomeActivity(Fragment f, String tag){

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .add(R.id.activity_home_framelayout, f, tag)
                .addToBackStack(tag)
                .commit();
    }

    public SharedPreferences.Editor getPrefsEditor() {
        return editor = getPrefs().edit();
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public Programmer getProgrammer() {
        return programmer;
    }

}
