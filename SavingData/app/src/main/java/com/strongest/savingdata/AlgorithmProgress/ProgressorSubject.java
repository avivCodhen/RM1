package com.strongest.savingdata.AlgorithmProgress;

import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.Database.Exercise.BeansHolder;

/**
 * Created by Cohen on 12/28/2017.
 */

public interface ProgressorSubject {

    void onProgressChanged(BeansHolder prev, BeansHolder next);
    void onProgressInserted(int pos, PLObjects.ExerciseProfile ep);
    void onProgressRemoved(int pos);
    void onProgressSwap(int from, int to);

}
