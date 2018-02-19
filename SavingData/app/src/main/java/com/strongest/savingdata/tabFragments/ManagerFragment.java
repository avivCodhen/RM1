package com.strongest.savingdata.tabFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Manager.ManagerView;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

/**
 * Created by Cohen on 4/22/2017.
 */

public class ManagerFragment extends BaseCreateProgramFragment {

    private ManagerView mManagerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_manager, container, false);
        View v = inflater.inflate(R.layout.fragment_manager, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
       /* mManagerView.instantiate(((HomeActivity) getActivity()).getDataManager(),
                ManagerView.ViewType.Reps,
                ManagerView.ViewType.Rest);*/

    }
}
