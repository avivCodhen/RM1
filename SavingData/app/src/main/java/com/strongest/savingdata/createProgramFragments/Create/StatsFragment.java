package com.strongest.savingdata.createProgramFragments.Create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;
import com.strongest.savingdata.createProgramFragments.Unused.UpdateFragmentData;


public class StatsFragment extends BaseCreateProgramFragment implements UpdateFragmentData {

    private CircleProgressBar metabolicProgress;
    private CircleProgressBar mechanicalProgress;

    public static StatsFragment newInstance(int met, int mec) {

        Bundle args = new Bundle();
        args.putInt(METABOLIC_VALUE, met);
        args.putInt(MECHANICAL_VALUE, mec);
        StatsFragment fragment = new StatsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(false);
        getActionBar().hide();
        //  getActionBar().setDisplayHomeAsUpEnabled(false);
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {

    }

    @Override
    public void update() {

    }
}
