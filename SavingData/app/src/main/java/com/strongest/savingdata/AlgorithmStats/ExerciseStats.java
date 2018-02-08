package com.strongest.savingdata.AlgorithmStats;

/**
 * Created by Cohen on 11/29/2017.
 */

public class ExerciseStats {

    private int numOfComplex;
    private int numOfExercises;
    private int numOfMethod;

    public ExerciseStats(){

    }

    public int getNumOfMethod() {
        return numOfMethod;
    }

    public void setNumOfMethod(int numOfMethod) {
        this.numOfMethod += numOfMethod;
    }

    public int getNumOfExercises() {
        return numOfExercises;
    }

    public void setNumOfExercises(int numOfExercises) {
        this.numOfExercises += numOfExercises;
    }

    public int getNumOfComplex() {
        return numOfComplex;
    }

    public void setNumOfComplex(int numOfComplex) {
        this.numOfComplex += numOfComplex;
    }
}
