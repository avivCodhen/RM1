package com.strongest.savingdata.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;

import java.util.ArrayList;

/**
 * Created by Cohen on 6/19/2017.
 */

public class oldWorkoutsAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {

    private final int fragment;
    private Context context;
    private ArrayList<ArrayList<PLObject>> splitRecyclerWorkouts;
    private String[] workouts;
    private boolean editMode;

    public static final int WORKOUT_TAB_FARGMENT = 0, CREATE_FRAGMENT = 1;


    public oldWorkoutsAdapter(ArrayList<ArrayList<PLObject>> splitRecyclerWorkouts, int fragment, FragmentManager fm, Context context, String[] workouts,
                              boolean editMode) {
        super(fm);
        this.splitRecyclerWorkouts = splitRecyclerWorkouts;
        this.workouts = workouts;
        this.fragment = fragment;
        this.context = context;
        this.workouts = workouts;
        this.editMode = editMode;

    }


    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return workouts.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return workouts[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void updateList(ArrayList<ArrayList<PLObject>> splitRecyclerWorkouts) {
        this.splitRecyclerWorkouts = splitRecyclerWorkouts;
    }
}

