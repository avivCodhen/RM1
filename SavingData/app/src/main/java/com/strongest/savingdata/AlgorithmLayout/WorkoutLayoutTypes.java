package com.strongest.savingdata.AlgorithmLayout;

/**
 * Created by Cohen on 10/18/2017.
 */

public enum WorkoutLayoutTypes {

    WorkoutView,

    BodyView,

    ExerciseProfile,

    SetsPLObject,

    ProgressPLObject,


    IntraSet,

    IntraExerciseProfile,

    SuperSetIntraSet,

    IntraSetNormal,

    AddExercise,

    Method,

    More;

    public static WorkoutLayoutTypes getEnum(int value) {
        return values()[value];
    }

}
