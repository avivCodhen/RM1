package com.strongest.savingdata.BaseWorkout;

import android.arch.persistence.room.Entity;

/**
 * Created by Cohen on 10/18/2017.
 */
@Entity
public class Program {

    public String programName;
    public final String time;
    public final String programDate;
    public final String dbName;
    //private boolean current;


    public Program(String programName, String time,  String programDate, String dbName) {
        this.programName = programName;
        this.time = time;
        this.dbName = dbName;

        this.programDate = programDate;
    }

    public String getDbName() {
        return dbName;
    }

    /*public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }*/
}
