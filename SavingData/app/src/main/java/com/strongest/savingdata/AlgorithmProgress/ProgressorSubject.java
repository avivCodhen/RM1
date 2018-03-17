package com.strongest.savingdata.AlgorithmProgress;

import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.Database.Exercise.Sets;

/**
 * Created by Cohen on 12/28/2017.
 */

public interface ProgressorSubject {

    void onProgressChanged(Sets prev, Sets next);
    void onProgressInserted(int pos, PLObjects.ExerciseProfile ep);
    void onProgressRemoved(int pos);
    void onProgressSwap(int from, int to);

}
