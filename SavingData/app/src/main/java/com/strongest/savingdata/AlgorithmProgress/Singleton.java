package com.strongest.savingdata.AlgorithmProgress;

/**
 * Created by Cohen on 10/14/2017.
 */

public class Singleton {
    private static final Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }
}
