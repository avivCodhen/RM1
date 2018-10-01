package com.strongest.savingdata;

import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.Adapters.LogDataSetsAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.SetsItemAdapter;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.Controllers.CallBacks;
import com.strongest.savingdata.Database.LogData;
import com.strongest.savingdata.Database.LogDataManager;
import com.strongest.savingdata.MyViews.ExerciseStatsView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogDataActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, CallBacks.Change {

    public static final String EXERCISE = "exercise";
    public static final String DATE = "date";
    public static final String NO_LIST = "nolist";

    boolean fromData;
    @BindView(R.id.log_data_Recycler)
    RecyclerView recyclerView;

    /*@BindView(R.id.fragment_log_data_saveexittoolbar)
    SaveExitToolBar saveExitToolBar;
*/

    @BindView(R.id.logdata_frag_toolbar)
    Toolbar toolbar;

    @BindView(R.id.log_data_appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.doc_iv)
    ImageView docIV;

    @BindView(R.id.logdata_activity_date)
    TextView dateTV;

    @BindView(R.id.saveLogFab)
    FloatingActionButton saveLogFab;

    @BindView(R.id.addSetFab)
    FloatingActionButton addSetFab;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @BindView(R.id.logdata_statsview)
    ExerciseStatsView exerciseStatsView;

    LogDataSetsAdapter logDataSetsAdapter;
    ArrayList<LogData.LogDataSets> list;
    String date;
    PLObject.ExerciseProfile exercise;
    LogDataManager logDataManager;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXERCISE, exercise);
        outState.putString(DATE, date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_data);
        ButterKnife.bind(this);
        exercise = (PLObject.ExerciseProfile) getIntent().getSerializableExtra(EXERCISE);
        date = getIntent().getStringExtra(DATE);

        if (savedInstanceState != null) {
            exercise = (PLObject.ExerciseProfile) savedInstanceState.getSerializable(EXERCISE);
            date = savedInstanceState.getString(DATE);
        }
        dateTV.setText(date);
        toolbarTitle.setText(exercise.getExercise().getName());
        logDataManager = new LogDataManager(this);
        list = logDataManager.readSets(exercise.getExercise().getName(), date);
        if (list == null || list.size() == 0) {
            list = logDataManager.exerciseToLogDataSetList(exercise);
            fromData = true;
        }
        exerciseStatsView.instantiate(list);
        logDataSetsAdapter = new LogDataSetsAdapter(this, list, fromData, this);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(logDataSetsAdapter);
      /*  saveExitToolBar.instantiate()
        .showBack(true)
        .setSaveButton(v->getFragmentManager().popBackStack())
        .showCancel(false);
*/

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        appBarLayout.addOnOffsetChangedListener(this);

        saveLogFab.setOnClickListener(saveView -> {
            logDataManager.insert(exercise.getExercise().getName(), list);
            Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> finish(), 1000);
        });

        addSetFab.setOnClickListener(add -> {
            ArrayList<LogData.LogDataSets> logDataSets = logDataManager.setToLogDataSetList(new SetsItemAdapter(exercise).insert(), exercise.getSets().size()-1);
            list.addAll(logDataSets);
            logDataSetsAdapter.notifyItemRangeInserted(list.size() - 1, logDataSets.size());
            exerciseStatsView.updateStats();
        });

        if(fromData){
            saveLogFab.hide();
            addSetFab.hide();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            MyJavaAnimator.fadeIn(300, docIV);
            MyJavaAnimator.fadeIn(300, exerciseStatsView);
        } else {
            MyJavaAnimator.fadeOut(0, docIV);
            MyJavaAnimator.fadeOut(0, exerciseStatsView);
        }
    }

    @Override
    public void onChange() {
        exerciseStatsView.updateStats();
    }
}
