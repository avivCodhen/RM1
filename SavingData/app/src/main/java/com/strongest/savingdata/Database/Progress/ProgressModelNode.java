package com.strongest.savingdata.Database.Progress;

import com.strongest.savingdata.Database.Exercise.BeansHolder;

/**
 * Created by Cohen on 12/27/2017.
 */

public class ProgressModelNode {

    private BeansHolder oldBeansHolder;
    private BeansHolder newBeansHolder;
    private int workoutId;
    private int phase;
    private int exerciseProfileId;


    public ProgressModelNode(BeansHolder oldBeansHolder, BeansHolder newBeansHolder, int workoutId, int exerciseProfileId, int order){
        this.workoutId = workoutId;
        this.exerciseProfileId = exerciseProfileId;
        this.phase = order;
    }

    public BeansHolder getOldBeansHolder() {
        return oldBeansHolder;
    }

    public void setOldBeansHolder(BeansHolder oldBeansHolder) {
        this.oldBeansHolder = oldBeansHolder;
    }

    public BeansHolder getNewBeansHolder() {
        return newBeansHolder;
    }

    public void setNewBeansHolder(BeansHolder newBeansHolder) {
        this.newBeansHolder = newBeansHolder;
    }

    public int getWorkoutId() {
        return workoutId;
    }


    public int getExerciseProfileId() {
        return exerciseProfileId;
    }

    public int getPhase() {
        return phase;
    }
}
