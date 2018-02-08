package com.strongest.savingdata.AlgorithmGenerator;

import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;

import java.util.Random;

/**
 * Created by Cohen on 9/27/2017.
 */

public class MethodTemplate {

    private Random r = new Random();
    private final int SIMPLE = 0, BALANCED = 2, COMPLEX = 3;
    private final int NOT = -1;
    private Load load;
    private Muscle b;
    private BParams[] aParams;
    int counter = 0;

    private final String ID = DBExercisesHelper.ID;
    private final String LEVEL = DBExercisesHelper.LEVEL;

    private BParams[] simple, balanced, complex;

    public MethodTemplate(Muscle b, Load load) {
        this.load = load;
        this.b = b;


        /*simple = new BParams[]{p(ID, NOT)};
        balanced = new BParams[]{p(LEVEL, BALANCED)};
        complex = new BParams[]{p(LEVEL, COMPLEX)};*/
     //   complex = new BParams[]{p(LEVEL, COMPLEX)};

    }

    private BParams[] balanced() {
        BParams[] p = new BParams[1];
        BParams p1 = new BParams();
        /*p1.setName(LEVEL);
        p1.setValue(r.nextInt());*/
        p[0] = p1;
        p[0].setName(LEVEL);
    //    p[0].setValue(r.nextInt(2));
        return p;
    }

    //load has three levels: 1, 2 & 3
    // for levels, 1 = novice exercise level, 2 = intermediate, 3 = advanced
    // for type, 1 = beginners exercise(commonly machines), 2 = isolation exercise, 3 = compound
    // a novice exercise can be compound, and so advanced exercise can be beginners
    // not all machines are beginners exercises

    //B = back, C = chest, O = one exercise, LO = level one...
    private BParams params;

    private BParams[] results(int request){
        if(request == SIMPLE){
            return new BParams[]{p(ID, NOT)};
        }
        if(request == BALANCED){
            return new BParams[]{p(LEVEL, BALANCED)};
        }
        return new BParams[]{p(LEVEL, COMPLEX)};
    }

    //3 needs to be replaced with max level;
    private BParams p(String name, int value) {
        params = new BParams();
        r = new Random();
        if (value == COMPLEX) {
            value = r.nextInt(load.getExerciseLevel()+1);
        }

        if (value == BALANCED) {
            int random = r.nextInt(10);
            if(random > 5){
                value = r.nextInt(load.getExerciseLevel()+1);
            }else{
                value = NOT;
            }
        }

        params.setName(name);
       // params.setValue(value);
        return params;
    }

    private BParams p(String name) {
        counter++;
        params = new BParams();
        params.setName(name);
        return params;
    }


    public BParams[][] getTemplate(int num_ex) {
        switch (num_ex) {
            case 1:
                switch (load.getComplexity()) {
                    case 0:
                        return new BParams[][]{
                                results(SIMPLE)
                        };
                    case 1:
                        return new BParams[][]{
                                results(BALANCED)
                        };
                    case 2:
                        return new BParams[][]{
                                results(COMPLEX),
                        };

                }
            case 2:
                switch (load.getComplexity()) {
                    case 0:
                        return new BParams[][]{
                                results(SIMPLE),
                                results(SIMPLE),
                        };
                    case 1:
                        return new BParams[][]{
                                results(SIMPLE),
                                results(BALANCED),
                        };
                    case 2:
                        return new BParams[][]{
                                results(BALANCED),
                                results(COMPLEX),
                        };

                }
            case 3:
                switch (load.getComplexity()) {
                    case 0:
                        return new BParams[][]{
                                results(SIMPLE),
                                results(SIMPLE),
                                results(SIMPLE),
                        };
                    case 1:
                        return new BParams[][]{
                                results(SIMPLE),
                                results(BALANCED),
                                results(BALANCED)
                        };
                    case 2:
                        return new BParams[][]{
                                results(BALANCED),
                                results(COMPLEX),
                                results(BALANCED),
                        };

                }
            case 4:
                switch (load.getComplexity()) {
                    case 0:
                        return new BParams[][]{
                                results(SIMPLE),
                                results(SIMPLE),
                                results(SIMPLE),
                                results(SIMPLE),
                        };
                    case 1:
                        return new BParams[][]{
                                results(SIMPLE),
                                results(BALANCED),
                                results(BALANCED),
                                results(BALANCED),
                        };
                    case 2:
                        return new BParams[][]{
                                results(SIMPLE),
                                results(BALANCED),
                                results(COMPLEX),
                                results(BALANCED)
                        };

                }
            case 5:
                switch (load.getComplexity()) {

                    case 0:
                        return new BParams[][]{
                                results(SIMPLE),
                                results(SIMPLE),
                                results(SIMPLE),
                                results(SIMPLE),
                                results(SIMPLE),
                        };
                    case 1:
                        return new BParams[][]{
                                results(SIMPLE),
                                results(SIMPLE),
                                results(BALANCED),
                                results(BALANCED),
                                results(BALANCED),
                        };
                    case 2:
                        return new BParams[][]{
                                results(SIMPLE),
                                results(BALANCED),
                                results(COMPLEX),
                                results(BALANCED),
                                results(BALANCED)
                        };
                }
        }

        return null;
    }
}
