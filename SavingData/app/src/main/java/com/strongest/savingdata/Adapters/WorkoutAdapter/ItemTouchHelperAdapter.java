package com.strongest.savingdata.Adapters.WorkoutAdapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Cohen on 10/27/2017.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(RecyclerView.ViewHolder fromVh, RecyclerView.ViewHolder toVh);

    void onItemDismiss(int position);
}