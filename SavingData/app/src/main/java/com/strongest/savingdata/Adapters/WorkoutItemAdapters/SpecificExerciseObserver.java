package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import com.strongest.savingdata.AModels.workoutModel.PLObject;

public interface SpecificExerciseObserver {

    void onChange(PLObject.ExerciseProfile exerciseProfile, int position);
}
