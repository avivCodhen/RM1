package com.strongest.savingdata.Fragments;

import android.animation.Animator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AViewModels.ProgramViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.DefaultProgramsRecycAdapter;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.Database.Managers.DefaultWorkoutsDataManager;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.MyViews.WorkoutView.ProgramToolsView;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cohen on 3/11/2018.
 */

public class NewProgramFragment extends BaseFragment implements DefaultProgramsRecycAdapter.OnDefaultWorkoutClick {

    @BindView(R.id.new_program_frag_saveexittoolbar)
    SaveExitToolBar saveExitToolBar;

    @BindView(R.id.fab_new_program)
    View fab;
    View mainView;

    NewProgramFragmentCallBack newProgramFragmentCallBack;

    @BindView(R.id.new_program_recycler)
    RecyclerView recyclerView;

    DefaultProgramsRecycAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_program, container, false);
        ButterKnife.bind(this, v);
        mainView = v;

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
                .setSaveButton(view -> MyJavaAnimator.animateRevealShowParams(v, false, R.color.colorAccent, 0,0, r -> getFragmentManager().popBackStack()))
                .setOptionalText("Create New Program");
        ArrayList<String> list = new ArrayList();
        list.add(DefaultWorkoutsDataManager.DEFAULT);
        list.add(DefaultWorkoutsDataManager.FBW_TEMPLATE);
        list.add(DefaultWorkoutsDataManager.AB_TEMPLATE);
        list.add(DefaultWorkoutsDataManager.ABC_DROPSET_strength_TEMPLATE);
        adapter = new DefaultProgramsRecycAdapter(list, this);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyJavaAnimator.animateRevealShowParams(v, true, R.color.colorAccent, fab.getWidth(), fab.getHeight(), null);
            }
        }, 0);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        newProgramFragmentCallBack = (NewProgramFragmentCallBack) context;
    }

    @Override
    public void onDefaultClick(String s) {
        newProgramFragmentCallBack.createProgram(s);

    }

    public interface NewProgramFragmentCallBack {
        void createProgram(String workoutName);
    }
}
