package com.strongest.savingdata.MyViews.WorkoutView;

import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;

import java.util.ArrayList;

/**
 * Created by Cohen on 1/6/2018.
 */

public interface OnUpdateLayoutStatsListener {

    ArrayList<PLObjects> updateLayoutStats(LayoutManager.UpdateComponents updateComponents);
}
