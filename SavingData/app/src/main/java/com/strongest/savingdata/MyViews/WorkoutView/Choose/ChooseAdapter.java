package com.strongest.savingdata.MyViews.WorkoutView.Choose;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Cohen on 11/16/2017.
 */


class ChooseAdapter extends FragmentStatePagerAdapter {

    private final ChooseData data;
    private ChooseDataListener chooseDataListener;
    private final MandatoryChooseDialogFragment manFrag;
    private final OptionalChooseDialogFragment opFrag;

    public ChooseAdapter(FragmentManager fm, ChooseData data, ChooseDataListener listener,
                         MandatoryChooseDialogFragment manFrag, OptionalChooseDialogFragment opFrag) {
        super(fm);
        this.data = data;
        this.chooseDataListener = listener;
        this.manFrag = manFrag;
        this.opFrag = opFrag;
    }

    @Override
    public Fragment getItem(int position) {

        return position == 0 ? manFrag : opFrag;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Simple Build" : "Custom Build";
    }
}