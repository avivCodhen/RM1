package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import com.strongest.savingdata.AModels.workoutModel.PLObject;

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
