package com.strongest.savingdata.MyViews.WorkoutView;

import android.support.v7.widget.RecyclerView;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;

/**
 * Created by Cohen on 3/20/2018.
 */

public interface OnWorkoutViewInterfaceClicksListener {

    void onExerciseClick(MyExpandableAdapter.ExerciseViewHolder vh);

    void onLongClick(RecyclerView.ViewHolder vh, boolean delete);

    void onLongSupersetClick(RecyclerView.ViewHolder vh, boolean delete);
    void onSetsDoubleClick(RecyclerView.ViewHolder vh, int childPosition);
    void onSetsLongClick(RecyclerView.ViewHolder vh, int childPosition, boolean delete);

    void onMoreClick(RecyclerView.ViewHolder vh);

    void onSettingsClick(RecyclerView.ViewHolder vh);

    void onSwapExercise(int fromPosition, int toPosition);

    void onBodyViewLongClick(RecyclerView.ViewHolder vh, boolean delete);
}
