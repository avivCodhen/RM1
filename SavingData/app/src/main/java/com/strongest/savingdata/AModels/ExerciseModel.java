package com.strongest.savingdata.AModels;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
import com.strongest.savingdata.AModels.workoutModel.WorkoutsModelValidator;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.SetsItemAdapter;
import com.strongest.savingdata.Controllers.CallBacks;

import java.util.ArrayList;

public class ExerciseModel {


    public static void exerciseToWorkout(SetsItemAdapter setsItemAdapter, PLObject.ExerciseProfile ep, CallBacks.OnFinish onFinish) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                int results = WorkoutsModelValidator.getWorkoutsValidator().validateExercise(ep);

                if (results == WorkoutsModelValidator.NULL_SET) {
                    ep.setSets(new ArrayList<>());
                }

                if (results == WorkoutsModelValidator.NO_SET) {
                    setsItemAdapter.insert();
                }
                Workout w = new Workout();


                //add each set to the list
                for (int i = 0; i < ep.getSets().size(); i++) {
                    PLObject.SetsPLObject s = ep.getSets().get(i);

                    //each set has to get it's supersets from each superset exercise
                    s.superSets.clear();
                    for (PLObject.ExerciseProfile superset : ep.exerciseProfiles) {
                        s.superSets.add(superset.intraSets.get(i));
                        if (i < superset.intraSets.size()) {
                        }
                    }

                    w.exArray.add(s);

                }
             //   w.setParents(expandExerciseSupersets(ep));

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
        for (PLObject.ExerciseProfile e : ep.exerciseProfiles) {
            list.add(e);
        }
        return list;
    }

/*


    public static int getSetPosition(PLObject.ExerciseProfile parent, PLObject.SetsPLObject set) {
        return parent.getSets().indexOf(set);

    }

    private static boolean hasSupersets(PLObject.ExerciseProfile parent) {
        return parent.getExerciseProfiles().size() > 0;
    }
*/


    /**
     * injectSetSuperset injects a new set in the superset.
     * it gets the new position  of the set in the list
     * based on the position of the sets
     */
    public static void injectDuplicateSetToSuperset(int positionInList,
                                                    PLObject.ExerciseProfile parent,
                                                    ArrayList<PLObject.SetsPLObject> supersetsSets) {

        for (int i = 0; i < parent.exerciseProfiles.size(); i++) {
            PLObject.ExerciseProfile superset = parent.exerciseProfiles.get(i);
            superset.intraSets.add(positionInList, supersetsSets.get(i));
        }

    }


    /**
     * this function injects a superset-set in a set(ahm)
     */

    public static void injectSupersetExercise(int positionInList,
                                              PLObject.ExerciseProfile parent,
                                              PLObject.SetsPLObject setsPLObject,
                                              SetsItemAdapter setsItemAdapter) {

        int numOfSupersets = parent.exerciseProfiles.size();
        for (int i = 0; i < numOfSupersets; i++) {
            PLObject.ExerciseProfile superset = parent.exerciseProfiles.get(i);
            PLObject.SetsPLObject intraSet =new PLObject.SetsPLObject();
            intraSet.innerType = WorkoutLayoutTypes.SuperSetIntraSet;
            intraSet.type = WorkoutLayoutTypes.SuperSetIntraSet;
            superset.intraSets.add(positionInList, intraSet);
            setsPLObject.superSets.add(intraSet);
        }
    }

}
