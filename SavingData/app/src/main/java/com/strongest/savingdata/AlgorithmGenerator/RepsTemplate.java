package com.strongest.savingdata.AlgorithmGenerator;

import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;

import java.util.Random;

/**
 * Created by Cohen on 9/27/2017.
 */

public class RepsTemplate {

    private final String LOW_MED = "-1", MED_HIGH = "-2";

    private final boolean TRUE = true;
    private final int FALSE = 0;
    private final String INTENSITY = DBExercisesHelper.INTENSITY;

    private final String LOW = "0", MED = "1", HIGH = "2";
    private final int LO=0, ME= 1, HIG=2, LOMED=-1, MEHIG=-2;

    private Load load;
    private Muscle m;
    private Random r;
    private BParams[] aParams;

    private Muscle muscle;
    private BParams[] lowIntensity, medIntensity, highIntensity;
    private BParams[] lowMedIntensity, medHighIntensity;

    public RepsTemplate(Muscle b, Load load) {
        this.load = load;
        this.m = b;

        lowIntensity = new BParams[]{p(INTENSITY, LOW)};
        medIntensity = new BParams[]{p(INTENSITY, MED)};
        highIntensity = new BParams[]{p(INTENSITY, HIGH)};
        lowMedIntensity = new BParams[]{p(INTENSITY, LOW_MED)};
        medHighIntensity = new BParams[]{p(INTENSITY, MED_HIGH)};

    }

    private boolean isBig() {
        if (m.getMuscle_size() == "big") {
            return true;
        }
        return false;
    }
    //load has three levels: 1, 2 & 3
    // for levels, 1 = novice exercise level, 2 = intermediate, 3 = advanced
    // for type, 1 = beginners exercise(commonly machines), 2 = isolation exercise, 3 = compound
    // a novice exercise can be compound, and so advanced exercise can be beginners
    // not all machines are beginners exercises

    //B = back, C = chest, O = one exercise, LO = level one...
    private BParams params;


    //3 needs to be replaced with max level;
    private BParams p(String name, String value) {
        params = new BParams();
        r = new Random();
        try{
            int v = Integer.parseInt(value);
            if (!isBig() && v == 0) {
                v += 1;
            }
            if (v == LOMED) {
                v = r.nextInt(ME + 1);
            }
            if (v == MEHIG) {
                v = r.nextInt(HIG) + 1;
            }
        }catch(Exception e){
            e.toString();
        }


        params.setName(name);
        params.setValue(value);
        return params;
    }

    private BParams p(String name) {
        params = new BParams();
        params.setName(name);
        return params;
    }


    public BParams[][] getTemplate(int num_ex) {
        switch (num_ex) {
            case 1:
                switch (load.getIntensity()) {
                    case 0:
                        return new BParams[][]{
                                lowIntensity
                        };
                    case 1:
                        return new BParams[][]{
                                medIntensity
                        };
                    case 2:
                        return new BParams[][]{
                                highIntensity
                        };

                }
            case 2:
                switch (load.getIntensity()) {
                    case 0:
                        return new BParams[][]{
                                lowIntensity,
                                lowIntensity
                        };
                    case 1:
                        return new BParams[][]{
                                lowMedIntensity,
                                medHighIntensity
                        };
                    case 2:
                        return new BParams[][]{
                                medHighIntensity,
                                highIntensity
                        };

                }
            case 3:
                switch (load.getIntensity()) {
                    case 0:
                        return new BParams[][]{
                                lowIntensity,
                                lowIntensity,
                                medIntensity
                        };
                    case 1:
                        return new BParams[][]{
                               lowIntensity,
                                medIntensity,
                                highIntensity
                        };
                    case 2:
                        return new BParams[][]{
                                medIntensity,
                                medHighIntensity,
                                highIntensity
                        };

                }
            case 4:
                switch (load.getIntensity()){

                    case 0:
                        return new BParams[][]{
                                lowIntensity,
                                lowIntensity,
                                medIntensity,
                                medIntensity
                        };
                    case 1:
                        return new BParams[][]{
                                lowIntensity,
                                lowMedIntensity,
                                medHighIntensity,
                                highIntensity
                        };
                    case 2:
                        return new BParams[][]{
                                medIntensity,
                                medIntensity,
                                highIntensity,
                                highIntensity,
                        };

                }
            case 5:
                switch (load.getIntensity()){

                    case 0:
                        return new BParams[][]{
                                lowIntensity,
                                lowIntensity,
                                lowMedIntensity,
                                medIntensity,
                                medHighIntensity,
                                highIntensity
                        };
                    case 1:
                        return new BParams[][]{
                                lowIntensity,
                                lowMedIntensity,
                                medIntensity,
                                medHighIntensity,
                                highIntensity
                        };
                    case 2:
                        return new BParams[][]{
                                medIntensity,
                                medIntensity,
                                medHighIntensity,
                                highIntensity,
                                highIntensity,
                        };
                }
        }

        return null;
    }

}
