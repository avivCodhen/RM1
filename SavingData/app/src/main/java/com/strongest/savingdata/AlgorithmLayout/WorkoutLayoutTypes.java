package com.strongest.savingdata.AlgorithmLayout;

/**
 * Created by Cohen on 10/18/2017.
 */

public enum WorkoutLayoutTypes {

    WorkoutView,

    BodyView,

    ExerciseView,

    BeansHolderPLObject,

    ProgressPLObject,

    ExerciseViewLeftMargin,

    ExerciseViewRightMargin,

    BeansHolderLeftMargin,

    BeansHolderRightMargin,

    AddExercise;

    public static WorkoutLayoutTypes getEnum(int value) {
        return values()[value];
    }

}
