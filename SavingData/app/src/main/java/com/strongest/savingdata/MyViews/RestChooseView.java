package com.strongest.savingdata.MyViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.AModels.AlgorithmLayout.ExerciseProfileStats;
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
    private TextView secondsET;
    private Context context;
    public int minutes;
    public int seconds;
    public String rest;


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
        secondsET = (TextView) findViewById(R.id.rest_seconds);

        minus_5.setOnClickListener(this);
        minus_10.setOnClickListener(this);
        plus_5.setOnClickListener(this);
        plus_10.setOnClickListener(this);

        String restTime = mNumberManager.getExerciseSet().getRest();
       initRest(restTime);
    }

    public void initRest(String restTime){
        singleNumberChooseView.setText(
                restTime.charAt(0) == '0' ? restTime.substring(1,2) : restTime.substring(0,2));
        secondsET.setText(restTime.substring(3, restTime.length()));
        seconds = Integer.parseInt(restTime.substring(3, restTime.length()));
        rest = ExerciseProfileStats.restToString(60 * singleNumberChooseView.getIntNum() + seconds);
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
        rest = ExerciseProfileStats.restToString(60 * singleNumberChooseView.getIntNum() + seconds);
        mNumberManager.injectRest(rest);
    }

    private void setText() {
        secondsET.setText(seconds + "");
    }

    @Override
    public void onInject(String num) {
        mNumberManager.injectRest(ExerciseProfileStats.restToString(60* Integer.parseInt(num)+seconds));
    }
}
