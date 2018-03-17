package com.strongest.savingdata.BaseWorkout;

import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.Database.Managers.DataManager;

/**
 * Created by Cohen on 10/18/2017.
 */

public class Program {

    public String programName;
    public String time;
    public String programDate;
    private String dbName;

    private boolean current;


    public Program(String programName, String time,  String programDate, String dbName) {
        this.programName = programName;
        this.time = time;
        this.dbName = dbName;

        this.programDate = programDate;
    }

    public String getDbName() {
        return dbName;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
