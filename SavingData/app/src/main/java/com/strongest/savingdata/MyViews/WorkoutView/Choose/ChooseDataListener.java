package com.strongest.savingdata.MyViews.WorkoutView.Choose;

import android.os.Parcelable;

import com.strongest.savingdata.Database.Exercise.Beans;

/**
 * Created by Cohen on 11/23/2017.
 */

public interface ChooseDataListener extends Parcelable {

    void updateBeansHolder(double weight, Beans method);
    void updateBeansHolder(Beans ex, Beans reps, Beans rest, int sets);
}
