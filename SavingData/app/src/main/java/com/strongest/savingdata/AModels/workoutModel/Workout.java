package com.strongest.savingdata.AModels.workoutModel;

import com.strongest.savingdata.Adapters.WorkoutItemAdapters.Lister;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.SpecificExerciseObserver;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.WorkoutObserver;

import java.util.ArrayList;
import java.util.List;

public class Workout implements Lister {

    public ArrayList<PLObject> exArray = new ArrayList<>();
    //private ArrayList<PLObject> parents = new ArrayList<>();
    public String workoutName;
    private WorkoutObserver workoutObserver;
    private SpecificExerciseObserver exerciseObserver;
    private String uid;
    private String programUid;

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

    public void registerExerciseObserver(SpecificExerciseObserver o) {
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

    @Override
    public SpecificExerciseObserver getExerciseObserver() {
        return exerciseObserver;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProgramUid() {
        return programUid;
    }

    public void setProgramUid(String programUid) {
        this.programUid = programUid;
    }


   /* public ArrayList<PLObject> getParents() {
        return parents;
    }

    public void setParents(ArrayList<PLObject> parents) {
        this.parents = parents;
    }*/
}
