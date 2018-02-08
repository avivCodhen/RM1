package com.strongest.savingdata.AlgorithmStats;

import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Managers.DataManager;

import java.util.ArrayList;

/**
 * Created by Cohen on 10/20/2017.
 */

public class StatsCalculatorManager {

    private ArrayList<ProgressStats> progressStats;
    private StatsHolder statsHolder;
    private SParams[] paramsProgressStats;

    private DataManager dataManager;
    private LayoutManager layoutManager;
    private Calculator calculator;

    public StatsCalculatorManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        this.dataManager = layoutManager.getDataManager();
        calculator = new Calculator(layoutManager);
    }


    public StatsHolder calculateStatsHolder(Muscle muscle,
                                            int position,
                                            boolean haveStatsHolder) {
        Calculator.ProgressStatsMinMaxBean s = getMinMax(position);
        return calculator.calculateStats(muscle, position, s, haveStatsHolder);
    }

    public Calculator.ProgressStatsMinMaxBean getMinMax(int workoutPosition) {
        SParams sParams = getSParams(workoutPosition);
        Calculator.ProgressStatsMinMaxBean s = dataManager.getStatsDataManager().fetchStats(sParams.getRoutine(), sParams.getLevel());
        return s;
    }

    public SParams getSParams(int position) {
       /* int frequency = programLayoutManager.getmuscleFrequency(position);
        int workoutSize = programLayoutManager.getNumOfWorkouts();
        int level = dataManager.getPrefs().getInt(dataManager.EXERCISE_LEVEL, 0);
        int score = frequency / workoutSize;
        int routine;
        if (score == 1) {
            routine = FBW.ordinal();
        } else if (score > 0.5 && score < 1) {
            routine = AB.ordinal();
        } else if (score > 0.3 && score < 0.5) {
            routine = ABC.ordinal();
        } else {
            routine = ABCDE.ordinal();
        }*/

        //these lines fetch the user level, and the routine to base the stats calculations
        // it makes sure to avoid extra calculations and retrieve only the default routine
        //incase it is not custom made
        int level = dataManager.getPrefs().getInt(dataManager.EXERCISE_LEVEL, 0);
        String routine;
            routine = layoutManager.getMusclesRoutine().get(position);

        return new SParams(routine, level);
    }
}
