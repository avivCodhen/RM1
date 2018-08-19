package com.strongest.savingdata.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.strongest.savingdata.AService.ProgramService;
import com.strongest.savingdata.AViewModels.ProgramViewModel;
import com.strongest.savingdata.DependencyInjection.MainApplication;
import com.strongest.savingdata.R;

import javax.inject.Inject;

public class BaseFragment extends Fragment {

    public static final String FRAGMENT_TAG = "tag";

    ProgramViewModel programViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getActivity().getApplication()).getAppComponent().inject(this);

    }

    public void addFragmentChild(FragmentManager fm, Fragment f) {
        //  f = ChooseDialogFragment.getInstance(this, oldPosition, position, plObject);
        f.setTargetFragment(this, 0);
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .add(R.id.activity_home_framelayout, f, "unique")
                .addToBackStack("unique")
                .commit();
    }
}
