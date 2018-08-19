package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import java.util.ArrayList;

public interface Lister<T> {

    WorkoutObserver getObserver();
    ArrayList<T> getList();
    SpecificExerciseObserver getExerciseObserver();
}
