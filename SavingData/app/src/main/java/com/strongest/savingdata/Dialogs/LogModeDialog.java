package com.strongest.savingdata.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.AModels.ExerciseModel;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.SetsItemAdapter;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogModeDialog extends android.support.v4.app.DialogFragment {

    private PLObject.ExerciseProfile exerciseProfile;

    @BindView(R.id.dialog_log_recycler)
    RecyclerView recyclerView;

    MyExpandableAdapter adapter;
    LinearLayoutManager lm;
    Workout workout = new Workout();
    Handler handler = new Handler();
    SetsItemAdapter setsItemAdapter;
    public static LogModeDialog getInstacen(PLObject.ExerciseProfile exerciseProfile){
        LogModeDialog f = new LogModeDialog();
        Bundle b = new Bundle();
        b.putSerializable("exercise",exerciseProfile);
        f.setArguments(b);
        return f;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            exerciseProfile = (PLObject.ExerciseProfile) getArguments().get("exercise");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_log, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setsItemAdapter = new SetsItemAdapter(exerciseProfile);
        adapter = new MyExpandableAdapter(workout.exArray, getContext());
        lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
        getList();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        return super.onCreateDialog(savedInstanceState);

    }

    private void getList(){
        ExerciseModel.exerciseToWorkout(setsItemAdapter, exerciseProfile, w -> {
            workout = (Workout) w;
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter.setExArray(workout.getExArray());
                    adapter.notifyDataSetChanged();
                    MyJavaAnimator.fadeIn(recyclerView);

                }
            });

        });
    }
}
