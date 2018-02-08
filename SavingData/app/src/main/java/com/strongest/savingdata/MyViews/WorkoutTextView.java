package com.strongest.savingdata.MyViews;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Cohen on 7/20/2017.
 */

//used to view workout in the main exercise layout
public class WorkoutTextView extends TextView implements Serializable{

    private int titlePosition;
    public WorkoutTextView(Context context) {
        super(context);

        init();
    }

    public LinearLayout.LayoutParams setParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 70, 0, 20);
        return params;
    }

    private void init() {

        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        setGravity(Gravity.CENTER);
        setLayoutParams(setParams());
    }

    public int getTitlePosition() {
        return titlePosition;
    }

    public void setTitlePosition(int titlePosition) {
        this.titlePosition = titlePosition;
    }
}
