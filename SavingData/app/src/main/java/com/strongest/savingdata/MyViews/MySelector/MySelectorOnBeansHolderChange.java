package com.strongest.savingdata.MyViews.MySelector;

import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;

/**
 * Created by Cohen on 2/15/2018.
 */

public interface MySelectorOnBeansHolderChange {

    void notifyExerciseProfileBeanChange(String beanType, Beans bean);
    void notifyMuscleChange(Muscle m);
}
