package com.strongest.savingdata.AlgorithmGenerator;

import android.util.Log;

import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;

import java.util.Random;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.*;

/**
 * Created by Cohen on 8/28/2017.
 */

public class ExerciseTemplate {

    //chest & back - by levels.
    //legs - by levels and type
    //shoulders - by type and muscles
    //arms - by muscles
    //triceps & biceps - nothing


    //strings are DB colum names
    //int are DM colum values
    private final String ANY = "-2";
    private String EXERCISE_LEVEL, MINUS_EXERCISE_LEVEL;
    private final int ADVANCED = 3;

    private final boolean TRUE = true;
    private final int FALSE = 0;
    private final String LEVEL = DBExercisesHelper.LEVEL;
    private final String TYPE = DBExercisesHelper.TYPE;

    private final String ANTERIOR = "quadriceps";
    private final String POSTERIOR = "hamstring";

    private final String FR_SHOULDERS = "anterior_shoulders";
    private final String RE_SHOULDERS = "posterior_shoulders";

    private final String BICEPS = "biceps";
    private final String TRICEPS = "triceps";


    private Load load;
    private Random r;
    private BParams[] aParams;

    private String muscle;


    private final int LEVEL_P = 0, MINUS_LEVEL_P = 1, ANY_P = 2,
            ANTERIOR_SHOULDER_P = 3, MINUS_ANTERIOR_SHOULDER_P = 4,
            REAR_SHOULDER_P = 5,
            LEVEL_TYPE_P = 6,
            TRICEPS_P = 7, BICEPS_P = 8,
            ANTERIOR_LEGS_ANY_LEVEL_P = 9, POSTERIOR_LEGS_ANY_LEVEL_P = 10;


    private BParams params;

    //load has three levels: 1, 2 & 3
    // for levels, 1 = novice exercise level, 2 = intermediate, 3 = advanced
    // for type, 1 = beginners exercise(commonly machines), 2 = isolation exercise, 3 = compound
    // a novice exercise can be compound, and so advanced exercise can be beginners
    // not all machines are beginners exercises

    //B = back, C = chest, O = one exercise, LO = level one...

    public ExerciseTemplate(Muscle m, Load load) {
        this.load = load;
        this.muscle = m.getMuscle_name();
        EXERCISE_LEVEL = String.valueOf(load.getExerciseLevel());
        MINUS_EXERCISE_LEVEL = String.valueOf(load.getExerciseLevel()-1);
    }


    private BParams[] results(int request) {
        switch (request) {
            case ANY_P:
                return new BParams[]{p(MUSCLES, muscle), p(LEVEL, ANY)};
            case LEVEL_P:
                return new BParams[]{p(MUSCLES, muscle), p(LEVEL, EXERCISE_LEVEL)};
            case LEVEL_TYPE_P:
                return new BParams[]{p(MUSCLES, muscle), p(LEVEL, EXERCISE_LEVEL), p(TYPE, EXERCISE_LEVEL)};
            case MINUS_LEVEL_P:
                return new BParams[]{p(MUSCLES, muscle), p(LEVEL, MINUS_EXERCISE_LEVEL)};
            case ANTERIOR_SHOULDER_P:
                return new BParams[]{p(MUSCLES, muscle), p(MUSCLES, FR_SHOULDERS), p(LEVEL, EXERCISE_LEVEL)};
            case MINUS_ANTERIOR_SHOULDER_P:
                return new BParams[]{p(MUSCLES, muscle), p(MUSCLES, FR_SHOULDERS), p(LEVEL, MINUS_EXERCISE_LEVEL)};
            case REAR_SHOULDER_P:
                return new BParams[]{p(MUSCLES, muscle), p(MUSCLES, RE_SHOULDERS)};
            case ANTERIOR_LEGS_ANY_LEVEL_P:
                return new BParams[]{p(MUSCLES, muscle), p(MUSCLES, ANTERIOR), p(LEVEL, ANY)};
            case POSTERIOR_LEGS_ANY_LEVEL_P:
                return new BParams[]{p(MUSCLES, muscle), p(MUSCLES, POSTERIOR), p(LEVEL, ANY)};
            case TRICEPS_P:
                return new BParams[]{p(MUSCLES, muscle), p(MUSCLES, TRICEPS)};
            case BICEPS_P:
                return new BParams[]{p(MUSCLES, muscle), p(MUSCLES, BICEPS)};
            default:
                return null;
        }
    }

    private BParams p(String name, String value) {
        params = new BParams();
        r = new Random();
        try{
            int v = Integer.parseInt(value);

            if (v == -1) {
                v = 0;
            }
            if (v == Integer.parseInt(ANY)) {
                if (load.getExerciseLevel() == 0) {
                    v = 0;
                } else {
                    v = r.nextInt(load.getExerciseLevel()) + 1;
                }
            }
            value = String.valueOf(v);
        }catch(Exception e){
        }
       /* if (value == -1 && !name.equals(MUSCLE)) {
            value = 0;
        }
        if (value == ANY) {
            if (load.getExerciseLevel() == 0) {
                value = 0;
            } else {
                value = r.nextInt(load.getExerciseLevel()) + 1;
            }
        }
        if (muscle.equals("shoulders") && value > 1 && name.equals(LEVEL)) {
            value--;
        }*/

        params.setName(name);
        params.setValue(value);
        return params;
    }

    /*private BParams p(String name, String value) {
        params = new BParams();
        params.setName(name);
        params.setValue(value);
        return params;
    }*/


    public BParams[][] getTemplate(int num_ex) {

        switch (num_ex) {

            case 1:
                switch (muscle) {
                    case "chest":
                        return new BParams[][]{
                                results(LEVEL_P)};

                    case "back":
                        return new BParams[][]{
                                results(LEVEL_P)};

                    case "legs":
                        //this might be a problem
                        //{p(LEVEL, load.getExerciseLevel()), p(TYPE, load.getExerciseLevel())},
                        return new BParams[][]{
                                results(LEVEL_TYPE_P)
                        };
                    case "shoulders":
                        return new BParams[][]{
                                results(ANTERIOR_SHOULDER_P)
                        };
                    case "triceps":
                        return new BParams[][]{
                                results(ANY_P),
                        };
                    case "biceps":
                        return new BParams[][]{
                                results(ANY_P),
                        };

                }
            case 2:
                switch (muscle) {
                    case "chest":
                        return new BParams[][]{
                                results(LEVEL_P),
                                results(LEVEL_P),
                        };
                    case "back":
                        return new BParams[][]{
                                results(LEVEL_P),
                                results(LEVEL_P),
                        };
                    case "legs":
                        return new BParams[][]{
                                results(LEVEL_TYPE_P),
                                results(LEVEL_TYPE_P),
                        };

                    case "shoulders":
                        return new BParams[][]{
                                results(ANTERIOR_SHOULDER_P),
                                results(ANTERIOR_SHOULDER_P)
                        };
                    case "triceps":
                        return new BParams[][]{
                                results(ANY_P),
                                results(ANY_P)
                        };
                    case "biceps":
                        return new BParams[][]{
                                results(ANY_P),
                                results(ANY_P)
                        };
                }

            case 3:
                switch (muscle) {
                    case "chest":
                        return new BParams[][]{
                                results(LEVEL_P),
                                results(LEVEL_P),
                                results(MINUS_LEVEL_P),
                        };
                    case "back":
                        return new BParams[][]{
                                results(LEVEL_P),
                                results(LEVEL_P),
                                results(MINUS_LEVEL_P),
                        };
                    case "legs":
                        return new BParams[][]{
                                results(LEVEL_TYPE_P),
                                results(ANTERIOR_LEGS_ANY_LEVEL_P),
                                results(POSTERIOR_LEGS_ANY_LEVEL_P)
                        };
                    case "shoulders":
                        return new BParams[][]{
                                results(ANTERIOR_SHOULDER_P),
                                results(MINUS_ANTERIOR_SHOULDER_P),
                                results(REAR_SHOULDER_P),
                        };
                    case "triceps":
                        return new BParams[][]{
                                results(ANY_P),
                                results(ANY_P),
                                results(ANY_P)
                        };
                    case "biceps":
                        return new BParams[][]{
                                results(ANY_P),
                                results(ANY_P),
                                results(ANY_P)
                        };

                }
            case 4:
                switch (muscle) {
                    case "chest":
                        return new BParams[][]{
                                results(LEVEL_P),
                                results(LEVEL_P),
                                results(ANY_P),
                                results(MINUS_LEVEL_P)
                        };
                    case "back":
                        return new BParams[][]{
                                results(LEVEL_P),
                                results(LEVEL_P),
                                results(ANY_P),
                                results(MINUS_LEVEL_P)
                        };
                    case "legs":
                        return new BParams[][]{
                                results(LEVEL_P),
                                results(LEVEL_P),
                                results(ANTERIOR_LEGS_ANY_LEVEL_P),
                                results(POSTERIOR_LEGS_ANY_LEVEL_P)
                        };
                    case "shoulders":
                        return new BParams[][]{
                                results(ANTERIOR_SHOULDER_P),
                                results(MINUS_ANTERIOR_SHOULDER_P),
                                results(REAR_SHOULDER_P),
                                results(REAR_SHOULDER_P),
                        };
                    case "arms":
                        return new BParams[][]{
                                results(TRICEPS_P),
                                results(TRICEPS_P),
                                results(BICEPS_P),
                                results(BICEPS_P)
                        };
                }
            case 5:

                switch (muscle) {
                    case "chest":
                        return new BParams[][]{
                                results(LEVEL_P),
                                results(LEVEL_P),
                                results(ANY_P),
                                results(ANY_P),
                                results(MINUS_LEVEL_P)
                        };
                    case "back":
                        return new BParams[][]{
                                results(LEVEL_P),
                                results(LEVEL_P),
                                results(ANY_P),
                                results(ANY_P),
                                results(MINUS_LEVEL_P)
                        };
                    case "legs":
                        return new BParams[][]{
                                results(LEVEL_P),
                                results(MINUS_LEVEL_P),
                                results(ANY_P),
                                results(ANTERIOR_LEGS_ANY_LEVEL_P),
                                results(POSTERIOR_LEGS_ANY_LEVEL_P),
                                results(MINUS_LEVEL_P),
                        };

                }


        }
        return null;
    }

}
