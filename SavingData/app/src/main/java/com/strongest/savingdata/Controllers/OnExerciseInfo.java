package com.strongest.savingdata.Controllers;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.Database.Exercise.Beans;

public interface OnExerciseInfo {

    void transitionToExerciseInfo(PLObject.ExerciseProfile exerciseProfile);
}
