package com.strongest.savingdata.AlgorithmStats;

import java.io.Serializable;

/**
 * Created by Cohen on 10/8/2017.
 */

public abstract class StatsConstants implements Serializable{

    static final int METHOD_METABOLIC_STRESS = 2;
    static final int METHOD_MECHANICAL_STRESS = 2;
    static final int METHOD_DAMAGE = 0;

    static final int B_DAMAGE = 10;
    static final int S_DAMAGE = 20;
    static final int C_DAMAGE = 15;

    static final int B_MECHANICAL_STRESS = 0;
    static final int B_METABOLIC_STRESS = 0;

    static final int S_MECHANICAL_STRESS = 2;
    static final int S_METABOLIC_STRESS = 2;

    static final int SPACE_MODIFICATION = 5;
    static final int C_MECHANICAL_STRESS = 6;
    static final int C_METABOLIC_STRESS = 6;

    static int MIN_STRESS_POINTS = 2;
    static int MAX_METABOLIC_POINTS = 10;
    static int MAX_MECHANICAL_POINTS = 10;
    static int MAX_MEDIUM_STRESS = 5;



}
