package com.strongest.savingdata.Controllers;

import android.support.v7.widget.RecyclerView;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.ViewHolders.ExerciseViewHolder;

public interface UiExerciseClickHandler {

    void onExerciseEdit(int position, PLObject.ExerciseProfile exerciseProfile);
    void onExerciseDetails(ExerciseViewHolder vh, PLObject.ExerciseProfile exerciseProfile);
    void onExerciseStartDrag(RecyclerView.ViewHolder vh);
    void onLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh);
    void onAddSuperset(RecyclerView.ViewHolder vh, PLObject.ExerciseProfile exerciseProfile);
    void onRemoveSuperset(PLObject.ExerciseProfile exerciseProfile);

}
