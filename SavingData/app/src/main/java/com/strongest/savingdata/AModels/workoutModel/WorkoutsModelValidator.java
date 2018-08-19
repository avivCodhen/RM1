package com.strongest.savingdata.AModels.workoutModel;

import java.util.ArrayList;

public class WorkoutsModelValidator {


    public static final int OK = 0, NULL = 1, LIST_EMPTY = 2;
    public static final int  NULL_SET = 3, NO_SET = 4;


    public static WorkoutsModelValidator getWorkoutsValidator(){
        return new WorkoutsModelValidator();
    }


    public int validateList(ArrayList<Workout> list){
        if (validateNull(list) == NULL){
            return NULL;
        }

        return validateListEmpty(list);
    }

    private int validateNull(Object obj){
        if (obj != null)
            return OK;

        else return NULL;

    }

    private int validateListEmpty(ArrayList<Workout> list){
        if (list.size() == 0)
            return LIST_EMPTY;
        else return OK;
    }

    public int validateExercise(PLObject.ExerciseProfile ep){
        if(validateNull(ep) == NULL){
            return NULL;
        }
        return validateExerciseSetNull(ep);
    }

    private int validateExerciseSetNull(PLObject.ExerciseProfile ep){
        if (ep.getSets() == null)
            return NULL_SET;
        else return validateExerciseSetNotEmpty(ep);
    }

    private int validateExerciseSetNotEmpty(PLObject.ExerciseProfile ep){
        if(ep.getSets().size() == 0)
            return NO_SET;
        else
            return OK;
    }

}
