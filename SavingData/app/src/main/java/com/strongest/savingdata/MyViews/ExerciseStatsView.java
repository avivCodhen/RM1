package com.strongest.savingdata.MyViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.AModels.workoutModel.ExerciseProfileStats;
import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.Database.LogData;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseStatsView extends LinearLayout {


    private Context context;
    /*@BindView(R.id.exercise_stats_viewpager)
    private ViewPager viewPager;
*/

    TextView sets;
    TextView reps;
    TextView rest;
    TextView volume;

    private Adapter adapter;
    private ExerciseProfileStats exerciseProfileStats;
    private ArrayList<LogData.LogDataSets> logDataSets;
    private PLObject.ExerciseProfile exerciseProfile;

    public ExerciseStatsView(Context context) {
        super(context);
        this.context = context;
    }

    public ExerciseStatsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void instantiate(PLObject.ExerciseProfile exerciseProfile) {
        this.exerciseProfile = exerciseProfile;
        this.exerciseProfileStats = ExerciseProfileStats.getInstance(exerciseProfile);
        this.logDataSets = null;
        init();
        showStats();

    }

    public void instantiate(ArrayList<LogData.LogDataSets> logDataSets) {
        this.exerciseProfileStats = ExerciseProfileStats.getInstance(logDataSets);
        this.logDataSets = logDataSets;
        this.exerciseProfile = null;
        init();
        showStats();
    }

    public void updateStats() {
        if (exerciseProfile == null)
            this.exerciseProfileStats = ExerciseProfileStats.getInstance(logDataSets);
        else {
            this.exerciseProfileStats = ExerciseProfileStats.getInstance(exerciseProfile);
        }

        showStats();
    }

    private void showStats() {
        reps.setText(exerciseProfileStats.getTotalReps()+"");
        rest.setText(exerciseProfileStats.getTotalRest()+"");
        sets.setText(exerciseProfileStats.getTotalSets()+"");
        volume.setText(exerciseProfileStats.getTotalVolume() + "");
    }

    private void init() {
        inflate(context, R.layout.layout_exercise_stats, this);
        sets = findViewById(R.id.exercise_stats_sets);
        reps = findViewById(R.id.exercise_stats_reps);
        volume = findViewById(R.id.exercise_stats_weight);
        rest = findViewById(R.id.exercise_stats_rest);

    }


    private class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
    }
}
