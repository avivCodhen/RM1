package com.strongest.savingdata.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hlab.fabrevealmenu.listeners.OnFABMenuSelectedListener;
import com.hlab.fabrevealmenu.view.FABRevealMenu;
import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.AService.WorkoutsService;
import com.strongest.savingdata.AViewModels.ProgramViewModel;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.WorkoutItemAdapterFactory;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.TitleItemAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.ExerciseItemAdapter;
import com.strongest.savingdata.Adapters.WorkoutsViewPagerAdapter;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.Database.Managers.DefaultWorkoutsDataManager;
import com.strongest.savingdata.Fragments.BaseFragment;
import com.strongest.savingdata.Fragments.CustomExerciseFragment;
import com.strongest.savingdata.Fragments.ExerciseEditFragment;
import com.strongest.savingdata.Fragments.ProgramSettingsFragment;
import com.strongest.savingdata.Fragments.SetsChooseSingleFragment;
import com.strongest.savingdata.Fragments.WorkoutViewFragment;
import com.strongest.savingdata.Handlers.MaterialDialogHandler;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.MyViews.MovableFloatingActionButton;
import com.strongest.savingdata.MyViews.SmartEmptyView;
import com.strongest.savingdata.MyViews.SmartProgressBar;
import com.strongest.savingdata.MyViews.WorkoutView.ProgramToolsView;
import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.strongest.savingdata.AModels.workoutModel.WorkoutsModel.Actions.Advanced;
import static com.strongest.savingdata.AModels.workoutModel.WorkoutsModel.Actions.CustomExercise;
import static com.strongest.savingdata.AModels.workoutModel.WorkoutsModel.Actions.MyProgram;
import static com.strongest.savingdata.AModels.workoutModel.WorkoutsModel.Actions.Share;
// import com.roughike.bottombar.OnMenuTabClickListener;

public class HomeActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        WorkoutViewFragment.WorkoutViewFragmentListener,
        Architecture.view.LongClickView,
        Architecture.view.ProgramTools,
        ProgramSettingsFragment.OnProgramSettingsChange,
        OnFABMenuSelectedListener {

    // public WorkoutsModelController workoutsModelController;

    public static final int EXERCISE_ACTIVITY = 1;
    public static final int LOGIN_ACTIVITY = 2;
    public static final int MY_PROGRAM_ACTIVITY = 3;
    public static final int NOTIFICATION = 4;
    public static final String EXERCISE_POSITION = "exercisePosition";
    public static final String REFRESH = "refresh";

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

    @BindView(R.id.home_activity_smartemptyview)
    SmartEmptyView smartEmptyView;

    @BindView(R.id.home_activity_smartprogressbar)
    SmartProgressBar smartProgressBar;

    @BindView(R.id.home_activity_fab)
    MovableFloatingActionButton fab;

    @BindView(R.id.fabMenu)
    FABRevealMenu fabRevealMenu;

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
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            program = (Program) savedInstanceState.getSerializable("program");
        }
        String userName = getIntent().getStringExtra("userName");
        if (userName != null && !userName.equals("")) {
            getIntent().removeExtra("userName");
            MaterialDialogHandler.get()
                    .defaultBuilder(this, userName + " has sent you a program!", "VIEW")
                    .addContent("You can view the program in My Programs.")
                    .buildDialog()
                    .addPositiveActionFunc(v -> toMyProgramsActivity(), true)
                    .show();
        }

       /* fabRevealMenu.bindAnchorView(fab);
        fabRevealMenu.setOnFABMenuSelectedListener(this);*/


        if (userService.firstTimeClient()) {
            toLogInActivity();
        }

        smartProgressBar.setText("Loading Program...")
                .setUpWithView(mViewPager)
                .registerRabitHoleBreaker(5000, () -> programViewModel.initProgram());


        programViewModel = ViewModelProviders.of(this, workoutsViewModelFactory).get(ProgramViewModel.class);
        programViewModel.initProgram();
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
            }
            smartProgressBar.hide();

        });

        programViewModel.fetchAllPrograms();

        programViewModel.getPrograms().observe(this, list -> {
            showNoProgramLoadedDialog(list.size());
        });

        workoutsViewModel.getWorkoutsList().observe(this, workouts -> {

            setUpViewPager();
            notifyCurrentWorkout();
            workoutsViewModel.safeToSave = true;
        });


        longClickMenuView.instantiate(this);
        programToolsView.instantiate(fab, this);
        programToolsView.setProgramToolsBtn(fab);
        programToolsView.setLoginFunc(v -> toLogInActivity());
        programToolsView.setLogoutFunc(v -> logOut());

        loggedInUI();

        ifNoProgram();
    }

    private void showNoProgramDialog() {
        MaterialDialogHandler
                .get()
                .defaultBuilder(this, getString(R.string.no_program), getString(R.string.navigate_my_programs_button))
                .addContent(getString(R.string.no_program_content))
                .buildDialog()
                .addPositiveActionFunc(v -> toMyProgramsActivity(), true)
                .show();
    }


    private void showNoProgramLoadedDialog(int size) {
        smartEmptyView.setImage(smartEmptyView.getDocImage())
                .setTitle(getString(R.string.no_program))
                .setBody(getString(R.string.no_program_content))
                .setButtonText(getString(R.string.navigate_my_programs_button))
                .setActionBtn(v -> toMyProgramsActivity())
                .setUpWithViewPager(mViewPager, false, false)
                .setUpWithMoreViews(mTabLayout)
                .onShowFunc(() -> ifNoProgram())
                .onCondition(size);

    }

    private void ifNoProgram() {
        if (program == null) {
            title.setText("RM1");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (programService.isProgramAvailable()) {
            programService.listenForSharedPrograms(count -> showBadgeForMyProgram((int) count));
        } else {
            programViewModel.noProgram();
            workoutsViewModel.noWorkout();
            programViewModel.fetchAllPrograms();

        }
    }

    private void showBadgeForMyProgram(int count) {
        myProgramBadge = (TextView) MenuItemCompat.getActionView(mNavigationView.getMenu().findItem(R.id.menu_my_programs));
        myProgramBadge.setGravity(Gravity.CENTER_VERTICAL);
        myProgramBadge.setTypeface(null, Typeface.BOLD);
        myProgramBadge.setTextColor(getResources().getColor(R.color.red));
        if (count > 0) {
            myProgramBadge.setText(count + "");
        } else {
            myProgramBadge.setText("");

        }
        programToolsView.updateMenuAlertCount(WorkoutsModel.Actions.MyProgram, count);
    }

    private void loggedInUI() {
        isLoggedIn = userService.isUserLoggedIn();

        View v = mNavigationView.getHeaderView(0);
        TextView untv = v.findViewById(R.id.usernameTV);
        TextView emtv = v.findViewById(R.id.emailTV);
        Button logInbtn = v.findViewById(R.id.menu_header_login);
        View logOut = v.findViewById(R.id.menu_header_logoutIV);
        if (isLoggedIn) {

            untv.setText(userService.getUsername());
            emtv.setText(userService.getEmail());
            logInbtn.setVisibility(View.GONE);
            logOut.setVisibility(View.VISIBLE);
            logOut.setOnClickListener(view -> logOut());
        } else {
            untv.setText("");
            emtv.setText("");
            logInbtn.setVisibility(View.VISIBLE);
            logInbtn.setOnClickListener(view -> toLogInActivity());
            logOut.setVisibility(View.GONE);
        }


    }


    private void notifyCurrentWorkout() {
        if (workoutsViewModel.getWorkoutsList().getValue().size() != 0)
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
                fab.show();
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
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
        if (workoutsViewModel.safeToSave && programService.isProgramAvailable()) {
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
        if (programToolsView.isOpen()) {
            programToolsView.close();
            return;
        }
        BaseFragment f;
        f = (BaseFragment) getSupportFragmentManager().findFragmentByTag(ExerciseEditFragment.FRAGMENT_EDIT_EXERCISE);
        if (f == null) {
            f = (BaseFragment) getSupportFragmentManager().findFragmentByTag(SetsChooseSingleFragment.SETS_CHOOSE_FRAGMENT);

            if (f != null) {
                View v = f.getView();

                MyJavaAnimator.animateRevealShowParams(v, false, R.color.background_color, 0, 0, r -> {
                    super.onBackPressed();
                    return;
                });
            } else {
                super.onBackPressed();
            }

        } else {
            View v = f.getView();

            MyJavaAnimator.animateRevealShowParams(v, false, R.color.background_color, 0, 0, r -> {
                super.onBackPressed();
                return;
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_activity_plus_menu, menu);
        /*programToolsBtn =(View) menu.findItem(R.id.home_activity_plus_menu);
        programToolsView.setProgramToolsBtn(programToolsBtn);*/
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        switch (item.getItemId()) {
            case R.id.edit_menu:
                programToolsView.expand();
                return true;

            case R.id.home_activity_plus_menu:
                programToolsView.expand();

                /*if (program == null) {
                    showNoProgramDialog();
                }
                addFragmentToActivity(R.id.activity_home_framelayout, new ProgramSettingsFragment(), "programsettings");*/
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment f;
        Intent i;
        switch (item.getItemId()) {
          /*  case R.id.menu_create_program:
                f = new NewProgramFragment();
                addFragmentToActivity(R.id.activity_home_framelayout, f, "NewProgram");
                break;*/
            case R.id.menu_my_programs:

                toMyProgramsActivity();

                break;
            case R.id.menu_custom_exercise:
                f = new CustomExerciseFragment();
                addFragmentToActivity(R.id.activity_home_framelayout, f, "CustomExercise");
                break;
            /*case R.id.menu_login:
                startActivityForResult(new Intent(this, LoginActivity2.class), LOGIN_ACTIVITY);
                break;
*/
            case R.id.menu_share_program:
                toShareActivity();
                break;
          /*  case R.id.menu_logout:
                logOut();
                break;*/
        }
        mDrawerLayout.closeDrawer(Gravity.START);

        return false;
    }

    private void logOut() {

        MaterialDialogHandler.get()
                .defaultBuilder(this, "Are you sure you want to logout?", "YES")
                .buildDialog()
                .addPositiveActionFunc(v -> {


                    userService.logout();
                    finish();

                    toLogInActivity();

                }, true)
                .show();
    }

    private void toShareActivity() {

        if (program == null) {
            MaterialDialogHandler
                    .get()
                    .defaultBuilder(this, getString(R.string.no_program), "My Programs")
                    .addContent(getString(R.string.load_program_to_share))
                    .buildDialog()
                    .addPositiveActionFunc(v -> toMyProgramsActivity(), true)
                    .show();
        } else if (!userService.isUserLoggedIn()) {
            MaterialDialogHandler.get().showNotLoggedInDialog(this, v -> toLogInActivity());
        } else {

            Intent i = new Intent(this, ShareProgramActivity.class);
            i.putExtra("programuid", programService.getProgramUID());
            i.putExtra("program", program);
            startActivity(i);

        }
    }

    private void toLogInActivity() {
        startActivityForResult(new Intent(this, LoginActivity2.class), LOGIN_ACTIVITY);
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
        PLObject plObject = longClickMenuView.getSelectedPLObjects().get(0);

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
                break;

            case edit:
                w.getOnEdit().edit(position, (PLObject.ExerciseProfile) longClickMenuView.getSelectedPLObjects().get(0));
                break;

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
    public void onProgramToolsAction(ProgramToolsView.ProgramButton programButton) {
        WorkoutsModel.Actions action = programButton.type;
        if (programButton.requiresProgram == true) {
            if (program == null) {

                showNoProgramDialog();
                return;
            }
        }

        if (action == Advanced) {
            addFragmentToActivity(R.id.activity_home_framelayout, new ProgramSettingsFragment(), "programsettings");
            return;

        } else if (action == Share) {
            toShareActivity();
            programToolsView.close();

            return;
        } else if (action == MyProgram) {
            toMyProgramsActivity();
            programToolsView.close();
            return;
        } else if (action == CustomExercise) {
            CustomExerciseFragment f = new CustomExerciseFragment();
            addFragmentToActivity(R.id.activity_home_framelayout, f, "CustomExercise");
            return;
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
                                    .OnWith(w, new TitleItemAdapter())
                                    .doAddNew(w.exArray.size())
                    );
                    break;

            }


        }
        workoutsViewModel.saveLayoutToDataBase();
        Snackbar snackbar = Snackbar.
                make(fab, "Added " + programButton.tv_name + " Successfully", Snackbar.LENGTH_SHORT);
        View v = snackbar.getView();
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        TextView textView = v.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        snackbar.show();
        programToolsView.close();

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
                String s = data.getStringExtra(MyProgramsActivity.WHICH_PROGRAM);
                if (s.equals(DefaultWorkoutsDataManager.DEFAULT)) {
                    programViewModel.setNewProgram();
                    workoutsViewModel.setNewWorkout();

                } else {
                    programViewModel.setDefaultWorkoutTemplate(s);
                    workoutsViewModel.setDefaultWorkoutsTemplate(s);
                }

                MaterialDialogHandler
                        .get()
                        .defaultBuilder(this, getString(R.string.created_program_title), getString(R.string.got_it))
                        .addContent(getString(R.string.created_program_content))
                        .buildDialog()
                        .hideNegativeButton()
                        .show();


            } else if (resultCode == MyProgramsActivity.LOG_IN) {
                toLogInActivity();
            }
        } else if (requestCode == NOTIFICATION) {
            Log.d(TAG, "onActivityResult: " + data.getStringExtra("userName"));
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onMenuItemSelected(View view, int id) {

    }

    public FloatingActionButton getFab() {
        return fab;
    }
}