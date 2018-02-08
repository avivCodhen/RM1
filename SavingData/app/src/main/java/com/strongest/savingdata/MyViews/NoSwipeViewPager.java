package com.strongest.savingdata.MyViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * Created by Cohen on 10/13/2017.
 */

public class NoSwipeViewPager extends ViewPager {
    public NoSwipeViewPager(Context context) {
        super(context);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

}
