package com.strongest.savingdata.AlgorithmGenerator;

/**
 * Created by Cohen on 8/28/2017.
 */

public class BParams {

    private String name;
    private String value;
    //private int value;
    public BParams(){

    }


    public BParams(String name, String stringValue){
        this.name = name;
        this.value = stringValue;

    }

   /* public int getValue() {
        return value;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
