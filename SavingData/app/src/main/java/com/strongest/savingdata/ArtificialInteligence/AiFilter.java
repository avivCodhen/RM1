package com.strongest.savingdata.ArtificialInteligence;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;

/**
 * Created by Cohen on 12/8/2017.
 */

public class AiFilter {

    private static final String N_EXERCISES = "nexercise";
    private static final String T_MUSCLE = "tmuscle";
    private Context context;

    public AiFilter(Context context) {
        this.context = context;
    }

    private Resources r() {
        return context.getResources();
    }


    public String filterAction(@StringRes int resId, int replacedInt, String replacedString) {
        String action = r().getString(resId);
        if (replacedInt != -1) {
            action =  action.replace(N_EXERCISES, String.valueOf(replacedInt));
        }

        if (replacedString != null) {
           action = action.replace(T_MUSCLE, replacedString);
        }
        return action;
    }

}