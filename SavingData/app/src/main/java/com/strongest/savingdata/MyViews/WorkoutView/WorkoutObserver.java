package com.strongest.savingdata.MyViews.WorkoutView;

import com.strongest.savingdata.Database.Exercise.ExerciseSet;

import java.util.ArrayList;

/**
 * Created by Cohen on 12/17/2017.
 */

public interface WorkoutObserver {
    void exerciseOnClick(ArrayList<ExerciseSet> sets, int position, boolean changed);
}
