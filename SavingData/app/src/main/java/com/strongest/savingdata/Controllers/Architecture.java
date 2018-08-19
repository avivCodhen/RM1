package com.strongest.savingdata.Controllers;

import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;

public interface Architecture {

    interface view {
        interface LongClickView {
            void onLongClickAction(LongClickMenuView longClickMenuView, WorkoutsModel.Actions action);
        }

        interface ProgramTools {
            void onProgramToolsAction(WorkoutsModel.Actions action);
        }

        void onWorkoutViewFragmentLongClick();
    }

    interface model {

       void removeItem(Workout w);
       void addNewItem(Workout w, WorkoutsModel.Actions a);
    }

}
