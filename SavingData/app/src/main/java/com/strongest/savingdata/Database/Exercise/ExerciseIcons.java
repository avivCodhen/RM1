package com.strongest.savingdata.Database.Exercise;

import com.strongest.savingdata.R;


/**
 * Created by Cohen on 12/23/2017.
 */

public class ExerciseIcons {

    private static int[] chestIcons = {
            R.drawable.ex_selector_barbell_flat_bench_press,
            R.drawable.ex_barbell_incline_benchpress,
            R.drawable.ex_barbell_decline_benchpress,
            R.drawable.ex_dumbell_decline_benchpress,
            R.drawable.ex_pushups,
    };

    public static int[] getExerciseIcon(int muscle) {
        switch (muscle){
            case 0:
                return chestIcons;
        }
        return null;
    }
}
