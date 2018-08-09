package com.strongest.savingdata.AModels.AlgorithmLayout;

import java.util.ArrayList;

public class Workout implements Lister {

    public ArrayList<PLObject> exArray = new ArrayList<>();
    private ArrayList<PLObject> parents = new ArrayList<>();
    public String workoutName;
    private WorkoutObserver workoutObserver;
    private SpecifiecExerciseObserver exerciseObserver;

    public  Workout(){
    }

    public ArrayList<PLObject> getExArray() {
        return exArray;
    }

    public void setExArray(ArrayList<PLObject> exArray) {
        this.exArray = exArray;

    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public void registerObserver(WorkoutObserver wo) {
       workoutObserver = wo;
    }

    public void registerExerciseObserver(SpecifiecExerciseObserver o) {
        this.exerciseObserver = o;
    }


    @Override
    public WorkoutObserver getObserver() {
        return workoutObserver;
    }

    @Override
    public ArrayList getList() {
        return exArray;
    }

    public SpecifiecExerciseObserver getExerciseObserver() {
        return exerciseObserver;
    }


    public ArrayList<PLObject> getParents() {
        return parents;
    }

    public void setParents(ArrayList<PLObject> parents) {
        this.parents = parents;
    }
}
