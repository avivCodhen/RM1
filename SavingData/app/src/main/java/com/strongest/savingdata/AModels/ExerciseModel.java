package com.strongest.savingdata.AModels;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModelValidator;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.SetsItemAdapter;

import java.util.ArrayList;

public class ExerciseModel {

    public static Workout exerciseToList(PLObject.ExerciseProfile ep) {
        int results = WorkoutsModelValidator.getWorkoutsValidator().validateExercise(ep);

        if (results == WorkoutsModelValidator.NULL_SET) {
            ep.setSets(new ArrayList<>());
        }

        if (results == WorkoutsModelValidator.NO_SET) {
            PLObject.SetsPLObject set = new SetsItemAdapter().insert();
            ep.getSets().add(set);
        }
        Workout w = new Workout();

        //add all of the supersets to the list
       /* for (ExerciseProfile superset : ep.getExerciseProfiles()) {
            w.exArray.add(superset);
        }
*/
        //add each set to the list
        for (PLObject.SetsPLObject s : ep.getSets()) {
            w.exArray.add(s);

           /* //add superset(s) for each set
            for (ExerciseProfile e : ep.getExerciseProfiles()) {
                w.exArray.add(e);
            }*/

            //add each child (if any)
            for (PLObject.SetsPLObject i : s.intraSets) {
                w.exArray.add(i);
            }


        }

        return w;


    }

    /**
     * this function expands the exercise into exercise and it's childs(supersets)
     * this is not to be confused with expanding the exercise into a list of sets
     * */
    public static ArrayList<PLObject> expandExercise(PLObject.ExerciseProfile ep) {
        ArrayList<PLObject> list = new ArrayList<>();
        for (PLObject.ExerciseProfile e : ep.getExerciseProfiles()) {
            list.add(e);
        }
        return list;
    }
}
