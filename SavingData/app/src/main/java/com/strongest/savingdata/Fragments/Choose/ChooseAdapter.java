package com.strongest.savingdata.Fragments.Choose;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.strongest.savingdata.Fragments.BaseCreateProgramFragment;

/**
 * Created by Cohen on 11/16/2017.
 */


class ChooseAdapter extends FragmentPagerAdapter {
    private final BaseCreateProgramFragment fragment;
    //   private final ArrayList<PLObject> layout;
    private int updatedPosition = -1;

    public ChooseAdapter(
            FragmentManager fm,
            BaseCreateProgramFragment fragment) {

        super(fm);
        this.fragment = fragment;
        // this.layout = layout;
    }

    @Override
    public Fragment getItem(int position) {
       return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

/*
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
*/

    /*private int determineSetPosition(int position) {
        return position - ((ExerciseProfile) layout.get(0)).getExerciseProfiles().size();
    }

    public void setUpdatedPosition(int position){
        this.updatedPosition = position;
    }*/
}