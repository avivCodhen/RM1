package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AViewModels.MyProgramsViewModel;
import com.strongest.savingdata.Activities.MyProgramsActivity;
import com.strongest.savingdata.Adapters.MyProgramsAdapter;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgramsListFragment extends BaseFragment implements Architecture.program {

    private List<Program> programs;
    private MyProgramsAdapter myProgramsAdapter;
    private MyProgramCallBack myProgramCallBack;


    @BindView(R.id.fragment_my_program_recyclerview)
    RecyclerView recyclerView;

    MyProgramsViewModel myProgramsViewModel;

    Program currentProgram;
    String tag;

    public static ProgramsListFragment getInstance(String tag) {
        ProgramsListFragment f = new ProgramsListFragment();
        Bundle b = new Bundle();
        b.putString("tag", tag);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tag = getArguments().getString("tag");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_programs_list, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        recyclerView = v.findViewById(R.id.fragment_my_program_recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        myProgramsViewModel = ViewModelProviders.of(this, workoutsViewModelFactory)
                .get(tag, MyProgramsViewModel.class);
        currentProgram = myProgramCallBack.getCurrentProgram();
        myProgramsViewModel.fetchProgramList(tag);
        myProgramsViewModel.getProgramList().observe(this, list -> {
            programs = list;
            boolean isShared = false;
            if(tag.equals(MyProgramsActivity.FRAGMENT_USER_SHARED_FOR)){
                isShared = true;
            }
            myProgramsAdapter = new MyProgramsAdapter(list, currentProgram, this, isShared);
            recyclerView.setAdapter(myProgramsAdapter);
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            myProgramCallBack = (MyProgramCallBack) context;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    @Override
    public void deleteProgram(Program p) {
        int pos = programs.indexOf(p);
        programs.remove(pos);
        myProgramsAdapter.notifyItemRemoved(pos);
        myProgramCallBack.deleteProgram(p);
    }

    @Override
    public void switchProgram(Program p) {

    }

    @Override
    public void shareProgram(Program p) {
        myProgramCallBack.shareProgram(p);
    }

    @Override
    public void loadProgram(Program p) {
        myProgramCallBack.onLoadProgram(p);
    }

    @Override
    public void loadSharedProgram(Program p) {
        myProgramCallBack.loadSharedProgram(p);
    }

    public interface MyProgramCallBack {

        void onLoadProgram(Program p);

        Program getCurrentProgram();

        void deleteProgram(Program p);

        void shareProgram(Program p);

        void loadSharedProgram(Program p);
    }
}
