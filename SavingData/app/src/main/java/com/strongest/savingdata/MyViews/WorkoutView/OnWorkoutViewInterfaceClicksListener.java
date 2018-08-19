package com.strongest.savingdata.MyViews.WorkoutView;

import android.support.v7.widget.RecyclerView;

import com.strongest.savingdata.AModels.workoutModel.PLObject;

/**
 * Created by Cohen on 3/20/2018.
 */

public interface OnWorkoutViewInterfaceClicksListener {

    void saveProgram();

    void notifyExerciseChanged(int position);


    void onLongClick(RecyclerView.ViewHolder vh, boolean delete);

    void onLongSupersetClick(int position, boolean delete);

    void onSetsDoubleClick(RecyclerView.ViewHolder vh);

    void onExerciseDoubleClick(PLObject.ExerciseProfile ep, int position);

    void onSetsLongClick(RecyclerView.ViewHolder vh, int childPosition, boolean delete);

    void onMoreClick(RecyclerView.ViewHolder vh);

    void onSettingsClick(RecyclerView.ViewHolder vh);

    void onSwapExercise(int fromPosition, int toPosition);

    void onBodyViewLongClick(RecyclerView.ViewHolder vh, boolean delete);



    void collapseExercise(int adapterPosition);

    void expandExercise(int adapterPosition);

    void onScrollPosition(int position, boolean enableScroll, boolean lastVisible);

    void onLongClickMenuAddSuperset(RecyclerView.ViewHolder vh);

    void onLongClickHideMenu();

    void onAddNormalIntraSet(RecyclerView.ViewHolder vh);

    void deleteItem(int position, boolean delete);

    void deleteExercise(int position);
}
