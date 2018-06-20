package com.strongest.savingdata.AModels.AlgorithmLayout;

import java.util.ArrayList;

public interface Lister<T> {

    WorkoutObserver getObserver();
    ArrayList<T> getList();
}
