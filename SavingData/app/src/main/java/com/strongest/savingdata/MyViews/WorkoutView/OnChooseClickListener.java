package com.strongest.savingdata.MyViews.WorkoutView;

import android.view.View;

import com.strongest.savingdata.AlgorithmLayout.PLObjects;

/**
 * Created by Cohen on 1/10/2018.
 */

public interface OnChooseClickListener {

    void click(View v, PLObjects.ExerciseProfile exerciseProfile, String transitionName);
}
