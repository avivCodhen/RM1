package com.strongest.savingdata.Fragments;

import android.animation.Animator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AViewModels.ProgramViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.MyViews.WorkoutView.ProgramToolsView;
import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cohen on 3/11/2018.
 */

public class NewProgramFragment extends BaseFragment {

    @BindView(R.id.new_program_frag_saveexittoolbar)
    SaveExitToolBar saveExitToolBar;

    @BindView(R.id.fab_new_program)
            View fab;

    NewProgramFragmentCallBack newProgramFragmentCallBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_program, container, false);
        ButterKnife.bind(this, v);
        v.setVisibility(View.INVISIBLE);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);


    }

    private void initViews(View v) {
        saveExitToolBar
                .instantiate()
                .noElevation()
                .showCancel(false)
                .showBack(true)
                .setOptionalText("Create New Program");

//        programViewModel = ViewModelProviders.of(getActivity()).get(ProgramViewModel.class);
        //      workoutsViewModel= ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);

        v.findViewById(R.id.fragment_new_program_default_blank_template).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    newProgramFragmentCallBack.createNewProgram();
                    /*programViewModel.setNewProgram();
                    workoutsViewModel.setNewWorkout();
                    getFragmentManager().popBackStack();*/
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyJavaAnimator.animateRevealShow(v,fab);
            }
        }, 0);

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        newProgramFragmentCallBack = (NewProgramFragmentCallBack) context;
    }

    public  interface NewProgramFragmentCallBack{

        void createNewProgram();
    }
}
