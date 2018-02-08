package com.strongest.savingdata.AlgorithmStats;

/**
 * Created by Cohen on 10/8/2017.
 */

public class ProgressStats extends StatsConstants{

    private int minDamage;
    private int maxDamage;

    private int minMetabolic;
    private int minMechanical;

    private int maxMetabolic;
    private int maxMechanical;

    public ProgressStats(int minDamage, int maxDamage, int minMetabolic, int minMechanical, int maxMetabolic, int maxMechanical) {
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.minMetabolic = minMetabolic;
        this.minMechanical = minMechanical;
        this.maxMetabolic = maxMetabolic;
        this.maxMechanical = maxMechanical;
    }


    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public int getMinMetabolic() {
        return minMetabolic;
    }

    public int getMinMechanical() {
        return minMechanical;
    }

    public int getMaxMetabolic() {
        return maxMetabolic;
    }

    public int getMaxMechanical() {
        return maxMechanical;
    }
}
