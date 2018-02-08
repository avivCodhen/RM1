package com.strongest.savingdata.AlgorithmGenerator;

import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;

import java.util.Random;

/**
 * Created by Cohen on 11/9/2017.
 */

public class RestTemplate {

    private final int LO = 0, ME = 1, HIG = 2;
    private final String LOW = "0", MED = "1", HIGH = "2";
    private Load load;
    private Random r;
    private BParams[] aParams;
    private BParams params;

    private int muscle;
    private BParams[] lowIntensity, medIntensity, highIntensity;
    private BParams[] lowMedIntensity, medHighIntensity;
    private final String TYPE = DBExercisesHelper.TYPE;


    public RestTemplate( Load load) {
        this.load = load;

        lowIntensity = new BParams[]{p(TYPE, LOW)};
        medIntensity = new BParams[]{p(TYPE, MED)};
        highIntensity = new BParams[]{p(TYPE, HIGH)};

    }

    private BParams p(String name, String value) {
        params = new BParams();
        r = new Random();

        params.setName(name);
        params.setValue(value);
        return params;
    }

    private BParams p(String name) {
        params = new BParams();
        params.setName(name);
        return params;
    }

    public BParams[] getTemplate(int repType){
        switch (repType){
            case LO:
                return lowIntensity;
            case ME:
                return medIntensity;
            case HIG:
                return highIntensity;
            default:
                return null;
        }

    }
}
