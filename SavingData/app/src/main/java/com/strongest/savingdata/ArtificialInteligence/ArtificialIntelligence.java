package com.strongest.savingdata.ArtificialInteligence;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.strongest.savingdata.AlgorithmStats.Calculator;
import com.strongest.savingdata.AlgorithmStats.ExerciseStats;
import com.strongest.savingdata.AlgorithmStats.ProgressStats;
import com.strongest.savingdata.AlgorithmStats.StatsHolder;
import com.strongest.savingdata.BaseWorkout.Programmer;
import com.strongest.savingdata.AlgorithmLayout.PLObject.BodyText;
import com.strongest.savingdata.R;

import java.util.ArrayList;

/**
 * Created by Cohen on 11/26/2017.
 */

public class ArtificialIntelligence implements ArtificialIntelligenceObserver {


    private AiFilter filter;
    private Context context;
    private Programmer programmer;
    public String[] suggestions;
    public String[] notes;
    private ArrayList<BodyText> body = new ArrayList<>();
    public ArrayList<AiActions.Analysis> aiAnalysis;


    private int currentWorkoutPosition;

    public ArtificialIntelligence(Context context, Programmer programmer) {

        this.context = context;
        filter = new AiFilter(context);
        this.programmer = programmer;
        // suggestions = learnSuggestions(0);
        //notes = learnNotes();
        /*if (programmer.getLayoutManager().getLayout().size() != 0)
        learnStats(0);*/
    }

    public ArrayList<AiActions.Analysis> getAiActionses() {
        return aiAnalysis;
    }


    public void learnStats(int workoutPosition) {
       /* body = new ArrayList<>();
        ArrayList<PLObjects> layout =
                programmer.getLayoutManager().initRecyclerMatrixLayout(false).get(workoutPosition);
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i).getType() == WorkoutLayoutTypes.BodyView) {
                BodyText bt = (BodyText) layout.get(i);
                body.add(bt);
            }
        }
//        deepStatsAnalysis();*/
    }

    public Resources r() {
        return context.getResources();
    }

    private void deepStatsAnalysis() {
        aiAnalysis = new ArrayList<>();
        ProgressStats userProgressStats;
        StatsHolder userStatsHolder;
        ExerciseStats userExerciseStats;
        Calculator.ProgressStatsMinMaxBean minMaxBean;
        for (BodyText bt : body) {
            userStatsHolder = bt.getStatsHolder();
            userProgressStats = userStatsHolder.getProgressStats();
            userExerciseStats = userStatsHolder.getExerciseStats();
            minMaxBean = userStatsHolder.getMinMaxBean();

            //checks if user picked too much exercises (higher than maximum)
            //category: analysis
            if (userExerciseStats != null) {

                if (minMaxBean.getMaxExercise() < userExerciseStats.getNumOfExercises()) {

                }

                //checks if user picked too little exercises (lower than minimum)
                if (minMaxBean.getMinExercise() > userExerciseStats.getNumOfExercises()) {
                    aiAnalysis.add(new AiActions.Analysis(filter.filterAction(
                            R.string.ai_not_enough_exercises,
                            minMaxBean.getMinExercise(),
                            bt.getMuscle().getMuscle_name()),
                            false));
                    Log.d("aviv", "deepStatsAnalysis: ");
                }

                //checks if user picked too many complex exercises
                if (minMaxBean.getMaxC() < userExerciseStats.getNumOfComplex()) {

                }

                //checks if user picked too many methods
                if (minMaxBean.getMaxMethod() < userExerciseStats.getNumOfMethod()) {

                }
            } else {
                aiAnalysis.add(new AiActions.Analysis("You have not yet added any exercises.", false));
            }


            //checks if damage picked is too high
            if (userStatsHolder.getDamage() <= userProgressStats.getMinDamage()) {
                //    aiAnalysis.add();

            }

            //checks if metabolic is too high
            if (userStatsHolder.getMetabolic() <= userProgressStats.getMinMetabolic()) {
                // add aiaction

            }

            //checks if mechanical is too high
            if (userStatsHolder.getMechanical() <= userProgressStats.getMinMetabolic()) {
                //add aiaction

            }
        }
    }


    public void setCurrentWorkoutPosition(int currentWorkoutPosition) {
        this.currentWorkoutPosition = currentWorkoutPosition;
    }


    public int getCurrentWorkoutPosition() {
        return currentWorkoutPosition;
    }

    @Override
    public void updatePosition(int workoutPosition) {
        currentWorkoutPosition = workoutPosition;
        learnStats(currentWorkoutPosition);

    }

    private class aiSpecificStats {

        private int numOfComplex;

    }


}
