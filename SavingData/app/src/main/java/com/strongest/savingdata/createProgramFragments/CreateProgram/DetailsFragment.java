package com.strongest.savingdata.createProgramFragments.CreateProgram;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.strongest.savingdata.Activities.BaseActivity;
import com.strongest.savingdata.Activities.OnCreateProgramListener;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.R;
import com.strongest.savingdata.Server.Download;

import java.util.ArrayList;
import java.util.Collection;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_ALL;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_GENERATOR;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_METHODS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_METHODS_GENERATOR;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS_GENERATOR;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REST;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_SETS;

/**
 * Created by Cohen on 4/27/2017.
 */


public class DetailsFragment extends BaseCreateProgramFragment implements View.OnClickListener {


    private DataManager dm;
    private DBExercisesHelper helper;
    private OnCreateProgramListener onCreateProgramListener;

    public static DetailsFragment getInstance(OnCreateProgramListener onCreateProgramListener) {
        DetailsFragment f = new DetailsFragment();
        f.setOnCreateProgramListener(onCreateProgramListener);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       /* getActionBar().show();
        getActionBar().setTitle("Details");
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActivity().getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        return inflater.inflate(R.layout.fragment_details, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
        dm = ((BaseActivity) getActivity()).getDataManager();
        v.findViewById(R.id.fragment_details_create_program).setOnClickListener(this);
        // TextView detailsTV = (TextView) v.findViewById(R.id.create_workout_TV);
        //detailsTV.setText(dedtailsFragmentText());

     //   downloadExercises();
    }

    private void downloadExercises() {
       // boolean flag = dm.getPrefs().getBoolean("download", true);
        boolean flag = true;
        if (flag) {
            Download d = new Download(getContext());
            try {

                d.refreshData(TABLE_METHODS,
                        TABLE_EXERCISES_ALL,
                        TABLE_REPS,
                        TABLE_EXERCISES_GENERATOR,
                        TABLE_REST,
                        TABLE_SETS);
            } catch (Exception e) {
                Log.d("aviv", "downloadExercises: " + e.toString());
                dm.getPrefsEditor().putBoolean("download", true).commit();
            }
        }


//        ExerciseTemplate eT = new ExerciseTemplate(new BodyPart.Chest(getContext()),new Load(0,0,0,0));
        //      BParams[][] muscle = eT.getTemplate(0);
        //ArrayList<recycler_view_exercises_left_margin> a =(ArrayList<recycler_view_exercises_left_margin>) dm.readByString("");

    }

    @Override
    public void onClick(View v) {
        //    switchFragment(new LimitFragment());
        // switchFragment(new GeneratorFragment());
        onCreateProgramListener.createProgramUI(LimitFragment.getInstance(onCreateProgramListener));
        dm.closeDataBases();
        //switchFragment(new LimitFragment(), null);
    }

    private void print(ArrayList<String> a) {
        for (int i = 0; i < a.size(); i++) {
            Log.d("aviv", a.get(i));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /*Activity activity = (Activity) getContext();
                activity.finish();*/

                return true;
        }
        return false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setOnCreateProgramListener(OnCreateProgramListener onCreateProgramListener) {
        this.onCreateProgramListener = onCreateProgramListener;
    }

   /* @Override
    public void onStop() {
        setDone(true);
        super.onStop();
    }

    @Override
    public void onResume() {
        setDone(false);
        super.onResume();
    }*/

    public interface FinishWorkoutActivityListener {
        void done();
    }
}
