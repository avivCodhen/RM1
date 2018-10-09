package com.strongest.savingdata.Animations;

import android.animation.Animator;
import android.graphics.Rect;
import android.media.Image;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.transition.Explode;
import android.support.transition.Transition;
import android.support.transition.TransitionSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Controllers.CallBacks;
import com.strongest.savingdata.R;

import de.hdodenhof.circleimageview.CircleImageView;

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

    public static void fadeIn(int duration, View... v) {
        int j = v.length - 1;
        while (j > -1) {
            v[j].animate().alpha(1.0f).setDuration(duration).start();
            v[j].setVisibility(View.VISIBLE);
            j--;
        }
    }

    public static void fadeOut(int duration,View... v) {
        for (View view : v) {
            view.animate().alpha(0.0f).setDuration(duration).start();
            //view.setVisibility(View.INVISIBLE);
        }
    }

    public static void expand(final View v, int height) {

        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void fadeInAndOut(final String type, final Object obj, final View... views) {
        for (final View v : views) {
            v.animate().alpha(0f).setDuration(200).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    switch (type) {
                        case "bodyview": {
                            //  ((MyExpandableAdapter.MuscleViewHolder) v)
                        }
                    }
                }
            }).start();
        }
    }

    public static void fadeInAndOutTextView(final String text, final TextView... tv) {
        for (final TextView v : tv) {
            v.animate().alpha(0f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    v.setText(text);
                    v.animate().alpha(1f).start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }

    public static void fadeInAndOutImageView(final int resource, final int color, final ImageView... tv) {
        for (final ImageView v : tv) {
            v.animate().alpha(0f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    v.setImageResource(resource);
                    if (color != -1) {
                        ((CircleImageView) v).setCircleBackgroundColor(color);
                    }
                    v.animate().alpha(1f).start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }

    public static void slideOutAndIn(ImageView iv) {

    }

    public static void MoveView(final int amountToMarginLeft, final int amountToMarginRight, final ViewGroup view) {
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
        anim.setDuration(1000);

        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                params.leftMargin -= amountToMarginLeft;
                params.rightMargin += amountToMarginRight;
                view.setLayoutParams(params);
            }
        });
        anim.setFillAfter(true);

        view.startAnimation(anim);
    }

    public static void explode(RecyclerView.ViewHolder vh, RecyclerView recycler, RecyclerView.Adapter adapter) {
        Rect rect = new Rect();
        vh.itemView.getGlobalVisibleRect(rect);
        rect.top = rect.bottom;
        Explode explode = new Explode();
        explode.setEpicenterCallback(new Transition.EpicenterCallback() {
            @Override
            public Rect onGetEpicenter(@NonNull Transition transition) {
                return rect;
            }
        });
        TransitionSet set = new TransitionSet();
        set.setPropagation(null);
        set.addTransition(explode);

        android.support.transition.TransitionManager.beginDelayedTransition(recycler, set);
        recycler.setAdapter(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recycler.setAdapter(adapter);
            }
        }, 1000);
    }


    public static class FlipAnimation extends Animation {
        private Camera camera;

        private View fromView;
        private View toView;

        private float centerX;
        private float centerY;

        private boolean forward = true;

        /**
         * Creates a 3D flip animation between two views.
         *
         * @param fromView First view in the onEditExerciseClick.
         * @param toView   Second view in the onEditExerciseClick.
         */
        public FlipAnimation(View fromView, View toView) {
            this.fromView = fromView;
            this.toView = toView;

            setDuration(500);
            setFillAfter(false);
            setInterpolator(new AccelerateDecelerateInterpolator());
        }

        public void reverse() {
            forward = false;
            View switchView = toView;
            toView = fromView;
            fromView = switchView;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            centerX = width / 2;
            centerY = height / 2;
            camera = new Camera();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            // Angle around the y-axis of the rotation at the given time
            // calculated both in radians and degrees.
            final double radians = Math.PI * interpolatedTime;
            float degrees = (float) (180.0 * radians / Math.PI);

            // Once we reach the midpoint in the animation, we need to hide the
            // source view and show the destination view. We also need to change
            // the angle by 180 degrees so that the destination does not come in
            // flipped around
            if (interpolatedTime >= 0.5f) {
                degrees -= 180.f;
                fromView.setVisibility(View.INVISIBLE);
                toView.setVisibility(View.VISIBLE);
            }

            if (forward)
                degrees = -degrees; //determines direction of rotation when flip begins

            final Matrix matrix = t.getMatrix();
            camera.save();
            camera.rotateY(degrees);
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }

    public static void animateRevealShowCenter(View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        viewRoot.setBackgroundColor(ContextCompat.getColor(viewRoot.getContext(), R.color.colorAccent));
        anim.setDuration(400);
        anim.setInterpolator(new AccelerateInterpolator());
        viewRoot.setVisibility(View.VISIBLE);

        anim.start();
    }

    public static void animateRevealShow(View viewRoot, View child) {
        int[] location = new int[2];
        child.getLocationOnScreen(location);
        int cx = location[0];
        int cy = location[1];
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        viewRoot.setBackgroundColor(ContextCompat.getColor(viewRoot.getContext(), R.color.colorAccent));
        anim.setDuration(400);
        anim.setInterpolator(new AccelerateInterpolator());
        viewRoot.setVisibility(View.VISIBLE);

        anim.start();
    }

    public static void animateRevealShowParams(View viewRoot, boolean isStart, int color, int x, int y, CallBacks.OnFinish onFinish) {
        int finalRadius;
        int startRadius;
        int duration;
     //   int cx = viewRoot.getRight();
       // int cy = viewRoot.getBottom();
        if (isStart) {
            duration = 250;
            finalRadius = (int) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());
            startRadius = 0;

        } else {
            duration = 250;
            finalRadius = 0;
            startRadius= Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        }


        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, startRadius, finalRadius);
        anim.setDuration(duration);
        anim.setInterpolator(new AccelerateInterpolator());
        if(isStart){
            viewRoot.setBackgroundColor(ContextCompat.getColor(viewRoot.getContext(), color));
            viewRoot.setVisibility(View.VISIBLE);

        }else{
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if(onFinish != null){
                        onFinish.onFinish(1);
                        viewRoot.setBackgroundColor(ContextCompat.getColor(viewRoot.getContext(), color));
                        viewRoot.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }

        anim.start();
    }
}
