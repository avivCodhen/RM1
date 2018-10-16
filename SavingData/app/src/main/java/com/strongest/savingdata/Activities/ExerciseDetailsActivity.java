package com.strongest.savingdata.Activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AViewModels.SelectedExerciseViewModel;
import com.strongest.savingdata.AViewModels.SelectedLogDataViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Adapters.LogDataAdapter;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.SetsItemAdapter;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.Controllers.LogDataAdapterOnClick;
import com.strongest.savingdata.Controllers.UISetsClickHandler;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.LogData;
import com.strongest.savingdata.Database.LogDataManager;
import com.strongest.savingdata.Fragments.ExerciseLogFragment;
import com.strongest.savingdata.Handlers.YoutubeHandler;
import com.strongest.savingdata.LogDataActivity;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ExerciseDetailsActivity extends BaseActivity implements
        Architecture.view.LongClickView, AppBarLayout.OnOffsetChangedListener, LogDataAdapterOnClick {

    @BindView(R.id.textview_title)
    TextView textview_title;

    @BindView(R.id.toolbar_add_set_btn)
    ImageView toolbarAddSet;

    @BindView(R.id.testIv)
    CircleImageView testIv;

    @BindView(R.id.details_fragment_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.exercise_details_activity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.youtube_fragment)
    FrameLayout youtubeFragment;

    @BindView(R.id.muscles_tv)
    TextView musclesTV;

    @BindView(R.id.type_tv)
    TextView typeTV;


    LogDataAdapter adapter;

    private PLObject.ExerciseProfile exercise;
    private YoutubeHandler youtubeHandler;

    /**
     * this is related to the toolbar and appbar offsets
     */

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.3f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    LogDataManager m;


    Animation slideRight, slideTop;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        if (savedInstanceState != null) {
            exercise = (PLObject.ExerciseProfile) savedInstanceState.getSerializable("exercise");
        }
        exercise = (PLObject.ExerciseProfile) getIntent().getSerializableExtra("exercise");

        ButterKnife.bind(this);
        testIv.setTransitionName(getIntent().getStringExtra("transitionName"));
        initToolbar();
        initRecycler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        youtubeHandler = YoutubeHandler.getHandler(this);
        youtubeHandler.init(R.id.youtube_fragment, getSupportFragmentManager())
                .searchOnYoutube(exercise.getExercise().getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        youtubeHandler.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initRecycler() {

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(lm);
        initAdapters();
        // setsItemAdapter = new SetsItemAdapter(exerciseProfile);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putSerializable("exercise", exercise);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        exercise = ((PLObject.ExerciseProfile) getIntent().getSerializableExtra("exercise"));

    }

    private void initToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (exercise != null) {
            textview_title.setText(exercise.getExercise().getName());

        }
        if (exercise.getMuscle() != null) {
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(exercise.getMuscle());
            testIv.setImageResource(mui.getImage());
            musclesTV.setText(Muscle.getParsedMuscles(exercise.getExercise().getMuscles()));
        }

        typeTV.setText(exercise.getExercise().getType());
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener((navClicked) -> {
            finish();
        });
    }

    private void initAdapters() {
        //workout = workoutsViewModel.workoutsModel.exerciseToList(ep);
        m = new LogDataManager(this);
        ArrayList<LogData> dates;

        try {
            dates = m.readDates(exercise.getExercise().getName());
        } catch (Exception e) {
            dates = new ArrayList<>();
            dates.add(new LogData("", "No Record Found", ""));
        }
        adapter = new LogDataAdapter(dates);
        adapter.setLogDataAdapterOnClick(this);

    }

    @Override
    public void onLongClickAction(LongClickMenuView longClickMenuView, WorkoutsModel.Actions action) {

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        // handleAlphaOnTitle(percentage);
        // handleToolbarTitleVisibility(percentage);
    }

    /*   @Override
       public boolean onCreateOptionsMenu(Menu menu) {
           getMenuInflater().inflate(R.menu.aviv_menu_program, menu);

           return true;
       }
   */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return true;
        }
        if (item.getItemId() == R.id.edit_menu) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dateClicked(String date) {
       /* SelectedLogDataViewModel selectedLogDataViewModel = ViewModelProviders.of(this)
                .get(SelectedLogDataViewModel.class);
        ArrayList<LogData.LogDataSets> sets = m.readSets(exercise.getExercise().getName(), date);
        selectedLogDataViewModel.setSets(sets);
        addFragmentToActivity(R.id.exercise_log_frame, new ExerciseLogFragment(), "log");*/

        Intent intent = new Intent(this, LogDataActivity.class);
        intent.putExtra(LogDataActivity.EXERCISE, exercise);
        intent.putExtra(LogDataActivity.DATE, date);
        intent.putExtra(LogDataActivity.FROM_DATA, true);
        startActivity(intent);
    }

}
