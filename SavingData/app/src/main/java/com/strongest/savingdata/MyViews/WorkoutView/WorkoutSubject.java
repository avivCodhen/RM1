package com.strongest.savingdata.MyViews.WorkoutView;

import com.strongest.savingdata.Database.Exercise.ExerciseSet;

import java.util.ArrayList;

/**
 * Created by Cohen on 12/17/2017.
 */

public interface WorkoutSubject {
    void Attach(WorkoutView o);
    void Dettach(WorkoutView o);
    void Notify(ArrayList<ExerciseSet> beanHolders, int position, boolean changed);
}
