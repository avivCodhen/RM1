package com.strongest.savingdata.AModels.workoutModel;

import java.util.ArrayList;
import java.util.List;

public class WorkoutBro {

    public List<PLObject> exArray;
    public String workoutName;
    private String programUid;

    public WorkoutBro(){

    }

    public List<PLObject> getExArray() {
        return exArray;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public void setExArray(List<PLObject> exArray) {
        this.exArray = exArray;
    }

    public String getProgramUid() {
        return programUid;
    }

    public void setProgramUid(String programUid) {
        this.programUid = programUid;
    }
}
