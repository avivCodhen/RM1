package com.strongest.savingdata.AlgorithmProgress;

import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.PLObjects.ExerciseProfile;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;

import java.util.ArrayList;

/**
 * Created by Cohen on 10/14/2017.
 */

public class WeightProgressor {


    private DataManager dataManager;
    private ArrayList<ArrayList<PLObjects>> layout;
    private static int counter;

    public WeightProgressor(DataManager dataManager,  ArrayList<ArrayList<PLObjects>> layout) {
        this.dataManager = dataManager;

        this.layout = layout;
    }

   /* public void addWeightToExercise(int workoutPosition) {
        for (int i = 0; i < layout.get(workoutPosition).size(); i++) {
            if (layout.get(workoutPosition).get(i).getType() == WorkoutLayoutTypes.ExerciseView) {
              ExerciseProfile exerciseProfile = (ExerciseProfile) layout.get(workoutPosition).get(i);
                exerciseProfile.getBeansHolder().setWeight(2.5);
                dataManager.getProgramDataManager().updateWeight(2.5, exerciseProfile.getBeansHolder().getExercise().getId());
            }
        }
    }*/


}
