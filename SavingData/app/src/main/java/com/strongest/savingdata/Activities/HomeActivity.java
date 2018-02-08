package com.strongest.savingdata.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.strongest.savingdata.ArtificialInteligence.ArtificialIntelligenceObserver;
import com.strongest.savingdata.BaseWorkout.Programmer;
import com.strongest.savingdata.Database.Articles.ArticleObj;
import com.strongest.savingdata.Database.Articles.DownloadImage;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Database.Muscles.DBMuscleHelper;
import com.strongest.savingdata.MyViews.MyViewPager;
import com.strongest.savingdata.R;
import com.strongest.savingdata.Server.Download;
import com.strongest.savingdata.tabFragments.HomeFragment;
import com.strongest.savingdata.tabFragments.ManagerFragment;
import com.strongest.savingdata.tabFragments.WorkoutFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_ARMS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_BACK;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_CHEST;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_CORE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_ALL;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_GENERATOR;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_LEGS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_METHODS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REST;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_SETS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_SHOULDERS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_STATS;
// import com.roughike.bottombar.OnMenuTabClickListener;

public class HomeActivity extends BaseActivity implements View.OnClickListener, ProgramDependencies, NavigationView.OnNavigationItemSelectedListener {


    private static final String TO_REFRESH = "refresh";
    private static final int COME_BACK_WITH_PROGRAM = 0;
    private BottomBar bottomBar;

    private ManagerFragment managerFragment;
    private HomeFragment homeFragment;
    private WorkoutFragment workoutFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    /*@Inject
    public Programmer programmer;
    @Inject
    public DataManager dataManager;*/

    private ArrayList<ArticleObj> texts = new ArrayList<>();
    private FloatingActionButton fab;
    private Fragment fragment;
    private Fragment[] fragments = new Fragment[]{
            new HomeFragment(),
            new WorkoutFragment(),
            new ManagerFragment()
    };

    private MyViewPager viewPager;
    private TabLayout tabLayout;
    //private NavigationTabBar navigationTabBar;

    //private ArrayList<NavigationTabBar.Model> models = new ArrayList<>();

    private HomeAdapter adapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mNavigationView = (NavigationView) findViewById(R.id.home_activity_navigationview);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        programmer.getLayoutManager();
        dataManager.getPrefsEditor();

        // dataManager = new DataManager(this);
        fab = (FloatingActionButton) findViewById(R.id.workout_fab_create);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragments[1] = new ManagerFragment();
                adapter.notifyDataSetChanged();

            }
        });

        if (getPrefs().getBoolean(TO_REFRESH, true)) {

        }
        tabLayout = (TabLayout) findViewById(R.id.activity_home_navigationbar);
        viewPager = (MyViewPager) findViewById(R.id.activity_home_viewpager);
        viewPager.setOffscreenPageLimit(defaultIcons.length);
        // navigationTabBar = (NavigationTabBar) findViewById(R.id.activity_home_navigationbar);
        initUI();
        initModels();

    }


    private void initUI() {
        adapter = new HomeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (i == 0) {
                tabLayout.getTabAt(i).setIcon(activeColors[i]);

            } else {
                tabLayout.getTabAt(i).setIcon(defaultIcons[i]);

            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(activeColors[tab.getPosition()]);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(defaultIcons[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setSelectedTabIndicatorHeight(0);


    }

    private void initModels() {
    /*    models.add(
                new NavigationTabBar.Model.Builder(
                        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home_black_24dp, null),
                        Color.parseColor(colors[0])
                )

                        .title("Heart")
                        .badgeTitle("Home")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_content_paste_black_24dp, null),
                        Color.parseColor(colors[1])
                ).title("Cup")
                        .badgeTitle("Workout")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_assessment_black_24dp, null),
                        Color.parseColor(colors[2])
                ).title("Progress")
                        .badgeTitle("state")
                        .build()
        );
        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager);
        navigationTabBar.setBackgroundColor(
                ResourcesCompat.getColor(getResources(), R.color.background_color, null)
        );*/
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(this, CreateWorkoutActivity.class), COME_BACK_WITH_PROGRAM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COME_BACK_WITH_PROGRAM) {
            if (resultCode == RESULT_OK) {
                /*LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
                Intent i = new Intent("TAG_REFRESH");
                lbm.sendBroadcast(i);*/
                //   initUI();
              /*  programmer.updateLayout();
                ((WorkoutFragment) fragments[1]).updateAdapter();
                adapter.notifyDataSetChanged();
                navigationTabBar.setModelIndex(1);
                viewPager.setCurrentItem(1);*/
                // dataManager.closeDataBases();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public DataManager getDependecies() {
        return dataManager;
    }

    @Override
    public void attachAI(ArtificialIntelligenceObserver o) {

    }

    @Override
    public void dettachAI(ArtificialIntelligenceObserver o) {

    }

    @Override
    public void notifyAI(int workoutPosition) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_create_program) {
            ((WorkoutFragment) fragments[1]).switchToCreateProgram();
            mDrawerLayout.closeDrawer(Gravity.START);
            viewPager.setCurrentItem(1);
            //navigationTabBar.setModelIndex(1);

        }
        return false;
    }


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
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        //new LoadArticles().execute();
        getPrefsEditor().putBoolean(TO_REFRESH, false);

    }

    @Override
    protected void onPause() {
        programmer.getLayoutManager().saveLayoutToDataBase(true);
        getPrefsEditor().putBoolean(TO_REFRESH, true).commit();
        //dataManager.getArticleDataManager().delete(TABLE_NAME);
        super.onPause();
    }

    private class HomeAdapter extends FragmentStatePagerAdapter {

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
