package com.strongest.savingdata.MyViews.CreateCustomBeansView;

import android.view.View;
import android.widget.TextSwitcher;

import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.MyViews.RestChooseView;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.OnExerciseSetChange;

/**
 * Created by Cohen on 1/18/2018.
 */

public class NumberChooseManager {

   // private LayoutManager.LayoutManagerHelper helper;
    private ExerciseSet exerciseSet;
    private OnExerciseSetChange onExerciseSetChange;
    private RestChooseView restChooseView;
    private SingleNumberChooseView singleNumberChooseView;
    private TextSwitcher repsTextSwitcher, restTextSwitcher;

    public void setRestChooseView(RestChooseView restChooseView) {
        this.restChooseView = restChooseView;
    }

    public void setSingleNumberChooseView(SingleNumberChooseView singleNumberChooseView) {
        this.singleNumberChooseView = singleNumberChooseView;
    }

    public void setRestTextSwitcher(TextSwitcher restTextSwitcher) {
        this.restTextSwitcher = restTextSwitcher;
    }

    public void setRepsTextSwitcher(TextSwitcher repsTextSwitcher) {
        this.repsTextSwitcher = repsTextSwitcher;
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

    public NumberChooseManager(OnExerciseSetChange onExerciseSetChange, ExerciseSet exerciseSet) {
        this.onExerciseSetChange = onExerciseSetChange;

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

    public void setTextSwitcherText(TextSwitcher textSwitcher, String text){
        textSwitcher.setText(text);
    }

    public void injectRest(String rest){
        exerciseSet.setRest(rest);
        onExerciseSetChange.notifyExerciseSetChange();
        setTextSwitcherText(restTextSwitcher, rest);
    }

    public void injectRep(String rep){
        exerciseSet.setRep(rep);
        onExerciseSetChange.notifyExerciseSetChange();
        setTextSwitcherText(repsTextSwitcher, rep);
    }

    public void updateSingleNum(){
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
