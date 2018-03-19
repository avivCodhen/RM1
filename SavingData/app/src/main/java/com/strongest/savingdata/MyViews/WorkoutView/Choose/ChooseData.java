package com.strongest.savingdata.MyViews.WorkoutView.Choose;

import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;

import java.util.ArrayList;

/**
 * Created by Cohen on 11/16/2017.
 */

public class ChooseData {

    private ArrayList<ExerciseSet> sets;
    private Muscle muscle;

    public ChooseData(){

    }

    public ArrayList<ExerciseSet> getSets() {
        return sets;
    }

    public void setSets(ArrayList<ExerciseSet> sets) {
        this.sets = sets;
    }

    public Muscle getMuscle() {
        return muscle;
    }

    public void setMuscle(Muscle muscle) {
        this.muscle = muscle;
    }
}
