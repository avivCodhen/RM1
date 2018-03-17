package com.strongest.savingdata.tabFragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Activities.OnCreateProgramListener;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.BaseWorkout.Programmer;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.MyViews.MyViewPager;
import com.strongest.savingdata.MyViews.WorkoutView.WorkoutView;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;
import com.strongest.savingdata.createProgramFragments.CreateProgram.DetailsFragment;
import com.strongest.savingdata.createProgramFragments.CreateProgram.LimitFragment;


import java.util.ArrayList;


import static android.view.View.GONE;

/**
 * Created by Cohen on 4/22/2017.
 */

public class WorkoutFragment extends BaseCreateProgramFragment implements TabLayout.OnTabSelectedListener,
        View.OnClickListener, OnCreateProgramListener {

    public static final String HASWORKOUT = "hasworkout";
    private MyViewPager viewPager;
    // private TabLayout tabLayout;
    //  private oldWorkoutsAdapter adapter;
    private boolean editMode;
    private int editCounter = 0;
    //  private FloatingActionButton fab;
    private int position;

    public DataManager dm;
    public Programmer programmer;
    private WorkoutView workoutView;
    //  private View view;
    private LinearLayout navLayout;
    //  private Button createProgramBtn;
    private LinearLayout mainView;
    private WorkoutFragmentAdapter newAdapter;
    private Button backBtn, saveBtn;
    private boolean toInitiateWorkout = true;
    public static int workoutFragmentHeight;


    private ArrayList<BaseCreateProgramFragment> fragmentsList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = (LinearLayout) inflater.inflate(R.layout.workout, container, false);
//        getActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.aviv_menu_program, menu);
        inflater.inflate(R.menu.progress_menu, menu);
    }


    private void initView(View v) {
        dm = ((HomeActivity) getActivity()).getDependecies();
        navLayout = (LinearLayout) v.findViewById(R.id.work_fragment_navigation_layout);
        backBtn = (Button) v.findViewById(R.id.workout_fragment_back_btn);
        viewPager = (MyViewPager) v.findViewById(R.id.workout_fragment_viewpager);
        newAdapter = new WorkoutFragmentAdapter(getFragmentManager());
        programmer = ((HomeActivity) getActivity()).getProgrammer();
        // saveBtn = (Button) v.findViewById(R.id.workout_fragment_save_btn);
        backBtn.setOnClickListener(this);
        workoutView = (WorkoutView) v.findViewById(R.id.fragment_workout_workoutview);
        /*if (programmer.isHasProgram()) {
            workoutView.instantiate(-1, getChildFragmentManager(), true, programmer.getLayoutManager());
            //toInitiateWorkout = false;
        } else {
            programmer.setLayoutManager(LayoutManager.getDefaultLayoutManagerInstance(getContext(), dm));
            workoutView.instantiate(-1, getChildFragmentManager(), true, programmer.getLayoutManager());

        }*/

    }


    public void createWorkout(View v) {
        if (viewPager != null) {
            viewPager.setVisibility(GONE);
        }
        navLayout.setVisibility(GONE);

        workoutView.setVisibility(View.VISIBLE);

        workoutView.instantiate(-1, getChildFragmentManager(), true, programmer.getLayoutManager());

    }


    public void updateAdapter() {

        createWorkout(mainView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public Resources getResource() {
        return getContext().getResources();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.workout_fragment_back_btn:

                fragmentsList.remove(fragmentsList.size() - 1);
                if (fragmentsList.size() == 1) {
                    if (fragmentsList.get(0) instanceof DetailsFragment) {
                        navLayout.setVisibility(GONE);
                    }
                } else {
                    navLayout.setVisibility(View.VISIBLE);
                }
                newAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(fragmentsList.size() - 1);

                if (fragmentsList.size() == 0) {
                    createWorkout(mainView);
                }
                break;

            /*case R.id.workout_fragment_create_program_btn:
                // mainView.setVisibility(GONE);
                switchFragment(new DetailsFragment(), null);*/

            //createProgramListener.createProgramUI();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BooleanEventHolder booleanEventHolder = new BooleanEventHolder();
        switch (item.getItemId()) {
            case R.id.edit_menu:
               /* ++editCounter;
                if (editCounter % 2 == 0) {
                    editMode = false;
                    item.setIcon(R.drawable.ic_edit_white_18dp);
                    //WorkoutsTabFragment f = (WorkoutsTabFragment) adapter.instantiateItem(viewPager, tabLayout.getSelectedTabPosition());
                    //f.notifyAdapter();
                    //initAdapter();
                    booleanEventHolder.setShowStats(false);
                    EventBus.getDefault().post(booleanEventHolder);

                } else {
                    editMode = true;
                    item.setIcon(R.drawable.ic_edit_black_18dp);
                    //initAdapter();
                    booleanEventHolder.setShowStats(true);
                    EventBus.getDefault().post(booleanEventHolder);

                }*/


                break;
            case R.id.progress_menu:

        }
        return true;
    }

    public void switchToCreateProgram() {
        if (dm.getPrefs().getBoolean(HASWORKOUT, true)) {

        }
        fragmentsList.add(LimitFragment.getInstance(this));
        //fragmentsList.add(new ManagerView());
        newAdapter.notifyDataSetChanged();
        workoutView.setVisibility(GONE);
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setAdapter(newAdapter);
        viewPager.setCurrentItem(fragmentsList.size() - 1);
        navLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        position = tab.getPosition();

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        // getPrefsEditor().putBoolean("animate", true).commit();

        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        //dm.closeDataBases();
        super.onDetach();

    }

    @Override
    public void createProgramUI(BaseCreateProgramFragment f) {
        navLayout.setVisibility(View.VISIBLE);
        if (f instanceof WorkoutFragment) {
            while (fragmentsList.size() != 0) {
                fragmentsList.remove(0);
                getFragmentManager().popBackStack();
            }
            newAdapter.notifyDataSetChanged();
            //fragmentsList.add(LimitFragment.getInstance(this));
            createWorkout(mainView);
        } else {

            fragmentsList.add(f);
            newAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(fragmentsList.size() - 1);
        }

    }

    private class WorkoutFragmentAdapter extends FragmentStatePagerAdapter {

        public WorkoutFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            int index = fragmentsList.indexOf(object);

            if (index == -1) {
                return POSITION_NONE;
            } else {
                return index;
            }
        }
    }

}
