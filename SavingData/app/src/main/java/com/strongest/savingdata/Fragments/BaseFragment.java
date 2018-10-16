package com.strongest.savingdata.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

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

import static android.content.Context.INPUT_METHOD_SERVICE;

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
        fm.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.activity_home_framelayout, f, tag)
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

    public void setOnBackPress(View v, CallBacks.Change change) {

        v.setFocusableInTouchMode(true);
        v.requestFocus();

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    change.onChange();
                    return true;
                }
                return false;
            }
        });

    }

    public void makeRevealAnimation(int delay, CallBacks.OnFinish onFinish) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyJavaAnimator.animateRevealShowParams(v, true, R.color.colorAccent, cx, cy, onFinish);
            }
        }, delay);
    }

    public void closeKeyBoardOnClick() {
        getView().setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            return true;
        });

    }


    public void closeKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

    }

    public void exitRevealAnimation(CallBacks.OnFinish onFinish) {
        MyJavaAnimator.animateRevealShowParams(v, false, R.color.colorAccent, 0, 0, onFinish);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
