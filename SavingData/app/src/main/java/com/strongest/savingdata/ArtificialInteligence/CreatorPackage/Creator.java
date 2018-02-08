package com.strongest.savingdata.ArtificialInteligence.CreatorPackage;

import android.content.Context;

import com.strongest.savingdata.ArtificialInteligence.AiActions;
import com.strongest.savingdata.ArtificialInteligence.ArtificialIntelligence;
import com.strongest.savingdata.BaseWorkout.Programmer;

import java.util.ArrayList;

/**
 * Created by Cohen on 11/24/2017.
 */

public class Creator extends ArtificialIntelligence {


    private final int icon = 0;
    private static Context context;
    private Programmer programmer;

    public Creator(Context context, Programmer programmer) {
        super(context, programmer);
        this.context = context;

        this.programmer = programmer;
    }


    /*@Override
    protected String[] learnNotes() {
        return new String[0];
    }

    @Override
    public String[] getSuggestions(int workoutPosition) {
        return super.suggestions;
    }

    @Override
    public String[] getNotes() {
        return super.notes;
    }
*/

    @Override
    public void updatePosition(int workoutPosition) {
        super.setCurrentWorkoutPosition(workoutPosition);
    }
}
