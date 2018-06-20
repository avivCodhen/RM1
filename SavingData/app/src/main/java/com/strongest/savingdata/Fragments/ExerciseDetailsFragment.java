package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;
import com.strongest.savingdata.AViewModels.SelectedExerciseViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExerciseDetailsFragment extends BaseFragment {

    @BindView(R.id.details_fragment_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_details_recycler_exercises)
    RecyclerView exerciseRecycler;
    MyExpandableAdapter adapter;
    MyExpandableAdapter exerciseAdapter;
    Workout workout;

    private SelectedExerciseViewModel selectedExerciseViewModel;
    private WorkoutsViewModel workoutsViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        selectedExerciseViewModel = ViewModelProviders.of(getActivity()).get(SelectedExerciseViewModel.class);
        workoutsViewModel = ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);
        PLObject.ExerciseProfile ep = selectedExerciseViewModel.getSelectedExercise().getValue();
        workout = workoutsViewModel.workoutsModel.exerciseToList(ep);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        LinearLayoutManager lm2 = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        exerciseRecycler.setLayoutManager(lm2);
        exerciseAdapter = new MyExpandableAdapter(
                workoutsViewModel.workoutsModel.expandExercise(ep),
                getContext(),
                null,
                null,
                null,
                null
        );
        adapter = new MyExpandableAdapter(
                workout.exArray,
                getContext(),
                null,
                null,
                null,
                null
        );
        recyclerView.setAdapter(adapter);
        exerciseRecycler.setAdapter(exerciseAdapter);
    }
}