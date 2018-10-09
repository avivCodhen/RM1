package com.strongest.savingdata.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;

import com.strongest.savingdata.AService.ProgramService;
import com.strongest.savingdata.AService.UserService;
import com.strongest.savingdata.AViewModels.ProgramViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModelFactory;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.Controllers.CallBacks;
import com.strongest.savingdata.DependencyInjection.MainApplication;
import com.strongest.savingdata.R;

import javax.inject.Inject;

public abstract class BaseFragment extends Fragment {

    public static final String FRAGMENT_TAG = "tag";

    ProgramViewModel programViewModel;
    WorkoutsViewModel workoutsViewModel;

    @Inject
    WorkoutsViewModelFactory workoutsViewModelFactory;


    @Inject
    UserService userService;


    private View v;
    public int cx;
    public int cy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getActivity().getApplication()).getAppComponent().inject(this);

    }

    public void addFragmentChild(FragmentManager fm, Fragment f, String tag) {
        //  f = ChooseDialogFragment.getInstance(this, oldPosition, position, plObject);
        f.setTargetFragment(this, 0);
        fm.beginTransaction()
                //  .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .add(R.id.activity_home_framelayout, f, tag)
                .addToBackStack(tag)
                .commit();
    }

    public void guard(Object o) {
        if (o == null) {
            getFragmentManager().popBackStack();
        }
    }

    public void setUpMainViewForCircularAnimation(View v) {
        this.v = v;
        this.cx = v.getRight();
        this.cy = v.getBottom();
        v.setVisibility(View.INVISIBLE);
    }

    public void makeRevealAnimation(int delay, CallBacks.OnFinish onFinish) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyJavaAnimator.animateRevealShowParams(v, true, R.color.colorAccent, cx, cy, onFinish);
            }
        }, delay);
    }

    public void exitRevealAnimation(CallBacks.OnFinish onFinish) {
        MyJavaAnimator.animateRevealShowParams(v, false, R.color.colorAccent, 0, 0, onFinish);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
