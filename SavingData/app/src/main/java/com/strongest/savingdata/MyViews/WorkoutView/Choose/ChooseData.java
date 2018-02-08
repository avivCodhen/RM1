package com.strongest.savingdata.MyViews.WorkoutView.Choose;

import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.BeansHolder;

import java.util.ArrayList;

/**
 * Created by Cohen on 11/16/2017.
 */

public class ChooseData {

    private ArrayList<BeansHolder> beansHolder;
    private Muscle muscle;

    public ChooseData(){

    }

    public ArrayList<BeansHolder> getBeansHolder() {
        return beansHolder;
    }

    public void setBeansHolder(ArrayList<BeansHolder> beansHolder) {
        this.beansHolder = beansHolder;
    }

    public Muscle getMuscle() {
        return muscle;
    }

    public void setMuscle(Muscle muscle) {
        this.muscle = muscle;
    }
}
