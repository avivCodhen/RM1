package com.strongest.savingdata.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.R;

/**
 * Created by Cohen on 3/11/2018.
 */

public class NewProgramFragment extends BaseCreateProgramFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_program, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        getActionBar().show();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews(View v) {

        v.findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        v.findViewById(R.id.fragment_new_program_default_blank_template).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).programmer.resetProgram();
                ((HomeActivity)getActivity()).programmer.setToCreateProgram(true);
                ((HomeActivity)getActivity()).finish();
                ((HomeActivity)getActivity()).startActivity( ((HomeActivity)getActivity()).getIntent());
                getFragmentManager().popBackStack();

            }
        });

        ( (TextView)v.findViewById(R.id.tool_bar_title)).setText("Create New Program");


    }
}
