package com.strongest.savingdata.Adapters.WorkoutAdapter;

/**
 * Created by Cohen on 10/27/2017.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}