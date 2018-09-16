package com.strongest.savingdata.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.AService.WorkoutsService;
import com.strongest.savingdata.AViewModels.ProgramViewModel;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.WorkoutItemAdapterFactory;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.DividerItemAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.ExerciseItemAdapter;
import com.strongest.savingdata.Adapters.WorkoutsViewPagerAdapter;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.Fragments.CustomExerciseFragment;
import com.strongest.savingdata.Fragments.NewProgramFragment;
import com.strongest.savingdata.Fragments.ProgramSettingsFragment;
import com.strongest.savingdata.Fragments.WorkoutViewFragment;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.MyViews.SmartProgressBar;
import com.strongest.savingdata.MyViews.WorkoutView.ProgramToolsView;
import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.strongest.savingdata.AModels.workoutModel.WorkoutsModel.Actions.Advanced;
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
    public static final int MY_PROGRAM_ACTIVITY = 3;
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
    public LongClickMenuView longClickMenuView;
    @BindView(R.id.activity_home_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.activity_home_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.activity_programtoolsview)
    ProgramToolsView programToolsView;
    @BindView(R.id.toolbar_title)
    public TextView title;

    @BindView(R.id.home_activity_smartprogressbar)
    SmartProgressBar smartProgressBar;

    //this view is the edit "+" button of the toolbar menu
    //it will be setuped with the programtoolsview on inflating menu
    View programToolsBtn;

    //used for the hamburger icon in the toolbar menu
    ActionBarDrawerToggle mToggle;

    public Program program;

    private WorkoutsViewPagerAdapter mAdapter;

    public WorkoutsViewModel workoutsViewModel;
    public ProgramViewModel programViewModel;

    private Workout w;

    private boolean isLoggedIn;
    private TextView myProgramBadge;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("program", program);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            program = (Program) savedInstanceState.getSerializable("program");
        }
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        smartProgressBar.setText("Loading Program...");
        smartProgressBar.setUpWithView(mViewPager);
        smartProgressBar.show();
        if (!userService.isUserLoggedIn()) {
            startActivityForResult(new Intent(this, LoginActivity2.class), LOGIN_ACTIVITY);

        }
        programViewModel = ViewModelProviders.of(this, workoutsViewModelFactory).get(ProgramViewModel.class);
        workoutsViewModel = ViewModelProviders.of(this, workoutsViewModelFactory).get(WorkoutsViewModel.class);
        setUpToolbar();


        programViewModel.getProgramModel().observe(this, program -> {
            this.program = program;
            if (program != null) {
                title.setText(program.getProgramName());
                if (!workoutsViewModel.workoutsInitialized) {
                    workoutsViewModel.initWorkouts();
                }
                programService.annonymouseToUser(program);
                smartProgressBar.hide();
            } else {
                toMyProgramsActivity();
            }

        });

        /*programViewModel.getNewProgram().observe(this, prog -> {
            programViewModel.setProgram(programViewModel.getNewProgram());
            title.setText(prog.getProgramName());
            workoutsViewModel.initWorkouts();


        });*/

        workoutsViewModel.getWorkoutsList().observe(this, workouts -> {

            setUpViewPager();
            notifyCurrentWorkout();
            workoutsViewModel.safeToSave = true;
        });


        longClickMenuView.instantiate(this);
        programToolsView.instantiate(programToolsBtn, this);
        loggedInUI();
        programService.listenForSharedPrograms(count -> showBadgeForMyProgram((int) count));

    }

    private void showBadgeForMyProgram(int count) {
        if (count > 0) {

            myProgramBadge = (TextView) MenuItemCompat.getActionView(mNavigationView.getMenu().findItem(R.id.menu_my_programs));
            myProgramBadge.setGravity(Gravity.CENTER_VERTICAL);
            myProgramBadge.setTypeface(null, Typeface.BOLD);
            myProgramBadge.setTextColor(getResources().getColor(R.color.red));

            myProgramBadge.setText(count + "");
        }
    }

    //TODO: call this function when needed
    private void loggedInUI() {
        isLoggedIn = userService.isUserLoggedIn();         //TODO: need to check if the user is logged in

        if (isLoggedIn) {
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.menu_main_logged_in);
            View v = mNavigationView.getHeaderView(0);
            TextView tv = v.findViewById(R.id.usernameTV);
            tv.setText(userService.getUsername());
            //TODO: apply any "user logged in" changes such as username
        } else {
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


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    protected void onPause() {
        if (workoutsViewModel.safeToSave) {
            workoutsViewModel.saveLayoutToDataBase();
        }
        super.onPause();
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
        Intent i;
        switch (item.getItemId()) {
            case R.id.menu_create_program:
                f = new NewProgramFragment();
                addFragmentToActivity(R.id.activity_home_framelayout, f, "NewProgram");
                break;
            case R.id.menu_my_programs:

                toMyProgramsActivity();

                break;
            case R.id.menu_custom_exercise:
                f = new CustomExerciseFragment();
                addFragmentToActivity(R.id.activity_home_framelayout, f, "CustomExercise");
                break;
            case R.id.menu_login:
                startActivityForResult(new Intent(this, LoginActivity2.class), LOGIN_ACTIVITY);
                //startActivityForResult(new Intent(this, RegisterActivity.class), LOGIN_ACTIVITY);
                break;

            case R.id.menu_share_program:
                i = new Intent(this, ShareProgramActivity.class);
                i.putExtra("programuid", programService.getProgramUID());
                i.putExtra("program", program);
                startActivity(i);
                break;
            case R.id.menu_logout:
                userService.logout();
                break;
        }
        mDrawerLayout.closeDrawer(Gravity.START);

        return false;
    }

    private void toMyProgramsActivity() {
        Intent i;
        i = new Intent(this, MyProgramsActivity.class);
        i.putExtra("current_program", program);
        startActivityForResult(i, MY_PROGRAM_ACTIVITY);
    }

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
                listModifier = WorkoutsModel.ListModifier.OnWith(w, WorkoutItemAdapterFactory.getFactory().create(plObject.getClass()))
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
            addFragmentToActivity(R.id.activity_home_framelayout, new ProgramSettingsFragment(), "programsettings");

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
                                    .setTaskTag("new_exercise")
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

   /* public void test(View v) {
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
    }*/

  /*  @SuppressLint("RestrictedApi")
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
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                loggedInUI();
                programViewModel.provideProgram();
                programService.listenForSharedPrograms(count -> showBadgeForMyProgram((int) count));
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == MY_PROGRAM_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                smartProgressBar.show();
                Program p = (Program) data.getSerializableExtra("program");
                if (p == null) {
                    throw new IllegalArgumentException();
                }
                workoutsViewModel.workoutsInitialized = false;
                workoutsViewModel.setCmd(WorkoutsService.CMD.SWITCH);
                programViewModel.postProgram(p);
            } else if (resultCode == MyProgramsActivity.FRAGMENT_CREATE_PROGRAM) {
                workoutsViewModel.setNewWorkout();
                programViewModel.setNewProgram();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}