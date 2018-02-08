package com.strongest.savingdata.AlgorithmStats;

/**
 * Created by Cohen on 10/20/2017.
 */

public class SParams {

    private String routine;
    private int level;

    public SParams(String routine, int level) {


        this.routine = routine;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public String getRoutine() {
        return routine;
    }
}
