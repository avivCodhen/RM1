package com.strongest.savingdata.Adapters.WorkoutAdapter;

import android.support.v7.widget.RecyclerView;

import com.strongest.savingdata.AlgorithmLayout.PLObject;

/**
 * Created by Cohen on 2/11/2018.
 */

public interface OnExerciseProfileEditClick {

    void onEditExerciseClick(boolean enter, RecyclerView.ViewHolder vh, int position, PLObject plObjecs);
}
