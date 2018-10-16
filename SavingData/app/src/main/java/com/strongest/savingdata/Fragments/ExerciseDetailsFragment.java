package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AModels.ExerciseModel;
import com.strongest.savingdata.AViewModels.SelectedExerciseViewModel;
import com.strongest.savingdata.AViewModels.SelectedLogDataViewModel;
import com.strongest.savingdata.AViewModels.SelectedSetViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Activities.ExerciseDetailsActivity;
import com.strongest.savingdata.Adapters.ExerciseRecyclerAdapter;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.SetsItemAdapter;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.Controllers.OnExerciseInfo;
import com.strongest.savingdata.Controllers.UISetsClickHandler;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.LogData;
import com.strongest.savingdata.Database.LogDataManager;
import com.strongest.savingdata.Dialogs.LogModeDialog;
import com.strongest.savingdata.Handlers.MaterialDialogHandler;
import com.strongest.savingdata.LogDataActivity;
import com.strongest.savingdata.MyViews.ExerciseStatsView;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.MyViews.SmartEmptyView;
import com.strongest.savingdata.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class ExerciseDetailsFragment extends BaseFragment implements
        Architecture.view.LongClickView, UISetsClickHandler, OnExerciseInfo {

    private static final String DONT_DISPLAY_CHECKBOX = "dont_display_checkbox";
    private static final String EXERCISE_POSITION = "exercise_position";
    @BindView(R.id.textview_title)
    TextView textview_title;
    @BindView(R.id.stats)
    ExerciseStatsView stats;

    @BindView(R.id.details_fragment_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_details_recycler_exercises)
    RecyclerView exerciseRecycler;
    @BindView(R.id.exercise_details_activity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.exercise_detail_expandable_layout)
    ExpandableLayout expandableLayout;

    @BindView(R.id.transparent_wrapper)
    ViewGroup expandableTransparentWrapper;

    @BindView(R.id.exercise_detail_fab)
    FloatingActionButton addSetFab;

    MyExpandableAdapter adapter;
    ExerciseRecyclerAdapter exerciseAdapter;
    private Workout workout = new Workout();


    @BindView(R.id.fragment_details_longclick)
    LongClickMenuView longClickMenuView;

    /*@BindView(R.id.exercise_info)
    View exerciseInfo;
*/

    @BindView(R.id.toLog)
    Button saveToLogBtn;


    @BindView(R.id.fragment_exercise_details_smartemptyview)
    SmartEmptyView smartEmptyView;

    @BindView(R.id.collapse_container)
    View collapseContainer;

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

    private Handler handler = new Handler();

    private int exercisePosition;

    public static ExerciseDetailsFragment getInstance(String fragment, int position) {
        ExerciseDetailsFragment f = new ExerciseDetailsFragment();
        Bundle b = new Bundle();
        b.putString(FRAGMENT_TAG, fragment);
        b.putInt(EXERCISE_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        //setOnBackPress(view, () -> getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentFragmentId = getArguments().getString(FRAGMENT_TAG);
            exercisePosition = getArguments().getInt(EXERCISE_POSITION);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FRAGMENT_TAG, parentFragmentId);
        outState.putInt(EXERCISE_POSITION, exercisePosition);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            parentFragmentId = savedInstanceState.getString(FRAGMENT_TAG);
            exercisePosition = savedInstanceState.getInt(EXERCISE_POSITION);

        }


        selectedExerciseViewModel = ViewModelProviders.of(getActivity()).get(parentFragmentId, SelectedExerciseViewModel.class);
        logDataManager = new LogDataManager(getContext());
        //instantiating the cloned exercise profile
        getExercise();
        if (exerciseProfile == null) {
            getFragmentManager().popBackStack();
            return;
        }
        stats.instantiate(exerciseProfile);
        setsItemAdapter = new SetsItemAdapter(exerciseProfile);
        initToolbar();
        initAdapters();
        initRecycler();
        getList();
        workoutsViewModel = ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);
        //workout = selectedExerciseViewModel.getExerciseAsList().getValue();

        longClickMenuView.instantiate(this);


        expandableTransparentWrapper.setOnClickListener(v -> {
            expandableLayout.collapse();
            getFragmentManager().popBackStack();
        });

        saveToLogBtn.setOnClickListener(saveLog -> {
            // logDataManager.insert(exerciseProfile);
            if (exerciseProfile.getExercise() != null) {
                Intent i = new Intent(getContext(), LogDataActivity.class);
                i.putExtra(LogDataActivity.EXERCISE, exerciseProfile);
                i.putExtra(LogDataActivity.DATE, LogDataManager.getDateString());
                startActivity(i);
            } else {
               showNoExerciseDialog();
            }

        });


    }

    private void showNoExerciseDialog(){
        MaterialDialogHandler.get()
                .defaultBuilder(getContext(), "You did not choose any exercise to log data.", "")
                .buildDialog().show();
    }

    private void initRecycler() {

        setsLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager lm2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(setsLayoutManager);

        exerciseRecycler.setLayoutManager(lm2);

        recyclerView.setAdapter(adapter);
        exerciseRecycler.setAdapter(exerciseAdapter);
        smartEmptyView
                .setTitle("You haven't created any sets for this Exercise yet.")
                .setBody("Click on the Plus Icon to add new sets.")
                .setButtonText("Add New Set")
                .noImage()
                .setUpWithRecycler(recyclerView, true, true)
                .setImage(smartEmptyView.getRocketImage())
                .setActionBtn(v -> addSetFab.callOnClick())
                .onCondition(exerciseProfile.getSets().size() == 0);

    }


    private void initToolbar() {

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        if (exerciseProfile.getExercise() != null) {
            textview_title.setText(exerciseProfile.getExercise().getName());

        }

        toolbar.setTitle("");

        addSetFab.setOnClickListener(toolBarAddIcon -> {

            selectedExerciseViewModel.getParentWorkout().getExerciseObserver().onChange(exerciseProfile, exercisePosition);
            WorkoutsModel.ListModifier.OnWith(workout, setsItemAdapter)
                    .doAddNew(workout.exArray.size()).applyWith(adapter, null, null);
            recyclerView.scrollToPosition(workout.exArray.size() - 1);
            stats.updateStats();
        });

        toolbar.setNavigationOnClickListener(navBack -> {
            getFragmentManager().popBackStack();
        });

    }

    private void initAdapters() {
        exerciseAdapter = new ExerciseRecyclerAdapter(
                getContext(),
                new ArrayList<>(),
                this
        );
        adapter = new MyExpandableAdapter(
                workout.exArray,
                getContext());
        adapter.setUiSetsClickHandler(this);
        adapter.setOnExerciseInfo(this);
        adapter.setSupersets(exerciseProfile.exerciseProfiles);
        adapter.setLongClickMenuView(longClickMenuView);


    }

    private void transitionToExerciseDetailsActivity(PLObject.ExerciseProfile exerciseProfile) {
        Intent i = new Intent(getContext(), ExerciseDetailsActivity.class);
        i.putExtra("exercise", exerciseProfile);
       /* Pair<View, String>[] pairs = new Pair[2];

        pairs[0] = Pair.create(icon, "icon");
        pairs[1] = Pair.create(textview_title, "title");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
        startActivity(i,options.toBundle());*/
        startActivity(i);
    }


    @Override
    public void onLongClickAction(LongClickMenuView longClickMenuView, WorkoutsModel.Actions action) {

        WorkoutsModel.ListModifier listModifier = WorkoutsModel.ListModifier.OnWith(workout, setsItemAdapter);
        switch (action) {

            case Delete:

                listModifier.doRemove(longClickMenuView.getSelectedPLObjects().get(0), 1)
                        .applyWith(adapter, null, null);
                adapter.notifyItemRangeChanged(0, adapter.getExArray().size());
                longClickMenuView.onHideMenu();
                break;
            case MultiDelete:
                for (PLObject plObject : longClickMenuView.getSelectedPLObjects()) {
                    listModifier.doRemove(plObject, 1);
                }
                longClickMenuView.onHideMenu();

                listModifier.applyWith(adapter, null, null);
                adapter.notifyItemRangeChanged(0, adapter.getExArray().size());
                break;

            case edit:
                onSetsClick(longClickMenuView.getHighlightedViews().get(0), longClickMenuView.getSelectedPLObjects().get(0));
                break;


        }
        selectedExerciseViewModel.getParentWorkout().getExerciseObserver().onChange(exerciseProfile, exercisePosition);

        if (workout.exArray.size() == 0) {
            longClickMenuView.onHideMenu();
        }
        stats.updateStats();

    }


    @Override
    public void onSetsClick(MyExpandableAdapter.MyExpandableViewHolder vh, PLObject plObject) {
        SelectedSetViewModel selectedSetViewModel = ViewModelProviders.of(getActivity()).get(SelectedSetViewModel.class);
        selectedSetViewModel.select((PLObject.SetsPLObject) plObject);
        selectedSetViewModel.getSelectedExerciseSet().removeObservers(this);
        selectedSetViewModel.getSelectedExerciseSet().observe(this, (exerciseSet) -> {
            adapter.notifyDataSetChanged();
            stats.updateStats();
            selectedExerciseViewModel.getParentWorkout().getExerciseObserver().onChange(exerciseProfile, exercisePosition);
        });
        selectedSetViewModel.selectExercise(exerciseProfile);
        SetsChooseSingleFragment f = SetsChooseSingleFragment.getInstance();

        addFragmentChild(getFragmentManager(), f, SetsChooseSingleFragment.SETS_CHOOSE_FRAGMENT);
        //expandableLayout.expand();

        /*setsItemAdapter.onChild((PLObject.SetsPLObject) plObject);
        adapter.notifyItemChanged(vh.getAdapterPosition());*/
    }

    @Override
    public void onSetsLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh) {
        longClickMenuView.onShowMenu(vh, plObject);
    }

    @Override
    public void onAddingIntraSet(PLObject.SetsPLObject setsPLObject, int position) {
        PLObject.SetsPLObject intraSet = new PLObject.SetsPLObject();
        intraSet.type = WorkoutLayoutTypes.IntraSet;
        intraSet.innerType = WorkoutLayoutTypes.IntraSet;
        setsPLObject.intraSets.add(intraSet);
        adapter.notifyItemChanged(workout.exArray.indexOf(setsPLObject));

        stats.updateStats();
    }

    @Override
    public void onRemoveIntraSet(PLObject.SetsPLObject setsPLObject, int intraSetPosition) {
        setsPLObject.intraSets.remove(intraSetPosition);
        adapter.notifyItemChanged(workout.exArray.indexOf(setsPLObject));

        stats.updateStats();

    }

    @Override
    public void onDuplicate(PLObject.SetsPLObject setsPLObject) {
        WorkoutsModel.ListModifier listModifier = WorkoutsModel.ListModifier.OnWith(workout,
                setsItemAdapter);
        listModifier.doDuplicate(setsPLObject).applyWith(adapter, null, null);
        selectedExerciseViewModel.getParentWorkout().getExerciseObserver().onChange(exerciseProfile, exercisePosition);
        adapter.notifyItemRangeChanged(0, adapter.getExArray().size());

        stats.updateStats();

    }

    @Override
    public void transitionToExerciseInfo(PLObject.ExerciseProfile exercise) {
        if(exercise.getExercise() == null){
            showNoExerciseDialog();
        }else{

            transitionToExerciseDetailsActivity(exercise);
        }
    }

    private void getExercise() {
        exerciseProfile = selectedExerciseViewModel.getSelectedExercise().getValue();

    }

    private void getList() {
        ExerciseModel.exerciseToWorkout(setsItemAdapter, exerciseProfile, w -> {
            workout = (Workout) w;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {

                    adapter.setExArray(workout.getExArray());
                    adapter.notifyDataSetChanged();


                }
            });

        });

        exerciseAdapter.setList(ExerciseModel.expandExerciseSupersets(exerciseProfile));
        exerciseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        stats.updateStats();
    }


}