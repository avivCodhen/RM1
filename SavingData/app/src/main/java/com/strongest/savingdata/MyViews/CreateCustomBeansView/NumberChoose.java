package com.strongest.savingdata.MyViews.CreateCustomBeansView;

import android.view.View;
import android.widget.LinearLayout;

import com.strongest.savingdata.Database.Exercise.Beans;

/**
 * Created by Cohen on 1/19/2018.
 */

public interface NumberChoose {

    boolean hasManager();
    void setUpWithNumberChooseManager(NumberChooseManager numberChooseManager);
    Beans createRepBeans();
    View getView();
}
