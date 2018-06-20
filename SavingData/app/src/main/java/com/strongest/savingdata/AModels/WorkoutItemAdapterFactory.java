package com.strongest.savingdata.AModels;

import android.content.Intent;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.Activities.MainActivity;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.DividerItemAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.ExerciseItemAdapter;

import java.util.Map;

public class WorkoutItemAdapterFactory extends ItemAdapterFactory{

    public static WorkoutItemAdapterFactory getFactory(){
        return new WorkoutItemAdapterFactory();
    }

    public  WorkoutItemAdapter create(Class<? extends PLObject> plObject){

        if(plObject.isAssignableFrom(PLObject.ExerciseProfile.class)) {
            return new ExerciseItemAdapter();
        }

        if(plObject.isAssignableFrom(PLObject.BodyText.class)){
            return new DividerItemAdapter();
        }


        throw new IllegalArgumentException("Class is not found in the factory");
    }
}
