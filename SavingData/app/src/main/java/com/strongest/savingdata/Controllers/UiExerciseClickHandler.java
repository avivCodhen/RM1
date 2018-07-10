package com.strongest.savingdata.Controllers;

import android.support.v7.widget.RecyclerView;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.ViewHolders.ExerciseViewHolder;

public interface UiExerciseClickHandler {

    void onExerciseEdit(RecyclerView.ViewHolder vh,PLObject plObject, int position);
    void onExerciseDetails(ExerciseViewHolder vh, PLObject.ExerciseProfile exerciseProfile);
    void onExerciseStartDrag(RecyclerView.ViewHolder vh);
    void onLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh);
    void onAddSuperset(PLObject.ExerciseProfile exerciseProfile, int position);

}
