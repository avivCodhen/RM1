package com.strongest.savingdata.Database.Exercise;

import java.io.Serializable;

/**
 * Created by Cohen on 10/18/2017.
 */

public class ExerciseSet implements Serializable {

    private Beans exercise;
    private String rep = "0";
    private String method;
    private Beans superset;
    private Beans sets;
    private String rest = "00:00";
    private double weight = 0;
    private boolean isLoaded;
    private boolean hasmethod;
    private String tag;
    private boolean customWeight;

    private boolean intraSet;
    //progressor stuff
    private int oldPosition = -1;
    private int newPosition = -1;

    public ExerciseSet() {
    }

    public ExerciseSet(ExerciseSet clone){
        this.exercise = clone.exercise;
        this.rep = clone.rep;
        this.rest = clone.rest;
        this.weight = clone.weight;
        this.tag = clone.tag;
    }

    public void setHasmethod(boolean hasmethod) {
        this.hasmethod = hasmethod;
    }

    public boolean isHasmethod() {
        return hasmethod;
    }

    public Beans getExercise() {
        return exercise;
    }

    public void setExercise(Beans exercise) {
        this.exercise = exercise;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Beans getSuperset() {
        return superset;
    }

    public void setSuperset(Beans superset) {
        this.superset = superset;
    }

    public Beans getSets() {
        return sets;
    }

    public void setSets(Beans sets) {
        this.sets = sets;
    }

    public String getRest() {
        return rest;
    }

    public void setRest(String rest) {
        this.rest = rest;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public int getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(int oldPosition) {
        this.oldPosition = oldPosition;
    }

    public int getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(int newPosition) {
        this.newPosition = newPosition;
    }

    public boolean isIntraSet() {
        return intraSet;
    }

    public void setIntraSet(boolean intraSet) {
        this.intraSet = intraSet;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isCustomWeight() {
        return customWeight;
    }

    public void setCustomWeight(boolean customWeight) {
        this.customWeight = customWeight;
    }
}
