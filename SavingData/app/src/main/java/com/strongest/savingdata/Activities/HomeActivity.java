package com.strongest.savingdata.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;
import com.strongest.savingdata.AModels.WorkoutItemAdapterFactory;
import com.strongest.savingdata.AViewModels.SelectedExerciseViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.DividerItemAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.ExerciseItemAdapter;
import com.strongest.savingdata.Adapters.WorkoutsViewPagerAdapter;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.BaseWorkout.Program;
import com.strongest.savingdata.Fragments.CustomExerciseFragment;
import com.strongest.savingdata.Fragments.ExerciseDetailsFragment;
import com.strongest.savingdata.Fragments.MyProgramsFragment;
import com.strongest.savingdata.Fragments.NewProgramFragment;
import com.strongest.savingdata.Fragments.ProgramSettingsFragment;
import com.strongest.savingdata.Fragments.RegisterFragment;
import com.strongest.savingdata.Fragments.WorkoutViewFragment;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.MyViews.WorkoutView.ProgramToolsView;
import com.strongest.savingdata.R;
import com.strongest.savingdata.ViewHolders.ExerciseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import youruserpools.MainActivity;

import static com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel.Actions.Advanced;
import static com.strongest.savingdata.Activities.TutorialActivity.FIRSTVISIT;
// import com.roughike.bottombar.OnMenuTabClickListener;

public class HomeActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        WorkoutViewFragment.WorkoutViewFragmentListener,
        Architecture.view.LongClickView,
        Architecture.view.ProgramTools,
        ProgramSettingsFragment.OnProgramSettingsChange {

    // public WorkoutsModelController workoutsModelController;

    public static final int EXERCISE_ACTIVITY = 1;
    public static final int LOGIN_ACTIVITY = 2;
    public static final String EXERCISE_POSITION = "exercisePosition";

    @BindView(R.id.activity_home_toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_home_app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.activity_home_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.home_activity_navigationview)
    NavigationView mNavigationView;
    @BindView(R.id.activity_home_longclick_menu)
    LongClickMenuView longClickMenuView;
    @BindView(R.id.activity_home_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.activity_home_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.activity_programtoolsview)
    ProgramToolsView programToolsView;
    @BindView(R.id.toolbar_title)
    public TextView title;


    //this view is the edit "+" button of the toolbar menu
    //it will be setuped with the programtoolsview on inflating menu
    View programToolsBtn;

    //used for the hamburger icon in the toolbar menu
    ActionBarDrawerToggle mToggle;

    public Program program;

    private WorkoutsViewPagerAdapter mAdapter;

    private WorkoutsViewModel workoutsViewModel;
    private Workout w;

    private boolean isLoggedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        workoutsViewModel = ViewModelProviders.of(this, workoutsViewModelFactory).get(WorkoutsViewModel.class);
        ButterKnife.bind(this);
        setUpToolbar();
        setUpViewPager();
        longClickMenuView.instantiate(this);
        programToolsView.instantiate(programToolsBtn, this);


        /*String newString = dataManager.getPrefs().getString(FIRSTVISIT, "yes");
        boolean isNew = newString.equals("yes");
        if (isNew) {
            startActivity(new Intent(this, TutorialActivity.class));
        }
*/



        workoutsViewModel.getWorkoutsList().observe(this, list -> mAdapter.notifyDataSetChanged());
        notifyCurrentWorkout();

    }

    //TODO: call this function when needed
    private void loggedInUI(){
        isLoggedIn = false;         //TODO: need to check if the user is logged in

        if(isLoggedIn){
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.menu_main_logged_in);
            //TODO: apply any "user logged in" changes such as username
        }else{
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.menu_main_logged_out);
        }

    }


    private void notifyCurrentWorkout() {
        w = workoutsViewModel.getWorkoutsList().getValue().get(mViewPager.getCurrentItem());

    }

    private void setUpViewPager() {

        mTabLayout.setupWithViewPager(mViewPager);
        mAdapter = new WorkoutsViewPagerAdapter(
                getSupportFragmentManager(),
                workoutsViewModel.getWorkoutsList().getValue());

        mViewPager.setOffscreenPageLimit(workoutsViewModel.getWorkoutsList().getValue().size());
        mViewPager.setAdapter(mAdapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                longClickMenuView.onHideMenu();
                notifyCurrentWorkout();
                //layoutManager.mLayoutManagerHelper.updateLayoutManagerHelper(mViewPager.getCurrentItem());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        mDrawerLayout.setScrimColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        mToggle.setDrawerIndicatorEnabled(true);
        mNavigationView.setNavigationItemSelectedListener(this);
        String programName = workoutsViewModel.getProgram().getValue().programName;
        toolbar.setTitle(programName);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            title.setText(programName);
            getSupportActionBar().setTitle("");
            //  getSupportActionBar().setElevation(0);
        }


    }

    @Override
    public void onBackPressed() {
        if (longClickMenuView != null && longClickMenuView.isOn()) {
            longClickMenuView.onHideMenu();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.aviv_menu_program, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        programToolsBtn = findViewById(R.id.edit_menu);
        programToolsView.setProgramToolsBtn(programToolsBtn);
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.edit_menu:
                programToolsView.expand();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment f;
        switch (item.getItemId()) {
            case R.id.menu_create_program:
                f = new NewProgramFragment();
                addFragmentToHomeActivity(R.id.activity_home_framelayout, f, "NewProgram");
                break;
            case R.id.menu_my_programs:
                f = new MyProgramsFragment();
                addFragmentToHomeActivity(R.id.activity_home_framelayout, f, "MyPrograms");
                break;
            case R.id.menu_custom_exercise:
                f = new CustomExerciseFragment();
                addFragmentToHomeActivity(R.id.activity_home_framelayout, f, "CustomExercise");
                break;
            case R.id.menu_login:
               startActivityForResult(new Intent(this, MainActivity.class), LOGIN_ACTIVITY);
                break;
            case R.id.menu_logout:
                //TODO: implement a logout function
        }
        mDrawerLayout.closeDrawer(Gravity.START);

        return false;
    }

    //getscreenheight provides the height of the screen
    //mainly used for the musclegridview allowing to create a symetric grid
    public int getScreenHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.heightPixels;
    }

    @Override
    public void onLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh) {
        longClickMenuView.onShowMenu(vh, plObject);
    }

    @Override
    public void onProgramToolsClick() {

    }

    @Override
    public void onLongClickAction(LongClickMenuView longClickMenuView, WorkoutsModel.Actions action) {
        WorkoutsModel.ListModifier listModifier = null;
        int position = longClickMenuView.getViewPosition();
        PLObject plObject = w.exArray.get(position);

        switch (action) {
            case Duplicate:
                listModifier = WorkoutsModel.ListModifier.
                        OnWith(w, WorkoutItemAdapterFactory.getFactory().create(plObject.getClass()))
                        .doDuplicate(plObject);
                break;
            case Delete:
                listModifier = WorkoutsModel.ListModifier.OnWith(w, new ExerciseItemAdapter())
                        .doRemove(plObject, 1);
                longClickMenuView.onHideMenu();
                break;

            case MultiDelete:
                for (PLObject p : longClickMenuView.getSelectedPLObjects()) {
                    listModifier = WorkoutsModel.ListModifier.OnWith(w, WorkoutItemAdapterFactory.getFactory())
                            .doRemove(p, 1);
                }
                longClickMenuView.onHideMenu();
                break;
            case Child:
                listModifier = WorkoutsModel.ListModifier.OnWith(w, WorkoutItemAdapterFactory.getFactory())
                        .doChild(longClickMenuView.getSelectedPLObjects().get(0));
        }
        if (listModifier != null) {
            w.getObserver().onChange(listModifier);
            workoutsViewModel.saveLayoutToDataBase();
        }
        if (w.exArray.size() == 0) {
            longClickMenuView.onHideMenu();
        }


    }

    @Override
    public void onProgramToolsAction(WorkoutsModel.Actions action) {
        if (action == Advanced) {
            addFragmentToHomeActivity(R.id.activity_home_framelayout, new ProgramSettingsFragment(), "programsettings");
        } else {
            workoutsViewModel.workoutsModel.validateActions(
                    workoutsViewModel.getWorkoutsList().getValue(),
                    mViewPager.getCurrentItem(),
                    action);

            switch (action) {
                case NewExercise:
                    w.getObserver().onChange(
                            WorkoutsModel.ListModifier
                                    .OnWith(w, new ExerciseItemAdapter())
                                    .doAddNew(w.exArray.size())
                    );

                    break;

                case NewWorkout:
                    workoutsViewModel.workoutsModel.addNewWorkout(workoutsViewModel.getWorkoutsList().getValue());
                    notifyAdapter();
                    break;

                case NewDivider:
                    w.getObserver().onChange(
                            WorkoutsModel.ListModifier
                                    .OnWith(w, new DividerItemAdapter())
                                    .doAddNew(w.exArray.size())
                    );
                    break;

            }


        }
        workoutsViewModel.saveLayoutToDataBase();

    }

    @Override
    public void notifyAdapter() {
        mAdapter.notifyDataSetChanged();
    }

    public void test(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment nextFragment = ExerciseDetailsFragment.getInstance(String.valueOf(mViewPager.getCurrentItem()));
        Fragment previousFragment = getSupportFragmentManager().getFragments().get(mViewPager.getCurrentItem());
        // 1. Exit for Previous Fragment
        Fade exitFade = new Fade();
        exitFade.setDuration(200);
        previousFragment.setExitTransition(exitFade);

        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(200);
        enterTransitionSet.setStartDelay(200);
        nextFragment.setSharedElementEnterTransition(enterTransitionSet);

        // 3. Enter Transition for New Fragment
        Fade enterFade = new Fade();
        enterFade.setStartDelay(200);
        enterFade.setDuration(200);
        nextFragment.setEnterTransition(enterFade);
        v.setTransitionName("q");
        fragmentTransaction.addSharedElement(v, v.getTransitionName());
        fragmentTransaction.replace(R.id.activity_home_framelayout, nextFragment);
        fragmentTransaction.addToBackStack(String.valueOf(mViewPager.getCurrentItem()));
        fragmentTransaction.commitAllowingStateLoss();
    }

    @SuppressLint("RestrictedApi")
    public void navigateToExerciseDetailsActivity(PLObject.ExerciseProfile ep, ExerciseViewHolder vh) {
        Intent i = new Intent(this, ExerciseDetailsActivity.class);
        i.putExtra(EXERCISE_POSITION, vh.getAdapterPosition());
        i.putExtra("exercise", ep);
        i.putExtra("transitionName", vh.icon.getTransitionName());
        Pair<View, String>[] pairs = new Pair[2];

        pairs[0] = Pair.create(vh.name, "q");
        pairs[1] = Pair.create(vh.icon, vh.icon.getTransitionName());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        startActivityForResult(i, EXERCISE_ACTIVITY, options.toBundle());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_ACTIVITY) {
            if (resultCode == RESULT_OK) {
               //TODO: implement loggedInUI function
                //TODO: change user name in the activity
                loggedInUI();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}