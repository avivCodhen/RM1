package com.strongest.savingdata.MyViews.WorkoutView;

import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;

import java.util.ArrayList;

/**
 * Created by Cohen on 1/6/2018.
 */

public interface OnUpdateLayoutStatsListener {

    ArrayList<PLObject> updateLayout(WorkoutsModel.UpdateComponents updateComponents);
}
