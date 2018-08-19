package com.strongest.savingdata.MyViews.WorkoutView;

import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AModels.workoutModel.PLObject;

import java.util.ArrayList;

/**
 * Created by Cohen on 1/6/2018.
 */

public interface OnUpdateLayoutStatsListener {

    ArrayList<PLObject> updateLayout(WorkoutsModel.UpdateComponents updateComponents);
}
