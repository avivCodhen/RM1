package com.strongest.savingdata.MyViews;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Cohen on 9/20/2017.
 */

@Deprecated
public class Line extends View {
    public Line(Context context) {
        super(context);
        setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                5
        ));
        setBackgroundColor(Color.parseColor("#B3B3B3"));
    }
}
