package com.strongest.savingdata.MyViews.WorkoutView;

import android.support.v7.widget.RecyclerView;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;

/**
 * Created by Cohen on 3/20/2018.
 */

public interface OnWorkoutViewInterfaceClicksListener {

    void duplicateExercise(RecyclerView.ViewHolder vh);

    void duplicateItem(RecyclerView.ViewHolder vh, WorkoutLayoutTypes type);

    void notifyExerciseChanged(int position);

    void onExerciseClick(MyExpandableAdapter.ExerciseViewHolder vh);

    void onLongClick(RecyclerView.ViewHolder vh, boolean delete);

    void onLongSupersetClick(int position, boolean delete);

    void onSetsDoubleClick(RecyclerView.ViewHolder vh);

    void onExerciseDoubleClick(PLObject.ExerciseProfile ep, int position);

    void onSetsLongClick(RecyclerView.ViewHolder vh, int childPosition, boolean delete);

    void onMoreClick(RecyclerView.ViewHolder vh);

    void onSettingsClick(RecyclerView.ViewHolder vh);

    void onSwapExercise(int fromPosition, int toPosition);

    void onBodyViewLongClick(RecyclerView.ViewHolder vh, boolean delete);

    void collapseExercise(MyExpandableAdapter.ExerciseViewHolder vh);

    void expandExercise(MyExpandableAdapter.ExerciseViewHolder vh);

    void collapseExercise(int adapterPosition);

    void expandExercise(int adapterPosition);

    void onScrollPosition(int position, boolean enableScroll, boolean lastVisible);

    void onLongClickMenuAddSuperset(RecyclerView.ViewHolder vh);

    void onLongClickHideMenu();

    void onAddNormalIntraSet(RecyclerView.ViewHolder vh);

    void deleteItem(int position, boolean delete);

    void deleteExercise(int position);
}
