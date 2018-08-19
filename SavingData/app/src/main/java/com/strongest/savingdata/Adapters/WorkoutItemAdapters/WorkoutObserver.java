package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;

public interface WorkoutObserver {

    void onChange(WorkoutsModel.ListModifier listModifier);
}
