package com.strongest.savingdata.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AViewModels.MyProgramsViewModel;
import com.strongest.savingdata.Adapters.MyProgramsPagerAdapter;
import com.strongest.savingdata.Fragments.BaseFragment;
import com.strongest.savingdata.Fragments.NewProgramFragment;
import com.strongest.savingdata.Fragments.ProgramsListFragment;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProgramsActivity extends BaseActivity implements
        ProgramsListFragment.MyProgramCallBack, NewProgramFragment.NewProgramFragmentCallBack{

    @BindView(R.id.my_programs_toolbar)
    SaveExitToolBar saveExitToolBar;

    @BindView(R.id.my_program_viewpager)
    ViewPager viewPager;

    @BindView(R.id.my_programs_tablayout)
    SlidingTabLayout tabLayout;

    MyProgramsPagerAdapter myProgramsPagerAdapter;

    @BindView(R.id.my_program_activity_fab)
    FloatingActionButton floatingActionButton;

    MyProgramsViewModel myProgramsViewModel;
    public static final String FRAGMENT_USER_PROGRAMS = "Mine";
    public static final String FRAGMENT_USER_SHARED_FOR = "Shared";
    public static final String FRAGMENT_USER_SHARED_BY = "Recieved";
    public static final int FRAGMENT_CREATE_PROGRAM = 3;


    Program currentProgram;

    private String[] fragmentsTitles = {
            MyProgramsActivity.FRAGMENT_USER_PROGRAMS,
            MyProgramsActivity.FRAGMENT_USER_SHARED_BY,
            MyProgramsActivity.FRAGMENT_USER_SHARED_FOR,
    };
    private ArrayList<BaseFragment> fragments;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("program", currentProgram);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_programs);
        ButterKnife.bind(this);
        currentProgram = (Program) getIntent().getSerializableExtra("current_program");
        saveExitToolBar.instantiate();
        saveExitToolBar.showBack(true);
        saveExitToolBar.showCancel(false);
        saveExitToolBar.setOptionalText("My Programs");
        saveExitToolBar.setSaveButton(clicked -> onBackPressed());
        saveExitToolBar.noElevation();
        populateFragments();
        myProgramsPagerAdapter = new MyProgramsPagerAdapter(getSupportFragmentManager(), fragmentsTitles, fragments);
        viewPager.setAdapter(myProgramsPagerAdapter);
        tabLayout.setViewPager(viewPager);

        programService.listenForSharedPrograms(count -> {
            tabLayout.showMsg(2, (int) count);
            tabLayout.setMsgMargin(2,43,0);
        });

        if (currentProgram == null) {
            viewPager.setCurrentItem(fragmentsTitles.length - 1);
        }

        floatingActionButton.setOnClickListener(v -> {
            addFragmentToActivityNoTransition(R.id.my_program_framelayout, new NewProgramFragment(), "newProgram");
        });
    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    private void populateFragments() {
        fragments = new ArrayList<>();
        ArrayList<Program> programs;
        for (int i = 0; i < fragmentsTitles.length; i++) {

            fragments.add(ProgramsListFragment.getInstance(fragmentsTitles[i]));
            MyProgramsViewModel programsViewModel = ViewModelProviders.of(this, workoutsViewModelFactory)
                    .get(fragmentsTitles[i], MyProgramsViewModel.class);
            programsViewModel.setCurrentProgram(currentProgram);

        }
    }


    @Override
    public void onLoadProgram(Program p) {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("program", p);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public Program getCurrentProgram() {
        return currentProgram;
    }

    @Override
    public void deleteProgram(Program p) {
        programService.deleteProgram(p);
        workoutsService.deleteWorkout(p.getKey());
    }

    @Override
    public void shareProgram(Program p) {
        Intent i;
        i = new Intent(this, ShareProgramActivity.class);
        i.putExtra("programuid", p.getKey());
        i.putExtra("program", p);
        startActivity(i);
    }

    @Override
    public void loadSharedProgram(Program p) {
        String originalKey = p.getKey();
        Program duplicatedProgram = programService.duplicateProgram(p);
        workoutsService.duplicateWorkouts(originalKey, duplicatedProgram.getKey(), noResult -> {
            onLoadProgram(p);
        });
    }

    @Override
    public void createProgram() {
        createNewProgram();
    }

    @Override
    public void createNewProgram() {
        setResult(FRAGMENT_CREATE_PROGRAM);
        finish();
    }
}
