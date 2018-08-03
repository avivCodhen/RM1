package com.strongest.savingdata.AModels;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModelValidator;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.SetsItemAdapter;
import com.strongest.savingdata.Controllers.CallBacks;

import java.util.ArrayList;

public class ExerciseModel {


    public static void exerciseToWorkout(PLObject.ExerciseProfile ep, CallBacks.OnFinish onFinish) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                int results = WorkoutsModelValidator.getWorkoutsValidator().validateExercise(ep);

                if (results == WorkoutsModelValidator.NULL_SET) {
                    ep.setSets(new ArrayList<>());
                }

                if (results == WorkoutsModelValidator.NO_SET) {
                    PLObject.SetsPLObject set = new SetsItemAdapter().insert();
                    ep.getSets().add(set);
                }
                Workout w = new Workout();


                //add each set to the list
                for (int i = 0; i < ep.getSets().size(); i++) {
                    PLObject.SetsPLObject s = ep.getSets().get(i);

                    //add all of the superSets to the list for the first time
                    if (s.superSets.size() == 0) {

                        for (PLObject.ExerciseProfile superset : ep.getExerciseProfiles()) {
                            if (i < superset.intraSets.size()) {
                                s.superSets.add(superset.intraSets.get(i));
                            }

                        }
                    }

                    w.exArray.add(s);

                }
                w.setParents(expandExerciseSupersets(ep));

                onFinish.onFinish(w);
            }
        }).start();


    }

    /**
     * this function expands the exercise into exercise and it's childs(superSets)
     * this is not to be confused with expanding the exercise into a list of sets
     */
    public static ArrayList<PLObject> expandExerciseSupersets(PLObject.ExerciseProfile ep) {
        ArrayList<PLObject> list = new ArrayList<>();
        for (PLObject.ExerciseProfile e : ep.getExerciseProfiles()) {
            list.add(e);
        }
        return list;
    }

    /**
     * this function injects a superset-set in a set(ahm)
     */

    public static boolean injectSupersetExercise(PLObject.ExerciseProfile parent, PLObject.SetsPLObject set) {
        if (hasSupersets(parent)) {
            for (PLObject.ExerciseProfile superset : parent.getExerciseProfiles()) {
                int setPositionInParent = getSetPosition(parent, set);
                set.intraSets.add(superset.intraSets.get(setPositionInParent));

            }

            return true;

        }
        return false;
    }

    public static int getSetPosition(PLObject.ExerciseProfile parent, PLObject.SetsPLObject set) {
        return parent.getSets().indexOf(set);

    }

    /**
     * injectSetSuperset injects a new set in the superset.
     * it gets the position of the correct set in the list
     * based on the position of the sets
     */
    public static void injectDuplicateSetToSuperset(PLObject.ExerciseProfile parent, PLObject.SetsPLObject duplicate) {

        int setPosition = getSetPosition(parent, duplicate);
        for (PLObject.ExerciseProfile superset : parent.getExerciseProfiles()) {
          //  superset.intraSets
        }

    }

    private static boolean hasSupersets(PLObject.ExerciseProfile parent) {
        return parent.getExerciseProfiles().size() > 0;
    }

}
