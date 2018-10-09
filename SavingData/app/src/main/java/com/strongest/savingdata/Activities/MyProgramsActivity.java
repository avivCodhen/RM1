package com.strongest.savingdata.Activities;

import android.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AViewModels.MyProgramsViewModel;
import com.strongest.savingdata.Adapters.MyProgramsPagerAdapter;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.Fragments.BaseFragment;
import com.strongest.savingdata.Fragments.NewProgramFragment;
import com.strongest.savingdata.Fragments.ProgramsListFragment;
import com.strongest.savingdata.Handlers.MaterialDialogHandler;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.R;
import com.strongest.savingdata.Utils.MyUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProgramsActivity extends BaseActivity implements
        ProgramsListFragment.MyProgramCallBack, NewProgramFragment.NewProgramFragmentCallBack {

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
    //public static final String FRAGMENT_USER_SHARED_FOR = "Shared";
    public static final String FRAGMENT_USER_SHARED_BY = "Recieved";
    public static final int FRAGMENT_CREATE_PROGRAM = 3;
    public static final String WHICH_PROGRAM = "which_program";

    public static final int LOG_IN = 4;


    Program currentProgram;

    private String[] fragmentsTitles = {
            MyProgramsActivity.FRAGMENT_USER_PROGRAMS,
            MyProgramsActivity.FRAGMENT_USER_SHARED_BY,
            /*  MyProgramsActivity.FRAGMENT_USER_SHARED_FOR,*/
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

        listenForShares();

        if (!MyUtils.isNetworkConnected(this)) {
            MaterialDialogHandler.get()
                    .defaultBuilder(this, "No Internet Connection", "OK")
                    .addContent("Without internet connection, we cannot fetch your data.")
                    .hideNegativeButton()
                    .buildDialog().show();
        }

        floatingActionButton.setOnClickListener(v -> {
            toNewProgram();
            MyUtils.Interface.disableClick(v, 2000);
        });
    }

    private void toNewProgram() {
        addFragmentToActivityNoTransition(R.id.my_program_framelayout, new NewProgramFragment(), "newProgram");
    }

    @Override
    public void onBackPressed() {

        android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentByTag("newProgram");
        if (f != null) {
            View v = f.getView();
            MyJavaAnimator.animateRevealShowParams(v, false, R.color.background_color, 0, 0, r -> {
                super.onBackPressed();
                return;
            });
        } else {
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

    private void listenForShares() {
        programService.listenForSharedPrograms(count -> {
            int c = (int) count;
            if (c > 0) {
                tabLayout.showMsg(1, c);
                tabLayout.setMsgMargin(2, 73, 0);
            }
        });
    }

    @Override
    public void onLoadProgram(Program p) {
        if (p.equals(currentProgram)) {
            MaterialDialogHandler.get()
                    .defaultBuilder(this, "Program already loaded.", "")
                    .buildDialog()
                    .show();
        }else{
            Intent i = new Intent(this, HomeActivity.class);
            i.putExtra("program", p);
            setResult(RESULT_OK, i);
            finish();
        }

    }

    @Override
    public Program getCurrentProgram() {
        return currentProgram;
    }

    @Override
    public void deleteProgram(Program p) {
       if(p.equals(currentProgram)){
           currentProgram = null;
       }
        programService.deleteProgram(p);
        workoutsService.deleteWorkout(p.getKey());

    }

    @Override
    public void shareProgram(Program p) {
        if (userService.isUserLoggedIn()) {

            Intent i;
            i = new Intent(this, ShareProgramActivity.class);
            i.putExtra("programuid", p.getKey());
            i.putExtra("program", p);
            startActivity(i);
        } else {
            Toast.makeText(this, "You must be logged in to share.", Toast.LENGTH_SHORT).show();
        }
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
        toNewProgram();
    }

    @Override
    public void logIn() {
        setResult(LOG_IN);
        finish();
    }

    @Override
    public void seen(Program p) {
        programService.updateSeenOnSharedProgram(p);
        listenForShares();
    }

    @Override
    public void createProgram(String s) {
        if (userService.isUserLoggedIn() || currentProgram == null) {
            Intent i = new Intent();
            i.putExtra(WHICH_PROGRAM, s);
            setResult(FRAGMENT_CREATE_PROGRAM,i);
            finish();


        } else {
            Toast.makeText(this, "You can only save one program. Log in to save as many as you wish.", Toast.LENGTH_LONG).show();

        }
    }
}
