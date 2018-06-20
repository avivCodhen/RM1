package com.strongest.savingdata.AModels.AlgorithmLayout;

import java.util.ArrayList;

public class Workout implements Lister {

    public ArrayList<PLObject> exArray = new ArrayList<>();
    public String workoutName;
    private WorkoutObserver workoutObserver;

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

   /* public void notifyChanges(){
        workoutObserver.onChange(exArray);

    }*/

    @Override
    public WorkoutObserver getObserver() {
        return workoutObserver;
    }

    @Override
    public ArrayList getList() {
        return exArray;
    }
}
