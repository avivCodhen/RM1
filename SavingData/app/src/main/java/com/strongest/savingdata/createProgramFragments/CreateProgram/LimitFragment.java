package com.strongest.savingdata.createProgramFragments.CreateProgram;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.strongest.savingdata.Activities.OnCreateProgramListener;
import com.strongest.savingdata.Database.Muscles.DBMuscleHelper;
import com.strongest.savingdata.Server.Download;
import com.strongest.savingdata.Unused.AlertPopUp;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;
import com.strongest.savingdata.createProgramFragments.CreateProgram.GeneratorFragment2;
import com.strongest.savingdata.createProgramFragments.CreateProgram.RoutinesFragment;

import org.greenrobot.eventbus.EventBus;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_ARMS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_BACK;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_CHEST;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_LEGS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REST;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_SETS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_SHOULDERS;

public class LimitFragment extends BaseCreateProgramFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup radioGroup;
    private int radioButtonTag = -1;
    private TextView descriptionTV;
    private OnCreateProgramListener onCreateProgramListener;

    public static LimitFragment getInstance(OnCreateProgramListener onCreateProgramListener) {
        LimitFragment f = new LimitFragment();
        f.setOnCreateProgramListener(onCreateProgramListener);
        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        /*getActionBar().setTitle(R.string.limit_title);
        getActionBar().setDisplayHomeAsUpEnabled(true);*/
        return inflater.inflate(R.layout.fragment_limit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {

        radioGroup = (RadioGroup) v.findViewById(R.id.fragment_limit_radioGroup);
        descriptionTV = (TextView) v.findViewById(R.id.fragment_limit_descriptionTV);
        radioGroup.setOnCheckedChangeListener(this);
        v.findViewById(R.id.limit_fragment_btn).setOnClickListener(this);
      /*  Download d = new Download(getContext());
        try {

            d.refreshData(
                    DBMuscleHelper.DB_TABLE_NAME,
                    TABLE_REPS,
                    TABLE_CHEST,
                    TABLE_BACK,
                    TABLE_LEGS,
                    TABLE_SHOULDERS,
                    TABLE_ARMS,
                    TABLE_SETS,
                    TABLE_REST
            );
        } catch (Exception e) {
            Log.d("aviv", "downloadExercises: " + e.toString());
            // dm.getPrefsEditor().putBoolean("download", true).commit();
        }*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.next_button, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_next:
                if (radioButtonTag == -1) {
                    AlertPopUp alert = new AlertPopUp(getContext());
                    alert.createAlert("Please choose limitations.", false);
                    return false;
                } else {
                    getPrefsEditor().putBoolean(MODE_GENERATED_PROGRAM, radioButtonTag == 0 ? false : true).commit();
                    switchFragment(radioButtonTag == 0 ? new GeneratorFragment2() : new RoutinesFragment(), null);
                }
                break;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        switch (checkedId) {
            case R.id.fragment_limit_rB1:
                radioButtonTag = 0;
                descriptionTV.setText(getContext().getResources().getString(R.string.rockey_description));
                break;
            case R.id.fragment_limit_rB2:
                radioButtonTag = 1;
                descriptionTV.setText(getContext().getResources().getString(R.string.rockstar_description));
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().post(this);
    }

    public void setOnCreateProgramListener(OnCreateProgramListener onCreateProgramListener) {
        this.onCreateProgramListener = onCreateProgramListener;
    }

    @Override
    public void onClick(View v) {
        onCreateProgramListener.createProgramUI(radioButtonTag == 0 ?
                GeneratorFragment2.getInstance(onCreateProgramListener) :
                RoutinesFragment.getInstance(onCreateProgramListener));
    }
}
