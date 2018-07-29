package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.AModels.ExerciseModel;
import com.strongest.savingdata.AViewModels.SelectedExerciseViewModel;
import com.strongest.savingdata.AViewModels.SelectedSetViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Activities.ExerciseDetailsActivity;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.SetsItemAdapter;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.Controllers.OnExerciseInfo;
import com.strongest.savingdata.Controllers.UISetsClickHandler;
import com.strongest.savingdata.Controllers.UiExerciseClickHandler;
import com.strongest.savingdata.Database.LogDataManager;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class ExerciseDetailsFragment extends BaseFragment implements
        Architecture.view.LongClickView, UISetsClickHandler, OnExerciseInfo {

    @BindView(R.id.textview_title)
    TextView textview_title;
    @BindView(R.id.stats)
    View stats;

    @BindView(R.id.toolbar_add_set_btn)
    ImageView toolbarAddSet;

    @BindView(R.id.details_fragment_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_details_recycler_exercises)
    RecyclerView exerciseRecycler;
    @BindView(R.id.exercise_details_activity_toolbar)
    Toolbar toolbar;

    MyExpandableAdapter adapter;
    MyExpandableAdapter exerciseAdapter;
    private Workout workout;

    @BindView(R.id.testIv)
    CircleImageView icon;

    @BindView(R.id.fragment_details_longclick)
    LongClickMenuView longClickMenuView;

    @BindView(R.id.exercise_info)
    View exerciseInfo;

    @BindView(R.id.youtube_expand_layout)
    ExpandableLayout el;

    @BindView(R.id.logBtn)
    Button logBtn;

    @BindView(R.id.saveToLogBtn)
    Button saveToLogBtn;

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
    private LinearLayoutManager setsLayoutManager;
    private LogDataManager logDataManager;

    Animation slideRight, slideTop;


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
        postponeEnterTransition();
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
        icon.setTransitionName("q1");
        selectedExerciseViewModel = ViewModelProviders.of(getActivity()).get(parentFragmentId, SelectedExerciseViewModel.class);
        logDataManager = new LogDataManager(getContext());
        //instantiating the cloned exercise profile
        exerciseProfile = selectedExerciseViewModel.getSelectedExercise().getValue();
        setsItemAdapter = new SetsItemAdapter(exerciseProfile);
        workout = ExerciseModel.exerciseToList(exerciseProfile);
        workoutsViewModel = ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);
        //workout = selectedExerciseViewModel.getExerciseAsList().getValue();

        initAdapters();
        initRecycler();

        longClickMenuView.instantiate(this);
        initToolbar();


        if (exerciseProfile.getMuscle() != null) {
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(exerciseProfile.getMuscle());
            icon.setImageResource(mui.getImage());
        }
        startPostponedEnterTransition();


        slideRight = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_in_right);
        slideTop = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_in_up);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stats.setVisibility(View.VISIBLE);
                stats.startAnimation(slideRight);
                exerciseRecycler.setVisibility(View.VISIBLE);
                exerciseRecycler.startAnimation(slideTop);
            }
        }, 200);

        exerciseInfo.setOnClickListener(info ->{
            transitionToExerciseDetailsActivity();
        });

        logBtn.setOnClickListener(btnClicked ->{
            el.toggle();
        });

        saveToLogBtn.setOnClickListener(saveLog ->{
            logDataManager.insert(exerciseProfile);
        });
    }

    private void initRecycler() {

        setsLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager lm2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(setsLayoutManager);
        exerciseRecycler.setLayoutManager(lm2);
        initAdapters();
        setsItemAdapter = new SetsItemAdapter(exerciseProfile);
        recyclerView.setAdapter(adapter);
        exerciseRecycler.setAdapter(exerciseAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }
    private void initToolbar() {
      /*  saveExitToolBar.instantiate();
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
        saveExitToolBar.setCancelButton(v -> getFragmentManager().popBackStack());*/

        toolbar.setNavigationIcon(R.drawable.icon_back_white);
        if (exerciseProfile.getExercise() != null) {
            textview_title.setText(exerciseProfile.getExercise().getName());

        }
        if (exerciseProfile.getMuscle() != null) {
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(exerciseProfile.getMuscle());
            icon.setImageResource(mui.getImage());
        }
        toolbar.setTitle("");

        toolbarAddSet.setOnClickListener(toolBarAddIcon -> {
            selectedExerciseViewModel.getParentWorkout().getExerciseObserver().onChange(exerciseProfile);
            WorkoutsModel.ListModifier.OnWith(workout, setsItemAdapter)
                    .doAddNew(workout.exArray.size()).applyWith(adapter);

        });

        toolbar.setNavigationOnClickListener(navBack ->{
            getFragmentManager().popBackStack();
            workoutsViewModel.saveLayoutToDataBase();
        });

    }

    private void initAdapters() {
        //workout = workoutsViewModel.workoutsModel.exerciseToList(ep);

        adapter = new MyExpandableAdapter(
                workout.exArray,
                getContext());

        exerciseAdapter = new MyExpandableAdapter(
                ExerciseModel.expandExercise(exerciseProfile),
                getContext());
        exerciseAdapter.setLeanLayout(true);

        adapter = new MyExpandableAdapter(
                workout.exArray,
                getContext());
        adapter.setUiSetsClickHandler(this);
        adapter.setOnExerciseInfo(this);

    }

    private void transitionToExerciseDetailsActivity(){
        Intent i = new Intent(getContext(), ExerciseDetailsActivity.class);
        i.putExtra("exercise", exerciseProfile.getExercise());
        startActivity(i);
    }


    @Override
    public void onLongClickAction(LongClickMenuView longClickMenuView, WorkoutsModel.Actions action) {

        WorkoutsModel.ListModifier listModifier = WorkoutsModel.ListModifier.OnWith(workout, setsItemAdapter);
        switch (action) {

            case Delete:

                listModifier.doRemove(longClickMenuView.getSelectedPLObjects().get(0), 1)
                        .applyWith(adapter);
                adapter.notifyItemRangeChanged(0, adapter.getExArray().size()-1);
                longClickMenuView.onHideMenu();
                break;
            case MultiDelete:
                for (PLObject plObject : longClickMenuView.getSelectedPLObjects()) {
                    listModifier.doRemove(plObject, 1);
                }
                longClickMenuView.onHideMenu();

                listModifier.applyWith(adapter);
                adapter.notifyItemRangeChanged(0, adapter.getExArray().size()-1);
                break;
            case Duplicate:
                //int position = workout.exArray.indexOf(longClickMenuView.getSelectedPLObjects().get(0));
                listModifier.doDuplicate(longClickMenuView.getSelectedPLObjects().get(0)).applyWith(adapter);
                break;

            /*case Child:
                listModifier.doChild(longClickMenuView.getSelectedPLObjects().get(0))
                        .applyWith(adapter);
                break;*/


        }
        selectedExerciseViewModel.getParentWorkout().getExerciseObserver().onChange(exerciseProfile);

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
    public void onSetsClick(MyExpandableAdapter.SetsViewHolder vh,PLObject plObject) {
       /* SelectedSetViewModel selectedSetViewModel = ViewModelProviders.of(getActivity()).get(SelectedSetViewModel.class);
        selectedSetViewModel.select((PLObject.SetsPLObject) plObject);
        selectedSetViewModel.getSelectedExerciseSet().removeObservers(this);
        selectedSetViewModel.getSelectedExerciseSet().observe(this, (exerciseSet) -> {
            adapter.notifyDataSetChanged();
        });

        SetsChooseSingleFragment f = SetsChooseSingleFragment.getInstance();

        ((HomeActivity) getActivity()).addFragmentToHomeActivity(f, "setsFragment");*/

       setsItemAdapter.onChild((PLObject.SetsPLObject) plObject);
       adapter.notifyItemChanged(vh.getAdapterPosition());
    }

    @Override
    public void onSetsLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh) {
        longClickMenuView.onShowMenu(vh, plObject);
    }

    @Override
    public void onAddingIntraSet(PLObject.SetsPLObject setsPLObject, int position) {

    }

    @Override
    public void onRemoveIntraSet(PLObject.SetsPLObject setsPLObject) {

    }

    @Override
    public void transitionToExerciseInfo() {
        transitionToExerciseDetailsActivity();
    }
}