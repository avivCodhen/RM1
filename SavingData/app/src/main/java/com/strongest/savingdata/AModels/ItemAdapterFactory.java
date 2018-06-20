package com.strongest.savingdata.AModels;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;

public abstract class ItemAdapterFactory  {

    public  abstract WorkoutItemAdapter create(Class<? extends PLObject> plObject);
}
