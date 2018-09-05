package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.strongest.savingdata.AViewModels.ProgramViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.MyViews.WorkoutView.ProgramToolsView;
import com.strongest.savingdata.R;

/**
 * Created by Cohen on 3/11/2018.
 */

public class NewProgramFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_program, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

    }

    private void initViews(View v) {

        programViewModel = ViewModelProviders.of(getActivity()).get(ProgramViewModel.class);
        workoutsViewModel= ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);

        v.findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        v.findViewById(R.id.fragment_new_program_default_blank_template).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userService.isUserLoggedIn() || programViewModel.getProgram().getValue() == null) {

                    programViewModel.setNewProgram();
                    workoutsViewModel.setNewWorkout();
                    getFragmentManager().popBackStack();
                }else{
                    Toast.makeText(getContext(), "You can only save one prgraom. Log in to save as many as you wish.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ( (TextView)v.findViewById(R.id.tool_bar_title)).setText("Create New Program");


    }
}
