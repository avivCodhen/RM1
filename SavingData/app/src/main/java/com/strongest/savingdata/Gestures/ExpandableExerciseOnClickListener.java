package com.strongest.savingdata.Gestures;

import android.os.SystemClock;
import android.view.View;

/**
 * Created by Cohen on 4/1/2018.
 */

public abstract class ExpandableExerciseOnClickListener implements View.OnClickListener {

    private long prevClickTime =0;

    @Override
    public void onClick(View v) {
        _onClick(v);
    }

    private synchronized  void _onClick(View v) {

        long current = SystemClock.elapsedRealtime();
        if(current-prevClickTime> getGap()){
            onSingleClick(v);
            prevClickTime = SystemClock.elapsedRealtime();
        }else {
            onDoubleClick(v);
            prevClickTime = 0;
        }
    }

    public abstract void onSingleClick(View v);
    public abstract void onDoubleClick(View v);


    public long getGap(){
        return 500L; //500ms
    }
}
