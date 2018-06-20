package com.strongest.savingdata.AModels.AlgorithmLayout;

import com.strongest.savingdata.Fragments.WorkoutViewFragment;

/**
 * Created by Cohen on 3/16/2018.
 */

public class ReactLayoutManager {

    private final WorkoutsModel workoutsModel;
    private final WorkoutViewFragment workoutViewFragment;
    private PLObject.ExerciseProfile exerciseProfile;

    public static ReactLayoutManager newInstance(WorkoutsModel workoutsModel,
                                                 WorkoutViewFragment workoutViewFragment,
                                                 PLObject.ExerciseProfile exerciseProfile) {
        return new ReactLayoutManager(workoutsModel, workoutViewFragment, exerciseProfile);
    }

    private ReactLayoutManager(WorkoutsModel workoutsModel,
                               WorkoutViewFragment workoutViewFragment, PLObject.ExerciseProfile exerciseProfile) {

        this.workoutsModel = workoutsModel;
        this.workoutViewFragment = workoutViewFragment;
        this.exerciseProfile = exerciseProfile;
    }
}
