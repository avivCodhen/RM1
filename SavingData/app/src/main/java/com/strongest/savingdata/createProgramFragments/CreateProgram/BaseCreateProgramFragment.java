package com.strongest.savingdata.createProgramFragments.CreateProgram;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.strongest.savingdata.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Cohen on 5/2/2017.
 */

public abstract class BaseCreateProgramFragment extends Fragment{

    public static final String MODE_NEW_PROGRAM = "mode_new_program";
    public static final String MODE_GENERATED_PROGRAM = "mode_generated_program";
    public static final String MODE_EXCISTING_PROGRAM = "mode_excisting_program";
    public static final String MODE_CUSTOM= "mode_custom_template";

    //public static final  String INITIALIZE = "initialize";

    public static final String POSITION = "position";
    public static final String EDIT = "edit";
    public static final String WORKOUT= "workout";
   // public static final String GENERATED = "generated";
  //  public static final String COSTUM = "custom";
    public static final String DAYS_INT = "days";
  //  public static final String LEVEL_LEVEL = "level_level";
    //public static final String USER_PROFILE = "user_profile";
    public static final String ROUTINE = "routine";
    public static final String TAG = "aviv";
    public static final String ID = "id";
    public static final int CREATE_FRAGMENT = 0;
    public static final int INTERRACT_FRAGMENT = 1;


    public static final String MECHANICAL_VALUE = "mechanical";
    public static final String METABOLIC_VALUE = "metabolic";

    public static final String LOAD_EXERCISE_LEVEL = "exercise_level";
    public static final String LOAD_INTENSITY = "intensity";
    public static final String LOAD_VOLUME = "volume";
    public static final String LOAD_COMPLEXITY = "complex";


    public static final int LEVEL_FRAGMENT = 1;
    public static final String BODY_ARRAY = "bodyarray";

    public static final String EXERCISE_NAME = "exercisename";
    public static final String REPS = "reps";
    public static final String METHOD = "method";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String PREFS = "sharedprefs";


    private boolean isDone;
    public BaseCreateProgramFragment() {
    }

    public SharedPreferences getPrefs() {
        return getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getPrefsEditor() {
        return editor = getPrefs().edit();
    }

    public ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

   /* public void switchFragment(Fragment fragment, Bundle data) {
        if(data != null){
            fragment.setArguments(data);
        }
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.create_activity_frameLayout, fragment)
                .addToBackStack(null)
                .commit();

    }*/

    public String dedtailsFragmentText() {
        return getContext().getResources().getString(R.string.details_programCreate);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      //  inflater.inflate(R.menu.next_button, menu);
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
        }
        return false;
    }
*/


    public void switchFragment(Fragment fragment, Bundle data) {
        if(data != null){
            fragment.setArguments(data);
        }/*
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.create_activity_frameLayout, fragment)
                .addToBackStack(null)
                .commit();
*/
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.workout_fragment_layout, fragment)
                .addToBackStack(null)
                .commit();
    }


    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public void onPause() {
        super.onPause();
        isDone = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isDone = false;

    }
}

