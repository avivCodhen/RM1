package com.strongest.savingdata.Animations;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by Cohen on 2/17/2018.
 */

public class MyJavaAnimator {

    private static Handler handler = new Handler();

    public static void rotateView(final View v, final int from, final int to) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final RotateAnimation rotate =
                        new RotateAnimation(from, to, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(300);
                rotate.setFillAfter(true);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        v.startAnimation(rotate);

                    }
                });
            }
        }).start();
    }

    public static void fadeIn(View... v) {
        int j = v.length - 1;
        while (j > -1) {
            v[j].animate().alpha(1.0f).setDuration(600).start();
            v[j].setVisibility(View.VISIBLE);
            j--;
        }
    }

    public static void fadeOut(View... v) {
        for (View view : v) {
            view.animate().alpha(0.0f).setDuration(600).start();
            //view.setVisibility(View.INVISIBLE);
        }
    }

    public static void expand(final View v, int height) {

        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
