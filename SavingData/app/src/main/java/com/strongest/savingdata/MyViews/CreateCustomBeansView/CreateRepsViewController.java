package com.strongest.savingdata.MyViews.CreateCustomBeansView;

import android.animation.Animator;
import android.content.Context;
import android.widget.LinearLayout;

import static com.strongest.savingdata.MyViews.CreateCustomBeansView.CreateRepsViewController.Section.Single;

/**
 * Created by Cohen on 1/17/2018.
 */

@Deprecated
public class CreateRepsViewController {

    private Context context;
    private LinearLayout parent;
    private Section mCurrentSection = Single;
    private NumberChooseManager mNumberChooseManager;

    private SingleNumberChooseView mSingleChoose;
    private RangeNumberChooseView mRangeChoose;
    private PyramidNumberChooseView mPyramidChoose;


    public enum Section {
        Single, Range, Pyramid;
        public static Section getValue(int i){
            Section[] arr = Section.values();
            return arr[i];
        }
    }

    public void instantiate(Context context) {
        this.context = context;
        mNumberChooseManager = new NumberChooseManager();
    }

    public void updateSection(int position) {

        }


    private void animateCreateLayout() {
        parent.animate().alpha(1f).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
               // parent.addView(instantiateSection());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void animateDestroyLayout() {
        parent.animate().alpha(0f).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                parent.removeAllViews();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

  /*  private LinearLayout instantiateSection() {
        switch (mCurrentSection) {
            case Single:
                if (mSingleChoose != null) {
                    return mSingleChoose;
                } else {
                    mSingleChoose = new SingleNumberChooseView(context);
                    mSingleChoose.setWithNumberChooseManager(mNumberChooseManager);
                    return mSingleChoose;
                }
            case Range:
                if (mRangeChoose != null) {
                    return mRangeChoose;
                } else {
                    mRangeChoose = new RangeNumberChooseView(context);
                    mRangeChoose.setWithNumberChooseManager(mNumberChooseManager);
                    return mRangeChoose;
                }
            case Pyramid:
                if(mPyramidChoose != null){
                    return mPyramidChoose;
                }else{
                    mPyramidChoose = new PyramidNumberChooseView(context);
                    mPyramidChoose.setWithNumberChooseManager(mNumberChooseManager);
                    return mPyramidChoose;
                }
                default: return null;
        }
    }*/

}
