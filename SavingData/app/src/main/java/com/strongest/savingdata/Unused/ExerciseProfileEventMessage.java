package com.strongest.savingdata.Unused;

import com.strongest.savingdata.AlgorithmLayout.PLObjects.ExerciseProfile;

/**
 * Created by Cohen on 11/28/2017.
 */

public class ExerciseProfileEventMessage {

    private ExerciseProfile exerciseProfile;
    private int position;

    public ExerciseProfileEventMessage(ExerciseProfile exerciseProfile, int position){

        this.exerciseProfile = exerciseProfile;
        this.position = position;
    }

    public ExerciseProfile getExerciseProfile() {
        return exerciseProfile;
    }

    public void setExerciseProfile(ExerciseProfile exerciseProfile) {
        this.exerciseProfile = exerciseProfile;
    }

    public int getPosition() {
        return position;
    }
}
