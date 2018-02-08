package com.strongest.savingdata.Unused;

/**
 * Created by Cohen on 11/14/2017.
 */

public class P {
    private static final P ourInstance = new P();

    public static P getInstance() {
        return ourInstance;
    }

    private P() {
    }
}
