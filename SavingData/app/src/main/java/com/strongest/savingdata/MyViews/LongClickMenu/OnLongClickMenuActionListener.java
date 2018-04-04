package com.strongest.savingdata.MyViews.LongClickMenu;

import android.support.v7.widget.RecyclerView;

import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;

public interface OnLongClickMenuActionListener {

    void onAction(LongClickMenuView.Action action, RecyclerView.ViewHolder vh, WorkoutLayoutTypes type);
}
