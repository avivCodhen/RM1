package com.strongest.savingdata.MyViews.WorkoutView;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Cohen on 3/8/2018.
 */

public interface OnEnterExitChooseFragment {

    void onEnterChooseFragment(int position);

    void onExitChooseFragment(int position, int oldPosition);

}
