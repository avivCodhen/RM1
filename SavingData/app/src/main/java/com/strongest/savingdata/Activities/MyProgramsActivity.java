package com.strongest.savingdata.Activities;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AViewModels.MyProgramsViewModel;
import com.strongest.savingdata.AViewModels.ProgramViewModel;
import com.strongest.savingdata.Adapters.MyProgramsPagerAdapter;
import com.strongest.savingdata.Fragments.ProgramsListFragment;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProgramsActivity extends BaseActivity implements ProgramsListFragment.MyProgramCallBack{

    @BindView(R.id.my_programs_toolbar)
    SaveExitToolBar saveExitToolBar;

    @BindView(R.id.my_program_viewpager)
    ViewPager viewPager;

    @BindView(R.id.my_programs_tablayout)
    SlidingTabLayout tabLayout;

    MyProgramsPagerAdapter myProgramsPagerAdapter;


    MyProgramsViewModel myProgramsViewModel;
    public static final String FRAGMENT_USER_PROGRAMS = "My Programs";
    public static final String FRAGMENT_USER_SHARED_FOR = "Shared For";
    public static final String FRAGMENT_USER_SHARED_BY = "Shared By";


    Program currentProgram;

    private String[] fragmentsTitles = {
            MyProgramsActivity.FRAGMENT_USER_PROGRAMS,
            MyProgramsActivity.FRAGMENT_USER_SHARED_BY,
            MyProgramsActivity.FRAGMENT_USER_SHARED_FOR,
    };
    private ArrayList<ProgramsListFragment> fragments;

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
        saveExitToolBar.setSaveButton(clicked -> finish());
        populateFragments();
        myProgramsPagerAdapter = new MyProgramsPagerAdapter(getSupportFragmentManager(), fragmentsTitles, fragments);
        viewPager.setAdapter(myProgramsPagerAdapter);
        tabLayout.setViewPager(viewPager);

        programService.listenForSharedPrograms(count -> {
            tabLayout.showMsg(1, (int) count);
        });
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
}
