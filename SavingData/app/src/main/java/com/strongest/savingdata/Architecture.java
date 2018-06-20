package com.strongest.savingdata;

import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;
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
