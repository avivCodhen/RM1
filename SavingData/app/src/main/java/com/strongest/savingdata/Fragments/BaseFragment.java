package com.strongest.savingdata.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.strongest.savingdata.R;

public class BaseFragment extends Fragment {

    public static final String FRAGMENT_TAG = "tag";

    public void addFragmentChild(FragmentManager fm, Fragment f){
      //  f = ChooseDialogFragment.getInstance(this, oldPosition, position, plObject);
        f.setTargetFragment(this, 0);
        fm.beginTransaction()
                .setCustomAnimations(R.anim.fad_in, R.anim.slide_out_down, R.anim.fad_in, R.anim.fad_out)
                .replace(R.id.activity_home_framelayout, f, "unique")
                .addToBackStack("unique")
                .commit();
    }
}
