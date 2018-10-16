package com.strongest.savingdata.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AService.ProgramService;
import com.strongest.savingdata.AService.UserService;
import com.strongest.savingdata.AService.WorkoutsService;
import com.strongest.savingdata.AViewModels.ProgramViewModel;
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

    @Inject
    public DataManager dataManager;

    @Inject
    WorkoutsViewModelFactory workoutsViewModelFactory;

    @Inject
    UserService userService;

    @Inject
    ProgramService programService;

    @Inject
    WorkoutsService workoutsService;


    public WorkoutsViewModel workoutsViewModel;

    public SharedPreferences getPrefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getApplication()).getAppComponent().inject(this);

    }

    public void addFragmentToActivity(int id, Fragment f, String tag) {

        getSupportFragmentManager().beginTransaction()
        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(id, f, tag)
                .addToBackStack(tag)
                .commit();
    }

    public void addFragmentToActivityNoTransition(int id, Fragment f, String tag) {

        getSupportFragmentManager().beginTransaction()
                .replace(id, f, tag)
                .addToBackStack(tag)
                .commit();
    }


    public void closeKeyBoardOnClick() {
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        viewGroup.setOnTouchListener((par1,par2) -> {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                });


    }

    public DataManager getDataManager() {
        return dataManager;
    }


}
