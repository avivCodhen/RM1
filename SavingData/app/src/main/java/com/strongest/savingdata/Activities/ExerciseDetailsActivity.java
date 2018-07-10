package com.strongest.savingdata.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.AModels.ExerciseModel;
import com.strongest.savingdata.AViewModels.SelectedExerciseViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.SetsItemAdapter;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.Controllers.UISetsClickHandler;
import com.strongest.savingdata.Fragments.ExerciseDetailsFragment;
import com.strongest.savingdata.Fragments.SetsDetailsFragment;
import com.strongest.savingdata.Handlers.YoutubeHandler;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseDetailsActivity extends AppCompatActivity implements Architecture.view.LongClickView, UISetsClickHandler {

    @BindView(R.id.test)
    TextView test;
    @BindView(R.id.testIv)
    ImageView testIv;

    @BindView(R.id.details_fragment_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_details_recycler_exercises)
    RecyclerView exerciseRecycler;
    @BindView(R.id.fragment_details_saveExitToolbar)
    SaveExitToolBar saveExitToolBar;

    @BindView(R.id.play_fav)
    FloatingActionButton fab;

    @BindView(R.id.youtube_expand_layout)
    ExpandableLayout el;

    @BindView(R.id.add_set_btn)
    Button addSet;

    MyExpandableAdapter adapter;
    MyExpandableAdapter exerciseAdapter;
    private Workout workout;

    @BindView(R.id.exercise_details_activity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_details_longclick)
    LongClickMenuView longClickMenuView;

    private SelectedExerciseViewModel selectedExerciseViewModel;
    private WorkoutsViewModel workoutsViewModel;
    private SetsItemAdapter setsItemAdapter;

    private PLObject.ExerciseProfile exerciseProfile;
    private YoutubeHandler youtubeHandler;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);
        ButterKnife.bind(this);
        getWindow().setStatusBarColor(R.color.colorAccent);
        exerciseProfile = new PLObject.ExerciseProfile((PLObject.ExerciseProfile) getIntent().getSerializableExtra("exercise"));
        test.setTransitionName("q");
        testIv.setTransitionName("q1");
        toolbar.setNavigationIcon(R.drawable.checkmark_48_whitepx);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (exerciseProfile.getExercise() != null) {
            test.setText(exerciseProfile.getExercise().getName());

        }
        if (exerciseProfile.getMuscle() != null) {
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(exerciseProfile.getMuscle());
            testIv.setImageResource(mui.getImage());
        }
        toolbar.setTitle("");
        LinearLayoutManager lm = new LinearLayoutManager(this);
        LinearLayoutManager lm2 = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        exerciseRecycler.setLayoutManager(lm2);
        initAdapters();
        setsItemAdapter = new SetsItemAdapter(exerciseProfile);
        recyclerView.setAdapter(adapter);
        exerciseRecycler.setAdapter(exerciseAdapter);
        longClickMenuView.instantiate(this);
        initToolbar();

        fab.setOnClickListener((v) -> {

            if (youtubeHandler == null) {
                youtubeHandler = YoutubeHandler.getHandler(this).init(R.id.youtube_fragment, getSupportFragmentManager());
                if (exerciseProfile.getExercise() != null) {
                    youtubeHandler.searchOnYoutube(exerciseProfile.getExercise().getName());
                }
            }
            el.toggle();
            if (el.isExpanded()) {
                fab.setImageResource(R.drawable.delete_24px_white);
            } else {
                fab.setImageResource(R.drawable.play_24px_white);

            }
        });

        addSet.setOnClickListener((view) -> {
            WorkoutsModel.ListModifier.OnWith(workout, setsItemAdapter)
                    .doAddNew(workout.exArray.size()).applyWith(adapter);
            recyclerView.scrollToPosition(workout.exArray.size() - 1);
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            exerciseProfile = (PLObject.ExerciseProfile) outState.getSerializable("exercise");
        }
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
                ExerciseModel.expandExercise(exerciseProfile),
                this);
        exerciseAdapter.setLeanLayout(true);

        workout = ExerciseModel.exerciseToList(exerciseProfile);
        // workout.exArray.add(new PLObject.ExerciseStats(exerciseProfile));
        adapter = new MyExpandableAdapter(
                workout.exArray,
                this);
        adapter.setUiSetsClickHandler(this);

        adapter.setUiSetsClickHandler(this);
    }

    @Override
    public void onLongClickAction(LongClickMenuView longClickMenuView, WorkoutsModel.Actions action) {

    }

    @Override
    public void onSetsClick(MyExpandableAdapter.SetsViewHolder vh, PLObject plObject) {
        Intent i = new Intent(this, SetsDetailsActivity.class);
        i.putExtra("set", plObject);

        Pair<View, String>[] pairs = new Pair[3];
        vh.restIcon.setTransitionName("icon1");
        vh.repsIcon.setTransitionName("icon2");
        vh.weightIcon.setTransitionName("icon3");
        pairs[0] = Pair.create(vh.restIcon, vh.restIcon.getTransitionName());
        pairs[1] = Pair.create(vh.repsIcon, vh.repsIcon.getTransitionName());
        pairs[2] = Pair.create(vh.weightIcon, vh.weightIcon.getTransitionName());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, options.toBundle());

    }

    @Override
    public void onSetsLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh) {
        longClickMenuView.onShowMenu(vh, plObject);
    }
}
