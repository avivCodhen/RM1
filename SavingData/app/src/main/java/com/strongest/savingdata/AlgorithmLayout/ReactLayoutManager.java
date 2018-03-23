package com.strongest.savingdata.AlgorithmLayout;

import com.strongest.savingdata.MyViews.WorkoutView.WorkoutView;

/**
 * Created by Cohen on 3/16/2018.
 */

public class ReactLayoutManager {

    private final LayoutManager layoutManager;
    private final WorkoutView.WorkoutViewFragment workoutViewFragment;
    private PLObject.ExerciseProfile exerciseProfile;

    public static ReactLayoutManager newInstance(LayoutManager layoutManager,
                                                 WorkoutView.WorkoutViewFragment workoutViewFragment,
                                                 PLObject.ExerciseProfile exerciseProfile) {
        return new ReactLayoutManager(layoutManager, workoutViewFragment, exerciseProfile);
    }

    private ReactLayoutManager(LayoutManager layoutManager,
                               WorkoutView.WorkoutViewFragment workoutViewFragment, PLObject.ExerciseProfile exerciseProfile) {

        this.layoutManager = layoutManager;
        this.workoutViewFragment = workoutViewFragment;
        this.exerciseProfile = exerciseProfile;
    }
}
