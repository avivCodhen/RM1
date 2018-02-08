package com.strongest.savingdata.AlgorithmLayout;

/**
 * Created by Cohen on 10/18/2017.
 */

public enum WorkoutLayoutTypes {

    WorkoutView(0),

    BodyView(1),

    ExerciseView(2),

    BeansHolderPLObject(3),

    ProgressPLObject(4);

    private int value;
    private WorkoutLayoutTypes(int value){
        this.value = value;
    }

    public static WorkoutLayoutTypes getEnum(int value) {
        return values()[value];
    }

    public int getValue() {
        return value;
    }
}
