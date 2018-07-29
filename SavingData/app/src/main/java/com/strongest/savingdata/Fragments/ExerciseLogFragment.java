package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AViewModels.SelectedLogDataViewModel;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Database.LogDataManager;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseLogFragment extends BaseFragment {


    @BindView(R.id.log_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.log_toolbar)
    SaveExitToolBar saveExitToolBar;

    MyExpandableAdapter adapter;

    ArrayList<PLObject> list;
    LogDataManager logDataManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exerciselog, container, false);
        ButterKnife.bind(this, v);
        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SelectedLogDataViewModel selectedLogDataViewModel = ViewModelProviders.of(getActivity()).get(SelectedLogDataViewModel.class);
        list = selectedLogDataViewModel.getSets();
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(lm);
        adapter = new MyExpandableAdapter(list, getContext());
        recyclerView.setAdapter(adapter);
        saveExitToolBar.instantiate();
    }
}
