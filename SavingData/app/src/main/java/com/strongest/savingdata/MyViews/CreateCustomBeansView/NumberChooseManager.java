package com.strongest.savingdata.MyViews.CreateCustomBeansView;

import android.view.View;

import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;

/**
 * Created by Cohen on 1/18/2018.
 */

public class NumberChooseManager {

    private LayoutManager.LayoutManagerHelper helper;
    private ExerciseSet exerciseSet;

    public LayoutManager.LayoutManagerHelper getHelper() {
        return helper;
    }

    public enum Type {
        SingleRep, RangeRep, PyramidRep, Sets, Rest
    }

    public ExerciseSet getExerciseSet() {
        return exerciseSet;
    }

    //high intensity is if it is higher than 10
    public static final int LOW_INTENSITY_MAX = 5, MED_INTENSITY_MAX = 10;

    private OnRangeNumberChooseControl onRangeNumberChooseControl;

    public NumberChooseManager(LayoutManager.LayoutManagerHelper helper, ExerciseSet exerciseSet) {

        this.helper = helper;
        this.exerciseSet = exerciseSet;
    }

    public int calculateRepsIntensity(int num) {
        if (num < LOW_INTENSITY_MAX) {
            return 0;
        } else if (num < MED_INTENSITY_MAX) {
            {
                return 1;
            }
        }
        return 2;
    }

    public void injectRest(String rest){
        exerciseSet.setRest(rest);
        helper.onExerciseSetChange();
    }

    public void injectRep(String rep){
        exerciseSet.setRep(rep);
        helper.onExerciseSetChange();
    }


    public OnRangeNumberChooseControl getOnRangeNumberChooseControl() {
        return onRangeNumberChooseControl;
    }

    public void setOnRangeNumberChooseControl(OnRangeNumberChooseControl onRangeNumberChooseControl) {
        this.onRangeNumberChooseControl = onRangeNumberChooseControl;
    }

    public boolean hasOnRangeControlCallBack() {
        return onRangeNumberChooseControl != null;
    }

    public Beans createBeans(View view, Type type, String name) {
        switch (type) {
            case SingleRep:
                SingleNumberChooseView single = (SingleNumberChooseView) view;
                return Beans.createReps(
                        0,
                        0,
                        name,
                        calculateRepsIntensity(single.getIntNum()),
                        name,
                        0,
                        -1,
                        -1
                );
            case RangeRep:
                RangeNumberChooseView range = (RangeNumberChooseView) view;
                return Beans.createReps(
                        0,
                        0,
                        name,
                        1,
                        name,
                        0,
                        -1,
                        -1
                );
            case PyramidRep:
                PyramidNumberChooseView pyramid = (PyramidNumberChooseView) view;
                return Beans.createReps(
                        0,
                        0,
                        name,
                        1,
                        name,
                        0,
                        -1,
                        -1
                );
            case Sets:
                return null;
            case Rest:
                return null;
            default:
                return null;
        }
    }
}
