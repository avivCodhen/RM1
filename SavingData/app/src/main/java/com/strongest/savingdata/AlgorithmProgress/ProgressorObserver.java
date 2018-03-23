package com.strongest.savingdata.AlgorithmProgress;

import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;

/**
 * Created by Cohen on 12/28/2017.
 */

public interface ProgressorObserver {

    void notifyProgressorChanged(ExerciseSet prev, ExerciseSet next);
    void notifyProgressorInserted(int pos, PLObject.ExerciseProfile ep);
    void notifyProgressorRemoved(int pos);
    void notifyProgressorSwap(int from, int to);
}
