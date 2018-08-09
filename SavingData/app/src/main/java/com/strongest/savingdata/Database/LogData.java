package com.strongest.savingdata.Database;

import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Adapters.LogDataAdapter;

public class LogData {

    public String full;
    public String date;
    public String time;

    public LogData(String full, String date, String time){
        this.full = full;
        this.date = date;
        this.time = time;
    }
}
