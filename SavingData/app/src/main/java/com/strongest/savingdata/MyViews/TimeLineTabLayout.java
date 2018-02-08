package com.strongest.savingdata.MyViews;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.strongest.savingdata.R;

/**
 * Created by Cohen on 12/20/2017.
 */

public class TimeLineTabLayout extends LinearLayout {

    private final int viewLayout = R.layout.timeline;
    private Context context;

    public TimeLineTabLayout(Context context) {
        super(context);
        this.context = context;
        initTabLayout();
    }

    public TimeLineTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initTabLayout();
    }

    private void initTabLayout() {
        setOrientation(HORIZONTAL);
        //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
       // params.height = 50;
        setGravity(Gravity.START);
       // setLayoutParams(params);
    }

    public void initiateViewLayout(String text, @DrawableRes int res) {
        View v = LayoutInflater.from(context).inflate(viewLayout,this, false);
        //ImageView icon = (ImageView) findViewById(R.id.timeline_icon);
        TextView tv = (TextView) v.findViewById(R.id.timeline_text);
        //icon.setImageResource(res);
        tv.setText(text);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv.getLayoutParams();
        params.weight = 1;
       // params.gravity = Gravity.RIGHT;

        tv.setLayoutParams(params);
        // v.setLayoutParams(params);
        addView(v);
    }

}
