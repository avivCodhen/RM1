package com.strongest.savingdata.AlgorithmProgress;

import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.Database.Exercise.Sets;

/**
 * Created by Cohen on 12/28/2017.
 */

public interface ProgressorObserver {

    void notifyProgressorChanged(Sets prev, Sets next);
    void notifyProgressorInserted(int pos, PLObjects.ExerciseProfile ep);
    void notifyProgressorRemoved(int pos);
    void notifyProgressorSwap(int from, int to);
}
