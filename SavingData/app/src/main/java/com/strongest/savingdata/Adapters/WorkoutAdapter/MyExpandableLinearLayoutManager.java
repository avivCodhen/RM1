package com.strongest.savingdata.Adapters.WorkoutAdapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Cohen on 2/12/2018.
 */

public class MyExpandableLinearLayoutManager  extends LinearLayoutManager{

        private boolean isScrollEnabled = true;

        public MyExpandableLinearLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
}
