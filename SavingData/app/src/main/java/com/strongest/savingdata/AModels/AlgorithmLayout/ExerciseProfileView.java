package com.strongest.savingdata.AModels.AlgorithmLayout;

import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;

import java.io.Serializable;

/**
 * Created by Cohen on 7/11/2017.
 */

@Deprecated
public class ExerciseProfileView implements Serializable {

    private Beans exercise;
    private Beans reps;
    private Beans method;
    private Beans superset;
    private int repsInt;
    private String ovr;
    private int muscle;
    private int position;
    private int exPosition;
    private int repPosition;
    private int metPosition;
    private ProgramTemplate programTemplate;

    private ExerciseSet ExerciseSet;

    private int exerciseId;
    private int repsId;
    private int methodId;

    public ExerciseProfileView(){
        this.exPosition = 0;
        this.repPosition = 0;
        this.metPosition = 0;
    }






    public int getRepsInt() {
        return repsInt;
    }

    public void setRepsInt(int repsInt) {
        this.repsInt = repsInt;
    }

    public String getOvr() {
        return ovr;
    }

    public void setOvr(String ovr) {
        this.ovr = ovr;
    }

    public int getMuscle() {
        return muscle;
    }

    public void setMuscle(int muscle) {
        this.muscle = muscle;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getExPosition() {
        return exPosition;
    }

    public void setExPosition(int exPosition) {
        this.exPosition = exPosition;
    }

    public int getRepPosition() {
        return repPosition;
    }

    public void setRepPosition(int repPosition) {
        this.repPosition = repPosition;
    }

    public int getMetPosition() {
        return metPosition;
    }

    public void setMetPosition(int metPosition) {
        this.metPosition = metPosition;
    }

    public ProgramTemplate getProgramTemplate() {
        return programTemplate;
    }

    public void setProgramTemplate(ProgramTemplate programTemplate) {
        this.programTemplate = programTemplate;
    }

    public Beans getExercise() {
        return exercise;
    }

    public Beans getReps() {
        return reps;
    }

    public void setReps(Beans reps) {
        this.reps = reps;
    }

    public Beans getMethod() {
        return method;
    }

    public void setMethod(Beans method) {
        this.method = method;
    }

    public Beans getSuperset() {
        return superset;
    }

    public void setSuperset(Beans superset) {
        this.superset = superset;
    }

    public void setExercise(Beans exercise) {
        this.exercise = exercise;
    }

    public int getMethodId() {
        return methodId;
    }

    public void setMethodId(int methodId) {
        this.methodId = methodId;
    }

    public int getRepsId() {
        return repsId;
    }

    public void setRepsId(int repsId) {
        this.repsId = repsId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public ExerciseSet getExerciseSet() {
        return ExerciseSet;
    }

    public void setExerciseSet(ExerciseSet exerciseSet) {
        this.ExerciseSet = exerciseSet;
    }

    public class Builder{

        public Builder(){

        }
    }
}
