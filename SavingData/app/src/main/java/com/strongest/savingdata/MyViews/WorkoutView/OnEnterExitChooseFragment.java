package com.strongest.savingdata.MyViews.WorkoutView;

import android.support.v7.widget.RecyclerView;

import com.strongest.savingdata.AlgorithmLayout.PLObjects;

/**
 * Created by Cohen on 3/8/2018.
 */

public interface OnEnterExitChooseFragment {

    void onEnterChooseFragment(RecyclerView.ViewHolder vh);

    void onExitChooseFragment(RecyclerView.ViewHolder vh, boolean isCollapsed);

}
