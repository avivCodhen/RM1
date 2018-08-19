package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import com.strongest.savingdata.AModels.workoutModel.PLObject;

public abstract class ItemAdapterFactory  {

    public  abstract WorkoutItemAdapter create(Class<? extends PLObject> plObject);
}
