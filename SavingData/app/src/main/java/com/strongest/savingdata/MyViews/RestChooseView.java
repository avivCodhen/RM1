package com.strongest.savingdata.MyViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.AlgorithmLayout.ExerciseProfileStats;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.NumberChooseManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.OnNumberInject;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.SingleNumberChooseView;
import com.strongest.savingdata.R;

/**
 * Created by Cohen on 3/23/2018.
 */

public class RestChooseView extends LinearLayout implements View.OnClickListener, OnNumberInject {

    private SingleNumberChooseView singleNumberChooseView;
    private NumberChooseManager mNumberManager;
    private TextView minus_5, minus_10, plus_5, plus_10;
    private EditText secondsET;
    private Context context;
    public int minutes;
    public int seconds;


    public RestChooseView(Context context) {
        super(context);
        this.context = context;
    }

    public RestChooseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init() {
        inflate(context, R.layout.layout_rest_numbers, this);
        singleNumberChooseView = (SingleNumberChooseView) findViewById(R.id.rest_single_number_choose);
        singleNumberChooseView.setUpWithNumberChooseManager(mNumberManager, this);
        minus_5 = (TextView) findViewById(R.id.rest_minus_5_tv);
        minus_10 = (TextView) findViewById(R.id.rest_minus_10_tv);
        plus_10 = (TextView) findViewById(R.id.rest_plus_10);
        plus_5 = (TextView) findViewById(R.id.rest_plus_5);
        secondsET = (EditText) findViewById(R.id.rest_seconds);

        minus_5.setOnClickListener(this);
        minus_10.setOnClickListener(this);
        plus_5.setOnClickListener(this);
        plus_10.setOnClickListener(this);

        String rest = mNumberManager.getExerciseSet().getRest();
        singleNumberChooseView.setText(
                rest.charAt(0) == '0' ? rest.substring(1,2) : rest.substring(0,2));
        secondsET.setText(rest.substring(3, rest.length()));
        seconds = Integer.parseInt(rest.substring(3, rest.length()));
    }


    public void setUpWithManager(NumberChooseManager numberChooseManager) {
        mNumberManager = numberChooseManager;
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rest_plus_5:
                if (seconds == 60 || seconds + 5 > 60) {
                    seconds = 60;
                } else {
                    seconds += 5;
                }

                break;

            case R.id.rest_plus_10:
                if (seconds == 60 || seconds + 10 > 60) {
                    seconds = 60;
                } else {
                    seconds += 10;
                }

                break;

            case R.id.rest_minus_5_tv:
                if (seconds == 0 || seconds - 5 < 0) {
                    seconds = 0;
                } else {
                    seconds -= 5;
                }

                break;
            case R.id.rest_minus_10_tv:
                if (seconds == 0 || seconds - 10 < 0) {
                    seconds = 0;
                } else {
                    seconds -= 10;
                }

                break;

        }
        setText();
        mNumberManager.injectRest(ExerciseProfileStats.restToString(60 * singleNumberChooseView.getIntNum() + seconds));
    }

    private void setText() {
        secondsET.setText(seconds + "");
    }

    @Override
    public void onInject(String num) {
        mNumberManager.injectRest(ExerciseProfileStats.restToString(60* Integer.parseInt(num)+seconds));
    }
}
