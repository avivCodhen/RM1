package com.strongest.savingdata.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.MyViews.MyViewPager;
import com.strongest.savingdata.R;

import static com.strongest.savingdata.Fragments.SetsChooseSingleFragment.SET_POS;

/**
 * Created by Cohen on 3/13/2018.
 */

public class ChooseContainerFragment extends BaseCreateProgramFragment {

    public static final String TYPE = "type";
    public static final String INTRA_EXERCISE = "intra_exercise", SET = "set";

    private MyViewPager myViewPager;
    private TabLayout mTabLayout;

    private PLObject.ExerciseProfile exerciseProfile;
    private int pos;
    private SetsChooseAdapter adapter;
    private String type;


    public static ChooseContainerFragment getInstance(PLObject.ExerciseProfile ep, int pos, String type) {
        ChooseContainerFragment f = new ChooseContainerFragment();
        Bundle b = new Bundle();
        b.putSerializable(ExerciseEditFragment.EXERCISE_PROFILE, ep);
        b.putInt(SET_POS, pos);
        b.putSerializable(TYPE, type);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseProfile = (PLObject.ExerciseProfile) getArguments().getSerializable(ExerciseEditFragment.EXERCISE_PROFILE);
            pos = getArguments().getInt(SET_POS);
            type = getArguments().getString(TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_container, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

    }

    private void initViews(View v) {
        //reactLayoutManager = ((ChooseDialogFragment) getParentFragment()).getmReactLayoutManager();
        myViewPager = (MyViewPager) v.findViewById(R.id.fragment_sets_choose_myviewpager);
        mTabLayout = (TabLayout) v.findViewById(R.id.fragment_sets_choose_tablayout);
        mTabLayout.setupWithViewPager(myViewPager);
        adapter = new SetsChooseAdapter(getChildFragmentManager(), type);
        myViewPager.setAdapter(adapter);
    }


    public class SetsChooseAdapter extends FragmentStatePagerAdapter {

        private final String type;

        public SetsChooseAdapter(FragmentManager fm, String type) {
            super(fm);
            this.type = type;
        }

        @Override
        public Fragment getItem(int position) {
           /* if(type.equals(INTRA_EXERCISE)){

                return ExerciseChooseFragment.newInstance(exerciseProfile);
            }else if(type.equals(SET)){
                return SetsChooseSingleFragment.getInstance(exerciseProfile, pos, 1);
            }*/
            return null;
        }

        @Override
        public int getCount() {
            if(type.equals(INTRA_EXERCISE)){
                return exerciseProfile.exerciseProfiles.size()+1;
            }else if(type.equals(SET)){
              //  return exerciseProfile.getSets().get(pos).getIntraSets().size();
            }
            return -1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "SET" + (pos +1) : "IS-" + position;
        }
    }

}
