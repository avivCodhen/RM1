package com.strongest.savingdata.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.strongest.savingdata.ArtificialInteligence.ArtificialIntelligence;
import com.strongest.savingdata.ArtificialInteligence.ArtificialSubjectListener;
import com.strongest.savingdata.BaseWorkout.Programmer;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.DependencyInjection.MainApplication;

import javax.inject.Inject;

/**
 * Created by Cohen on 6/20/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements ArtificialSubjectListener{

    public static String TAG = "aviv";
    public static final String ROUTINE = "routine";
    public static final String PREFS = "sharedprefs";
    private SharedPreferences.Editor editor;

    @Inject
    public Programmer programmer;
    @Inject
    public DataManager dataManager;
    @Inject
    public ArtificialIntelligence ai;

    public SharedPreferences getPrefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getApplication()).getAppComponent().inject(this);

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

    public ArtificialIntelligence getAi() {
        return ai;
    }
}
