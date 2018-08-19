package com.strongest.savingdata.MyViews.WorkoutView;

import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;

/**
 * Created by Cohen on 2/17/2018.
 */

public interface OnProgramToolsActionListener {

    void onProgramToolsAction(String command, WorkoutsModel.UpdateComponents updateComponents);
}
