package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AViewModels.SelectedLogDataViewModel;
import com.strongest.savingdata.Adapters.LogDataSetsAdapter;
import com.strongest.savingdata.Database.LogData;
import com.strongest.savingdata.Database.LogDataManager;
import com.strongest.savingdata.Fragments.BaseFragment;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseLogDataFragment extends BaseFragment {


    @BindView(R.id.log_data_Recycler)
    RecyclerView recyclerView;

    /*@BindView(R.id.fragment_log_data_saveexittoolbar)
    SaveExitToolBar saveExitToolBar;
*/

    @BindView(R.id.logdata_frag_toolbar)
    Toolbar toolbar;
    LogDataSetsAdapter logDataSetsAdapter;
    ArrayList<LogData.LogDataSets> list;
    String date;
    PLObject.ExerciseProfile exercise;
    LogDataManager logDataManager;

    public static ExerciseLogDataFragment getInstance(String date, PLObject.ExerciseProfile exerciseProfile) {
        ExerciseLogDataFragment f = new ExerciseLogDataFragment();
        Bundle b = new Bundle();
        b.putString("date", date);
        b.putSerializable("exercise", exerciseProfile);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_log_data, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date = getArguments().getString("date");
            exercise = (PLObject.ExerciseProfile) getArguments().getSerializable("exercise");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("exercise", exercise);
        outState.putString("date", date);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            date = savedInstanceState.getString("date");
            exercise = (PLObject.ExerciseProfile) savedInstanceState.getSerializable("exercise");

        }
        logDataManager = new LogDataManager(getContext());
        list = logDataManager.readSets(exercise.getExercise().getName(), date);
        if (list == null || list.size() == 0) {
            list = logDataManager.exerciseToLogDataSetList(exercise);
        }
        logDataSetsAdapter = new LogDataSetsAdapter(list);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(logDataSetsAdapter);
      /*  saveExitToolBar.instantiate()
        .showBack(true)
        .setSaveButton(v->getFragmentManager().popBackStack())
        .showCancel(false);
*/

        toolbar.setNavigationIcon(R.drawable.cancel_48px_black);
        toolbar.setNavigationOnClickListener(v -> getFragmentManager().popBackStack());
    }
}
