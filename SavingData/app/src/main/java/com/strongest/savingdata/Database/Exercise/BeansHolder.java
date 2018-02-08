package com.strongest.savingdata.Database.Exercise;

import com.strongest.savingdata.AlgorithmLayout.Ben;

import java.io.Serializable;

/**
 * Created by Cohen on 10/18/2017.
 */

public class BeansHolder implements Serializable, Ben {

    private Beans exercise;
    private Beans rep;
    private Beans method;
    private Beans superset;
    private Beans sets;
    private Beans rest;
    private double weight;
    private boolean isLoaded;
    private boolean hasmethod;

    //progressor stuff
    private int oldPosition = -1;
    private int newPosition = -1;

    public BeansHolder() {

    }

    public static boolean compareBeansHolders(BeansHolder one, BeansHolder two) {
        if(one == null){
            return true;
        }
        if (one.getExercise().getId() != two.getExercise().getId()) {
            return true;
        }
        if (one.getRep().getId() != two.getRep().getId()) {
            return true;
        }
        if (one.getMethod() != two.getMethod() && one.getMethod().getId() != two.getMethod().getId()) {
            return true;
        }
        if (one.getRest().getId() != two.getRest().getId()) {
            return true;
        }
        if (one.getSets() != two.getSets()) {
            return true;
        }
        if (one.getWeight() != two.getWeight()) {
            return true;
        }
        return false;
    }

   /* public static BeansHolder defaultBeansHolder(){
        BeansHolder beansHolder = new BeansHolder();
        beansHolder.setExercise(new Beans());
        beansHolder.setRep(new Beans());
        beansHolder.setMethod(new Beans());
        beansHolder.setSuperset(new Beans());
        beansHolder.setSets(3);
        return beansHolder;
    }*/

    public boolean isLoadedWithExerciseAndReps() {
        return rep.isLoaded() == true && exercise.isLoaded() == true;
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

    public Beans getRep() {
        return rep;
    }

    public void setRep(Beans rep) {
        this.rep = rep;
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

    public Beans getSets() {
        return sets;
    }

    public void setSets(Beans sets) {
        this.sets = sets;
    }

    public Beans getRest() {
        return rest;
    }

    public void setRest(Beans rest) {
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
}
