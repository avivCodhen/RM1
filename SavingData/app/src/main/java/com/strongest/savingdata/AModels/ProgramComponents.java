package com.strongest.savingdata.AModels;

import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.BaseWorkout.Program;

public class ProgramComponents {

    private Program program;
    private WorkoutsModel workoutsModel;

    public ProgramComponents(Program program, WorkoutsModel workoutsModel){

        this.program = program;
        this.workoutsModel = workoutsModel;
    }

    public Program getProgram() {
        return program;
    }

    public WorkoutsModel getWorkoutsModel() {
        return workoutsModel;
    }
}
