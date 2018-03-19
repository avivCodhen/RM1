package com.strongest.savingdata.Database.Progress;

import com.strongest.savingdata.Database.Exercise.ExerciseSet;

/**
 * Created by Cohen on 12/27/2017.
 */

public class ProgressModelNode {

    private ExerciseSet oldExerciseSet;
    private ExerciseSet newExerciseSet;
    private int workoutId;
    private int phase;
    private int exerciseProfileId;


    public ProgressModelNode(ExerciseSet oldExerciseSet, ExerciseSet newExerciseSet, int workoutId, int exerciseProfileId, int order){
        this.workoutId = workoutId;
        this.exerciseProfileId = exerciseProfileId;
        this.phase = order;
    }

    public ExerciseSet getOldExerciseSet() {
        return oldExerciseSet;
    }

    public void setOldExerciseSet(ExerciseSet oldExerciseSet) {
        this.oldExerciseSet = oldExerciseSet;
    }

    public ExerciseSet getNewExerciseSet() {
        return newExerciseSet;
    }

    public void setNewExerciseSet(ExerciseSet newExerciseSet) {
        this.newExerciseSet = newExerciseSet;
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
