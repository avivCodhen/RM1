package com.strongest.savingdata.MyViews.WorkoutView.Choose;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.PLObjects.ExerciseProfile;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Fragments.ExerciseChooseFragment;
import com.strongest.savingdata.Fragments.ChooseContainerFragment;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

import java.util.ArrayList;

/**
 * Created by Cohen on 11/16/2017.
 */


class ChooseAdapter extends FragmentPagerAdapter {
    private final ArrayList<PLObjects> layout;
    private int updatedPosition = -1;
    private ArrayList<BaseCreateProgramFragment> fragments = new ArrayList<>();

    public ChooseAdapter(
            FragmentManager fm,
            ArrayList<PLObjects> layout) {

        super(fm);
        this.layout = layout;
        fragments.add(ChooseContainerFragment.getInstance((ExerciseProfile) layout.get(0), 0, ChooseContainerFragment.INTRA_EXERCISE));
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments.size() <= position){
            fragments.add(ChooseContainerFragment.getInstance((ExerciseProfile)layout.get(0), (position-1), ChooseContainerFragment.SET));
           // fragments.add(SetsChooseSingleFragment.getInstance((PLObjects.ExerciseProfile)layout.get(0), (position-1), 0));
            return fragments.get(position);
        }else{
            return fragments.get(position);
        }/*
        if (layout.get(position).getType() == WorkoutLayoutTypes.ExerciseProfile) {
            return ExerciseChooseFragment.newInstance((PLObjects.ExerciseProfile) layout.get(position));
        } else {
            return SetsChooseFragment.getInstance((PLObjects.ExerciseProfile) layout.get(0), (determineSetPosition(position))-1);
        }*/
    }

    @Override
    public int getCount() {
        return layout.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "EX-A";
        }
        if (layout.get(position).getType() == WorkoutLayoutTypes.ExerciseProfile) {
            return "EX-" + ((ExerciseProfile) layout.get(position)).getTag();
        } else {
            return "SET " + determineSetPosition(position);
        }
    }

/*
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
*/

    private int determineSetPosition(int position) {
        return position - ((ExerciseProfile) layout.get(0)).getExerciseProfiles().size();
    }

    public void setUpdatedPosition(int position){
        this.updatedPosition = position;
    }
}