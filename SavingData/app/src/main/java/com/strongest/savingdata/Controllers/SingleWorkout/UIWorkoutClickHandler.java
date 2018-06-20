package com.strongest.savingdata.Controllers.SingleWorkout;

import android.support.v7.widget.RecyclerView;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;

public interface UIWorkoutClickHandler {

    void onExerciseEdit(RecyclerView.ViewHolder vh,PLObject plObject, int position);
    void onExerciseDetails(PLObject.ExerciseProfile exerciseProfile);
    void onExerciseStartDrag(RecyclerView.ViewHolder vh);
    void onLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh);

}
