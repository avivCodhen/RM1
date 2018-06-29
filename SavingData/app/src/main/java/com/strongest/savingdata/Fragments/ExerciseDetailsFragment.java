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
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.AViewModels.SelectedExerciseViewModel;
import com.strongest.savingdata.AViewModels.SelectedSetViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.SetsItemAdapter;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.Controllers.UISetsClickHandler;
import com.strongest.savingdata.Controllers.UiExerciseClickHandler;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExerciseDetailsFragment extends BaseFragment implements
        Architecture.view.LongClickView, UISetsClickHandler {

    @BindView(R.id.details_fragment_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_details_recycler_exercises)
    RecyclerView exerciseRecycler;
    @BindView(R.id.fragment_details_saveExitToolbar)
    SaveExitToolBar saveExitToolBar;
    MyExpandableAdapter adapter;
    MyExpandableAdapter exerciseAdapter;
    private Workout workout;

    @BindView(R.id.fragment_details_longclick)
    LongClickMenuView longClickMenuView;

    private SelectedExerciseViewModel selectedExerciseViewModel;
    private WorkoutsViewModel workoutsViewModel;
    private SetsItemAdapter setsItemAdapter;

    /**
     * this is needs to be a clone version of the selected exerciseProfile
     * based upon saving or canceling, it will either replace the exerciseprofile
     * if the client saved, or the old exerciseprofile will keep it's data
     */
    private PLObject.ExerciseProfile exerciseProfile;
    private String parentFragmentId;

    public static ExerciseDetailsFragment getInstance(String fragment){
        ExerciseDetailsFragment f = new ExerciseDetailsFragment();
        Bundle b = new Bundle();
        b.putString(FRAGMENT_TAG , fragment);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            parentFragmentId = getArguments().getString(FRAGMENT_TAG);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null){
            parentFragmentId = outState.getString(FRAGMENT_TAG);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        selectedExerciseViewModel = ViewModelProviders.of(getActivity()).get(parentFragmentId, SelectedExerciseViewModel.class);

        //instantiating the cloned exercise profile
        exerciseProfile = new PLObject.ExerciseProfile(selectedExerciseViewModel.getSelectedExercise().getValue());
        setsItemAdapter = new SetsItemAdapter(exerciseProfile);
        workoutsViewModel = ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);
        workout = selectedExerciseViewModel.getExerciseAsList().getValue();
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        LinearLayoutManager lm2 = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        exerciseRecycler.setLayoutManager(lm2);
        initAdapters();
        recyclerView.setAdapter(adapter);
        exerciseRecycler.setAdapter(exerciseAdapter);
        longClickMenuView.instantiate(this);
        initToolbar();

    }

    private void initToolbar() {
        saveExitToolBar.instantiate();
        saveExitToolBar.setOptionalTV("Add Set", (v) -> {
            WorkoutsModel.ListModifier.OnWith(workout, setsItemAdapter)
                    .doAddNew(workout.exArray.size()).applyWith(adapter);
            exerciseAdapter.notifyItemChanged(0);
        });

        saveExitToolBar.setSaveButton(v -> {
            selectedExerciseViewModel.modifyNewExerciseProfile(exerciseProfile);
            workoutsViewModel.saveLayoutToDataBase();
            getFragmentManager().popBackStack();
        });
        saveExitToolBar.setCancelButton(v -> getFragmentManager().popBackStack());
    }

    private void initAdapters() {
        //workout = workoutsViewModel.workoutsModel.exerciseToList(ep);
        exerciseAdapter = new MyExpandableAdapter(
                workoutsViewModel.workoutsModel.expandExercise(exerciseProfile),
                getContext());

        workout.exArray.add(new PLObject.ExerciseStats(exerciseProfile));
        adapter = new MyExpandableAdapter(
                workout.exArray,
                getContext());

        adapter.setUiSetsClickHandler(this);
    }


    @Override
    public void onLongClickAction(LongClickMenuView longClickMenuView, WorkoutsModel.Actions action) {

        WorkoutsModel.ListModifier listModifier = WorkoutsModel.ListModifier.OnWith(workout, setsItemAdapter);
        switch (action) {

            case Delete:

                listModifier.doRemove(longClickMenuView.getSelectedPLObjects().get(0), 1)
                        .applyWith(adapter);
                longClickMenuView.onHideMenu();
                break;
            case MultiDelete:
                for (PLObject plObject : longClickMenuView.getSelectedPLObjects()) {
                    listModifier.doRemove(plObject, 1);
                }
                longClickMenuView.onHideMenu();
                listModifier.applyWith(adapter);
                break;
            case Duplicate:
                //int position = workout.exArray.indexOf(longClickMenuView.getSelectedPLObjects().get(0));
                listModifier.doDuplicate(longClickMenuView.getSelectedPLObjects().get(0)).applyWith(adapter);
                break;

            case Child:
                listModifier.doChild(longClickMenuView.getSelectedPLObjects().get(0))
                        .applyWith(adapter);
                break;
        }

        if (workout.exArray.size() == 0) {
            longClickMenuView.onHideMenu();
        }
        if(exerciseAdapter.getExArray().size() != 0){
            exerciseAdapter.notifyItemChanged(0);
        }else{
            throw new IllegalArgumentException("No exercise? Exercise list is 0? What the fuck");
        }
    }


    @Override
    public void onSetsClick(PLObject plObject) {
        SelectedSetViewModel selectedSetViewModel = ViewModelProviders.of(getActivity()).get(SelectedSetViewModel.class);
        selectedSetViewModel.select((PLObject.SetsPLObject) plObject);
        selectedSetViewModel.getSelectedExerciseSet().removeObservers(this);
        selectedSetViewModel.getSelectedExerciseSet().observe(this, (exerciseSet) -> {
            adapter.notifyDataSetChanged();
        });

        SetsChooseSingleFragment f = SetsChooseSingleFragment.getInstance();

        ((HomeActivity) getActivity()).addFragmentToHomeActivity(f, "setsFragment");
    }

    @Override
    public void onSetsLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh) {
        longClickMenuView.onShowMenu(vh, plObject);
    }
}