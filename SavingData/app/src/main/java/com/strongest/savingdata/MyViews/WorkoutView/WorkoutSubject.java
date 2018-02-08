package com.strongest.savingdata.MyViews.WorkoutView;

import com.strongest.savingdata.Database.Exercise.BeansHolder;
import com.strongest.savingdata.MyViews.WorkoutView.WorkoutView;

import java.util.ArrayList;

/**
 * Created by Cohen on 12/17/2017.
 */

public interface WorkoutSubject {
    void Attach(WorkoutView o);
    void Dettach(WorkoutView o);
    void Notify(ArrayList<BeansHolder> beanHolders, int position, boolean changed);
}
