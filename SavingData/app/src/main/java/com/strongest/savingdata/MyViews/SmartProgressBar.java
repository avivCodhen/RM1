package com.strongest.savingdata.MyViews;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.R;


public class SmartProgressBar extends LinearLayout {

    private final Context context;
    private TextView textView;
    private View childView;
    private Handler handler;
    private boolean hasRabbitHoleBreaker = false;
    private OnEnteringRabbitHole onEnteringRabbitHole;
    private int breakLoopDuration = 0;

    public SmartProgressBar(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public SmartProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();

    }

    private void initView() {
        handler = new Handler();
        inflate(context, R.layout.layout_custom_progress, this);
        textView = findViewById(R.id.loading_tv);
        setAlpha(0);
    }

    public SmartProgressBar setText(String text) {
        textView.setText(text);
        return this;
    }

    public SmartProgressBar whiteText(boolean b) {
        if (b) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
        return this;
    }

    public SmartProgressBar setUpWithView(View v) {
        this.childView = v;
        return this;
    }

    public SmartProgressBar registerRabitHoleBreaker(int duration, OnEnteringRabbitHole rabbitHole) {
       this.breakLoopDuration = duration;
       this.onEnteringRabbitHole = rabbitHole;
       this.hasRabbitHoleBreaker = rabbitHole != null;
        return this;
    }

    public void show() {

        animateShow(this);
        if(hasRabbitHoleBreaker){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onEnteringRabbitHole.breakLoop();
                    hide();
                }
            }, breakLoopDuration);
        }
    }

    public void hide() {
        animateHide(this);
    }

    private boolean hasChild() {
        return childView != null;
    }

    private void animateShow(View main) {
        if (hasChild()) {
            childView.animate().alpha(0).setDuration(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    main.animate().alpha(1).setDuration(0).start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();

        } else {
            main.animate().alpha(1).setDuration(0).start();
        }
    }

    private void animateHide(View main) {
        if (hasChild()) {
            childView.animate().alpha(1).setDuration(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    main.animate().alpha(0).setDuration(0).start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();

        } else {
            main.animate().alpha(0).setDuration(0).start();
        }
    }

    public interface OnEnteringRabbitHole {

        void breakLoop();
    }
}
