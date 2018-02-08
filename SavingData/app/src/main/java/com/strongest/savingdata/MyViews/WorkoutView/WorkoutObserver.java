package com.strongest.savingdata.MyViews.WorkoutView;

import com.strongest.savingdata.Database.Exercise.BeansHolder;

import java.util.ArrayList;

/**
 * Created by Cohen on 12/17/2017.
 */

public interface WorkoutObserver {
    void exerciseOnClick(ArrayList<BeansHolder> beansHolders, int position, boolean changed);
}
