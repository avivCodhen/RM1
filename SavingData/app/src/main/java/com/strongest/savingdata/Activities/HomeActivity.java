package com.strongest.savingdata.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
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

import com.strongest.savingdata.BaseWorkout.Programmer;
import com.strongest.savingdata.Database.Articles.ArticleObj;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Fragments.CustomExerciseFragment;
import com.strongest.savingdata.Fragments.MyProgramsFragment;
import com.strongest.savingdata.Fragments.NewProgramFragment;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.MyViews.WorkoutView.ProgramToolsView;
import com.strongest.savingdata.MyViews.WorkoutView.WorkoutView;
import com.strongest.savingdata.R;
import com.strongest.savingdata.tabFragments.HomeFragment;
import com.strongest.savingdata.tabFragments.ManagerFragment;

import java.util.ArrayList;

import static com.strongest.savingdata.Activities.TutorialActivity.FIRSTVISIT;
// import com.roughike.bottombar.OnMenuTabClickListener;

public class HomeActivity extends BaseActivity implements View.OnClickListener, ProgramDependencies, NavigationView.OnNavigationItemSelectedListener, OnProgramInitListener {

    public static String CURRENT_PROGRAM_DBNAME = "current_program_dbname";
    public static String CURRENT_PROGRAM_NAME = "current_program_name";

    private Toolbar toolbar;
    public WorkoutView workoutView;
    private AppBarLayout mAppBarLayout;
    private static final String TO_REFRESH = "refresh";
    private static final int COME_BACK_WITH_PROGRAM = 0;

    private ManagerFragment managerFragment;
    private HomeFragment homeFragment;
    //private WorkoutFragment workoutFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private LongClickMenuView longClickMenuView;
    private View programToolsBtn;
    /*@Inject
    public Programmer programmer;
    @Inject
    public DataManager dataManager;*/

    private ArrayList<ArticleObj> texts = new ArrayList<>();
    private FloatingActionButton fab;
    private Fragment fragment;
    /*private Fragment[] fragments = new Fragment[]{
            new HomeFragment(),
            new WorkoutFragment(),
            new ManagerFragment()
    };*/

    private ViewPager viewPager;
    private TabLayout tabLayout;
    ProgramToolsView programToolsView;


    //private NavigationTabBar navigationTabBar;

    //private ArrayList<NavigationTabBar.Model> models = new ArrayList<>();

    //private HomeAdapter adapter;

    private int[] defaultIcons = {
            R.drawable.nav_home_black_24px,
            R.drawable.nav_workout_black_24px,
            R.drawable.nav_manager_black_24px
    };

    private int[] activeColors = {
            R.drawable.nav_home_purple_24px,
            R.drawable.nav_workout_purple_24px,
            R.drawable.nav_manager_purple_24px
    };
    private boolean settingsDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* Download d = new Download(this);
        try {

            d.refreshData(
                    DBMuscleHelper.DB_TABLE_NAME,
                    TABLE_CHEST,
                    TABLE_BACK,
                    TABLE_LEGS,
                    TABLE_SHOULDERS,
                    TABLE_ARMS,
                    TABLE_CORE
            );
        } catch (Exception e) {
            Log.d("aviv", "downloadExercises: " + e.toString());
            // dm.getPrefsEditor().putBoolean("download", true).commit();
        }*/
        setContentView(R.layout.activity_home);
        String newString = dataManager.getPrefs().getString(FIRSTVISIT, "yes");
        boolean isNew = newString.equals("yes");
        if(isNew){
            startActivity(new Intent(this, TutorialActivity.class));
        }
        toolbar = (Toolbar) findViewById(R.id.activity_home_toolbar);
        setSupportActionBar(toolbar);

        longClickMenuView = (LongClickMenuView) findViewById(R.id.activity_home_longclick_menu);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.activity_home_app_bar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_drawer_layout);
        mDrawerLayout.setScrimColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mNavigationView = (NavigationView) findViewById(R.id.home_activity_navigationview);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        mToggle.setDrawerIndicatorEnabled(true);
        mNavigationView.setNavigationItemSelectedListener(this);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        programmer.getLayoutManager();
        dataManager.getPrefsEditor();
        toolbar.setTitleTextColor(Color.WHITE);

        viewPager = (ViewPager) findViewById(R.id.activity_home_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.activity_home_tablayout);
        programToolsView = (ProgramToolsView) findViewById(R.id.activity_programtoolsview);
        workoutView = new WorkoutView();
        programmer.tryInitProgram(this);
        if (programmer.getProgram() == null) {
            programmer.createNewProgram(this);
        }

        toolbar.setTitle(programmer.getProgram().programName);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(programmer.getProgram().programName);
            //  getSupportActionBar().setElevation(0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        }

    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float per = Math.abs(mAppBarLayout.getY()) / mAppBarLayout.getTotalScrollRange();
            boolean setExpanded = (per <= 0.5F);
            mAppBarLayout.setExpanded(setExpanded, true);
        }
        return super.dispatchTouchEvent(event);
    }*/

    @Override
    public void onClick(View v) {
    }

    public void begoneTabLayout() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                workoutView.getmTabLayout().setVisibility(View.INVISIBLE);
            }
        }, 800);
    }

    public void reviveTabLayout() {
        workoutView.getmTabLayout().setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if(longClickMenuView != null && longClickMenuView.isOn()){
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
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.edit_menu:
                programToolsBtn = findViewById(R.id.edit_menu);
                programToolsView.setProgramToolsBtn(programToolsBtn);
                workoutView.enterEditMode();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public DataManager getDependecies() {
        return dataManager;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create_program:
                NewProgramFragment fr = new NewProgramFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.activity_home_framelayout, fr, "myPrograms")
                        .addToBackStack("myPrograms")
                        .commit();
                mDrawerLayout.closeDrawer(Gravity.START);
                //viewPager.setCurrentItem(1);
                break;
            case R.id.menu_my_programs:
                MyProgramsFragment frag = new MyProgramsFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.activity_home_framelayout, frag, "myPrograms")
                        .addToBackStack("myPrograms")
                        .commit();
                mDrawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.menu_custom_exercise:

                CustomExerciseFragment fr1 = new CustomExerciseFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.activity_home_framelayout, fr1, "custom_exercise")
                        .addToBackStack("custom_exercise")
                        .commit();
                mDrawerLayout.closeDrawer(Gravity.START);
                break;
            /*case R.id.menu_save_program:
                programmer.getLayoutManager().saveLayoutToDataBase(true);
                Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();*/
        }


        return false;
    }

    @Override
    public void instantiateWorkView() {
        workoutView.instantiate(this, getSupportFragmentManager(), programmer.getLayoutManager(), viewPager, tabLayout);
        workoutView.setmAppBarLayout(mAppBarLayout);
        workoutView.setLongClickMenu(longClickMenuView);
        workoutView.setProgramToolsView(programToolsView, programToolsBtn);
    }

/*
    public class LoadArticles extends AsyncTask {

        @Override
        protected void onPreExecute() {
            if (fragment != null && fragment instanceof HomeFragment) {
                ((HomeFragment) fragment).dealWithProgressBar(true);
            }
        }


        @Override
        protected Object doInBackground(Object[] params) {
            ArticleObj arobj;
            try {
                Document doc = Jsoup.connect("http://strongest.co.il").get();
                final Elements article = doc.select("article");
                StringBuilder sb = new StringBuilder();
                for (Element link : article) {
                    arobj = new ArticleObj();
                    arobj.setTitle(link.select("a").attr("title"));
                    arobj.setSummary(link.select("p").text());
                    arobj.setLink(link.select("img").attr("src"));
                    arobj.setPage(link.select("a").attr("href"));
                    DownloadImage downloadImage = new DownloadImage(arobj, dataManager);
                    downloadImage.run();
                    texts.add(arobj);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            if (fragment != null && fragment instanceof HomeFragment) {
                ((HomeFragment) fragment).dealWithProgressBar(false);
                if (fragments[0] != null) {
                    ((HomeFragment) fragments[0]).updateArticles();
                }
            }
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        //new LoadArticles().execute();
        getPrefsEditor().putBoolean(TO_REFRESH, false);

    }

    @Override
    protected void onPause() {
        //    programmer.getLayoutManager().saveLayoutToDataBase(true);
        getPrefsEditor().putBoolean(TO_REFRESH, true).commit();
        //dataManager.getArticleDataManager().delete(TABLE_NAME);
        super.onPause();
    }

   /* private class HomeAdapter extends FragmentStatePagerAdapter {

        public HomeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
            //  return fragments.get(position);
        }

        @Override
        public int getCount() {
            //return fragments.size();
            return 3;
        }

    }*/

    public int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.widthPixels;

    }

    public int getScreenHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.heightPixels;
    }

    @Override
    public DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public Programmer getProgrammer() {
        return programmer;
    }
}
