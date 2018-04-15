package com.strongest.savingdata.Adapters.WorkoutAdapter;

import android.support.v7.widget.RecyclerView;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;

/**
 * Created by Cohen on 2/9/2018.
 */

public interface OnExpandCollapseListener {

    /*void onExpand(MyExpandableAdapter.ExerciseViewHolder vh);

    void onCollapse(MyExpandableAdapter.ExerciseViewHolder vh);
*/
    void deTailExpand(RecyclerView.ViewHolder vh);

    void detailCollapse(int position);
}
