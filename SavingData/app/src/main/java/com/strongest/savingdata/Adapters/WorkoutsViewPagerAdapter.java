package com.strongest.savingdata.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.Fragments.WorkoutViewFragment;

import java.util.ArrayList;

public class WorkoutsViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Workout> workoutsList;
    private FragmentManager supportFragmentManager;
    public WorkoutsViewPagerAdapter(FragmentManager supportFragmentManager,

                                    ArrayList<Workout> workoutsList) {
        super(supportFragmentManager);
        this.supportFragmentManager = supportFragmentManager;
        this.workoutsList = workoutsList;
    }

    @Override
    public int getCount() {
        return workoutsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return workoutsList.get(position).workoutName;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        WorkoutViewFragment frag = null;
        frag = WorkoutViewFragment.getInstance(position);
        // frag.setOnUpdateLayoutStatsListener(onUpdateListener);
        return frag;
    }

    /*@Override
    public Object instantiateItem(ViewGroup container, int position) {
        WorkoutViewFragment f = (WorkoutViewFragment) super.instantiateItem(container, position);
        if (supportFragmentManager.getFragments().size() <= position) {
            supportFragmentManager.getFragments().add(position, f);
        } else {
            supportFragmentManager.getFragments().set(position, f);
        }
        return f;
    }*/

    public void setWorkoutsList(ArrayList<Workout> workoutsList) {
        this.workoutsList = workoutsList;
    }
}
