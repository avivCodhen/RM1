package com.strongest.savingdata.Database.Progress;

import com.strongest.savingdata.Database.Exercise.Sets;

/**
 * Created by Cohen on 12/27/2017.
 */

public class ProgressModelNode {

    private Sets oldSets;
    private Sets newSets;
    private int workoutId;
    private int phase;
    private int exerciseProfileId;


    public ProgressModelNode(Sets oldSets, Sets newSets, int workoutId, int exerciseProfileId, int order){
        this.workoutId = workoutId;
        this.exerciseProfileId = exerciseProfileId;
        this.phase = order;
    }

    public Sets getOldSets() {
        return oldSets;
    }

    public void setOldSets(Sets oldSets) {
        this.oldSets = oldSets;
    }

    public Sets getNewSets() {
        return newSets;
    }

    public void setNewSets(Sets newSets) {
        this.newSets = newSets;
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
