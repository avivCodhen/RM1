package com.strongest.savingdata.AlgorithmProgress;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.MyViews.WorkoutView.WorkoutView;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.Unused.BaseDialogFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Cohen on 12/16/2017.
 */

public class ProgressDialogFragment extends BaseDialogFragment implements View.OnClickListener {


    private ProgressorManager progressorManager;
    private ArrayList<ArrayList<PLObject>> progressModels = new ArrayList<>();
    private int numOfModels = 1;
    String TAG = "aviv";

    @BindView(R.id.dialog_progress_tablayout)
    public TabLayout tabLayout;
    @BindView(R.id.progress_viewPager)
    public ViewPager viewPager;
    @BindView(R.id.dialog_progress_add_model)
    public Button addModel;
    @BindView(R.id.progress_dialog_save)
    public Button save;
    @BindView(R.id.progress_dialog_delete)
    public Button delete;

    private WorkoutView.WorkoutViewPagerAdapter workoutViewPagerAdapter;
    private ProgressAdapter progressAdapter;
    private ArrayList<PLObject> workout;
    private int position;
    private boolean disable;

    public static ProgressDialogFragment getInstance(ProgressorManager progressorManager) {
        ProgressDialogFragment frag = new ProgressDialogFragment();
        frag.setProgressorManager(progressorManager);
        //    frag.setPosition(position);
        //frag.setWorkout(workout);
        return frag;
    }

    public void setProgressorManager(ProgressorManager progressorManager) {
        this.progressorManager = progressorManager;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x22000000));
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(100);
        getDialog().getWindow().setBackgroundDrawable(d);
        View v = inflater.inflate(R.layout.progress_weight_fragment, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }

    private void initView(View v) {

        progressModels = progressorManager.requestModeList();
        progressAdapter = new ProgressAdapter(getChildFragmentManager());
        viewPager.setAdapter(progressAdapter);
        viewPager.setOffscreenPageLimit(numOfModels - 1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        addModel.setOnClickListener(this);
    }

    @OnClick({R.id.progress_dialog_delete, R.id.progress_dialog_save})
    public void buttonsClick(View v) {
        switch (v.getId()) {
            case R.id.progress_dialog_save:
                progressorManager.deconstructProgressModels(progressModels);
                break;
            case R.id.progress_dialog_delete:
        }
        dismiss();
    }

    public void setWorkout(ArrayList<PLObject> workout) {
        this.workout = workout;
    }

    public ArrayList<PLObject> getWorkout() {
        return workout;
    }

    @Override
    public void onClick(View v) {

        numOfModels++;
        progressModels.add(progressorManager.CreateProgressModel());
        progressAdapter.notifyDataSetChanged();
    }

    public void setPosition(int position) {
        this.position = position;
    }


    private class ProgressAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> list = new ArrayList<>();

        public ProgressAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return numOfModels;
        }


        @Override
        public Fragment getItem(int position) {
            /*if(position > numOfModels){
               numOfModels = position;
                notifyDataSetChanged();
            }*/
            if (position == 0) {
                disable = true;
            } else {
                disable = false;
            }
            //workoutview fragment has been removed
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "phase" + position;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //workout = null;
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
