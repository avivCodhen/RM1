package com.strongest.savingdata.AlgorithmStats;

/**
 * Created by Cohen on 10/19/2017.
 */

public class StatsHolder {


    private Calculator.ProgressStatsMinMaxBean minMaxBean;
    private ExerciseStats exerciseStats;
    private ProgressStats progressStats;
    private int damage;
    private int mechanical;
    private int metabolic;

    public StatsHolder(){

    }

    public int getMetabolic() {
        return metabolic;
    }

    public void setMetabolic(int metabolic) {
        this.metabolic = metabolic;
    }

    public int getMechanical() {
        return mechanical;
    }

    public void setMechanical(int mechanical) {
        this.mechanical = mechanical;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public ProgressStats getProgressStats() {
        return progressStats;
    }

    public void setProgressStats(ProgressStats progressStats) {
        this.progressStats = progressStats;
    }

    public Calculator.ProgressStatsMinMaxBean getMinMaxBean() {
        return minMaxBean;
    }

    public void setMinMaxBean(Calculator.ProgressStatsMinMaxBean minMaxBean) {
        this.minMaxBean = minMaxBean;
    }

    public ExerciseStats getExerciseStats() {
        return exerciseStats;
    }

    public void setExerciseStats(ExerciseStats exerciseStats) {
        this.exerciseStats = exerciseStats;
    }
}
