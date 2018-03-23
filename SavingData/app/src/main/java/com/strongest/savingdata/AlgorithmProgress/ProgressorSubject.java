package com.strongest.savingdata.AlgorithmProgress;

import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;

/**
 * Created by Cohen on 12/28/2017.
 */

public interface ProgressorSubject {

    void onProgressChanged(ExerciseSet prev, ExerciseSet next);
    void onProgressInserted(int pos, PLObject.ExerciseProfile ep);
    void onProgressRemoved(int pos);
    void onProgressSwap(int from, int to);

}
