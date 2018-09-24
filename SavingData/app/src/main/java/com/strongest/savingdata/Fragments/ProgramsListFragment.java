package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AViewModels.MyProgramsViewModel;
import com.strongest.savingdata.Activities.MyProgramsActivity;
import com.strongest.savingdata.Adapters.MyProgramsAdapter;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.Handlers.MaterialDialogHandler;
import com.strongest.savingdata.MyViews.SmartEmptyView;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgramsListFragment extends BaseFragment implements Architecture.program {

    private ArrayList<Program> programs = new ArrayList<>();
    private MyProgramsAdapter myProgramsAdapter;
    private MyProgramCallBack myProgramCallBack;


    @BindView(R.id.fragment_my_program_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.program_list_smartemptyview)
    SmartEmptyView smartEmptyView;
    MyProgramsViewModel myProgramsViewModel;

    Program currentProgram;
    String tag;
    boolean isShared;


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

        currentProgram = myProgramCallBack.getCurrentProgram();


        myProgramsViewModel = ViewModelProviders.of(this, workoutsViewModelFactory)
                .get(tag, MyProgramsViewModel.class);
        isShared = false;
        myProgramsAdapter = new MyProgramsAdapter(programs, currentProgram, this, isShared, tag);
        recyclerView.setAdapter(myProgramsAdapter);

        smartEmptyView
                .setUpWithRecycler(recyclerView, true, false)
                .setButtonText("Create A New Program")
                .setTitle("You don't have any programs saved.")
                .setBody("You can click on the Plus Icon to create a new Program")
                .setButtonText("Or Tap here")
                .setImage(smartEmptyView.getDocImage())
                .setActionBtn(view -> myProgramCallBack.createProgram());

        myProgramsViewModel.fetchProgramList(tag);
        myProgramsViewModel.getProgramList().observe(this, list -> {
            Log.d("aviv", "ProgramList : "+tag);
            programs = list;
            if (tag.equals(MyProgramsActivity.FRAGMENT_USER_SHARED_FOR)) {
                isShared = true;
                myProgramsAdapter.setShared(isShared);
            }
            if (tag.equals(MyProgramsActivity.FRAGMENT_USER_PROGRAMS) && list.size() == 0 && currentProgram != null) {
                programs.add(currentProgram);
            }

            if (tag.equals(MyProgramsActivity.FRAGMENT_USER_SHARED_FOR) || tag.equals(MyProgramsActivity.FRAGMENT_USER_SHARED_BY)) {
                if (!userService.isUserLoggedIn()) {
                    smartEmptyView.setTitle("You are not logged in.")
                            .setBody("Log in to send and recieve programs from other users.")
                            .setButtonText("Log In")
                            .setImage(smartEmptyView.getLogInImage())
                            .setActionBtn(view->myProgramCallBack.logIn());
                }else if(tag.equals(MyProgramsActivity.FRAGMENT_USER_SHARED_FOR)){
                    smartEmptyView.setTitle("You haven't shared any programs.")
                            .setImage(smartEmptyView.getDocImage())
                            .setBody("Don't be shy. Share a program and make a trainee happy!")
                            .noButton();
                }else if(tag.equals(MyProgramsActivity.FRAGMENT_USER_SHARED_BY )){
                    smartEmptyView.setTitle("No one has shared a program with you yet.")
                            .setBody("Don't worry. We are sure someone will share a program with you!")
                            .setImage(smartEmptyView.getDocImage())
                            .noButton();
                }

            }

            myProgramsAdapter.setList(programs);
            myProgramsAdapter.notifyDataSetChanged();

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
        MaterialDialogHandler
                .get()
                .defaultDeleteBuilder(getContext(),
                        "Delete This Program?"
                        ,
                        "DELETE")
                .buildDialog()
                .addPositiveActionFunc(v -> {
                    int pos = programs.indexOf(p);
                    programs.remove(pos);
                    myProgramsAdapter.notifyItemRemoved(pos);
                    myProgramCallBack.deleteProgram(p);

                }, true).show();


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

        void createProgram();

        void logIn();
    }
}
