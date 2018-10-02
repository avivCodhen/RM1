package com.strongest.savingdata.Controllers;

import com.strongest.savingdata.AModels.programModel.Program;
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

    interface program {
        void deleteProgram(Program p);

        void switchProgram(Program p);

        void shareProgram(Program p);

        void loadProgram(Program p);

        void loadSharedProgram(Program p);

        void seen(Program p);
    }


}
