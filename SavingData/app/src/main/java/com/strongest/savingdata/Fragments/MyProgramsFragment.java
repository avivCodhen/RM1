package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strongest.savingdata.AService.WorkoutsService;
import com.strongest.savingdata.AViewModels.ProgramViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cohen on 3/9/2018.
 */

public class MyProgramsFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private Program currentProgram;
   // private MyProgramsAdapter myProgramsAdapter;

    @BindView(R.id.my_programs_toolbar)
    SaveExitToolBar saveExitToolBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_program, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View v) {

        programViewModel = ViewModelProviders.of(getActivity()).get(ProgramViewModel.class);
        workoutsViewModel = ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);
        currentProgram = programViewModel.getProgram().getValue();
        initStaticViews(v);


        /*programViewModel.fetchAllPrograms();
        programViewModel.getAllPrograms().observe(this, (progList) -> {
           *//* programs = progList;
            myProgramsAdapter = new MyProgramsAdapter();
            recyclerView.setAdapter(myProgramsAdapter);*//*
        });*/
        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_my_program_recyclerview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(lm);

        saveExitToolBar.instantiate();
        saveExitToolBar.showBack(true);
        saveExitToolBar.setOptionalText("My Programs");
        saveExitToolBar.setSaveButton(clicked -> getFragmentManager().popBackStack());
    }

    private void initStaticViews(View v) {
        View includeLeftMarginProgram = v.findViewById(R.id.my_program_view);
        TextView programTitle = (TextView) includeLeftMarginProgram.findViewById(R.id.program_name);
        TextView programTimeAndDate = (TextView) includeLeftMarginProgram.findViewById(R.id.program_date);


        programTitle.setText(currentProgram.getProgramName());
        programTimeAndDate.setText(currentProgram.getProgramDate() + ", " + currentProgram.getTime());
    }

}
