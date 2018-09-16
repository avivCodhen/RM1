package com.strongest.savingdata.MyViews;

import android.animation.Animator;
import android.content.Context;
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

    public void show() {

        animateAlpha(1f, this, childView);
    }

    public void hide() {
        animateAlpha(0f, this, childView);
    }

    private boolean hasChild() {
        return childView != null;
    }

    private void animateAlpha(float value, View main, View childView) {

        if (hasChild()) {


            childView.animate().alpha(value == 0 ? 1 : 0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    main.animate().alpha(value).start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();

        } else {
            main.animate().alpha(value).start();
        }
    }
}
