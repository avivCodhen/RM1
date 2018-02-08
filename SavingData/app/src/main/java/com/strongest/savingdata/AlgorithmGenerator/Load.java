package com.strongest.savingdata.AlgorithmGenerator;

import java.io.Serializable;

/**
 * Created by Cohen on 8/19/2017.
 */
public class Load implements Serializable{

    private int exerciseLevel;
    private int exerciseVolume;
    private int intensity;
    private int complexity;

    public Load(int exerciseLevel, int exerciseVolume, int intensity, int complexity) {
        this.exerciseLevel = exerciseLevel;
        this.exerciseVolume = exerciseVolume;
        this.intensity = intensity;
        this.complexity = complexity;
    }

    public int getExerciseVolume() {
        return exerciseVolume;
    }

    public int getExerciseLevel() {
        return exerciseLevel;
    }

    public int getIntensity() {
        return intensity;
    }

    public int getComplexity() {
        return complexity;
    }
}
