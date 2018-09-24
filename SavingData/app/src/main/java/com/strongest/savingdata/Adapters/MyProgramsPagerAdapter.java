package com.strongest.savingdata.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.strongest.savingdata.Activities.MyProgramsActivity;
import com.strongest.savingdata.Fragments.BaseFragment;
import com.strongest.savingdata.Fragments.MyProgramsFragment;
import com.strongest.savingdata.Fragments.ProgramsListFragment;

import java.util.ArrayList;

public class MyProgramsPagerAdapter extends FragmentStatePagerAdapter {


    private final String[] fragmentsTitles;
    private final ArrayList<BaseFragment> fragments;

    public MyProgramsPagerAdapter(FragmentManager fm, String[] fragmentsTitles, ArrayList<BaseFragment> fragments) {
        super(fm);

        this.fragmentsTitles = fragmentsTitles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentsTitles[position];
    }
}
