package com.strongest.savingdata.Adapters.WorkoutAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;

/**
 * Created by Cohen on 2/11/2018.
 */

public interface OnExerciseProfileEditClick {

    void onEditExerciseClick(boolean enter, RecyclerView.ViewHolder vh, int position, PLObjects.ExerciseProfile ep);
}
