package com.strongest.savingdata.Fragments.Choose;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Fragments.ExerciseEditFragment;
import com.strongest.savingdata.Fragments.SetsChooseSingleFragment;
import com.strongest.savingdata.MyViews.WorkoutView.OnExerciseChangeListener;
import com.strongest.savingdata.R;
import com.strongest.savingdata.Fragments.BaseCreateProgramFragment;
import com.strongest.savingdata.YoutubeAPI.VideoItem;
import com.strongest.savingdata.YoutubeAPI.YoutubeConfig;
import com.strongest.savingdata.YoutubeAPI.YoutubeConnector;

import java.util.ArrayList;
import java.util.List;

public class ChooseDialogFragment extends BaseCreateProgramFragment implements View.OnClickListener,
        TabLayout.OnTabSelectedListener,  OnExerciseSetChange {

    public static final String PLOBJECT = "plobject";
    public static final String POSITION = "position";
    public static final String OLD_POSITION = "old_position";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Bundle i;
    private ArrayList<PLObject> mLayout;
    private DataManager dataManager;
    public AppBarLayout appBarLayout;
    //   private NestedScrollView nestedScrollView;
    private FloatingSearchView mSearchView;
    private ImageView searchIv;
    private PLObject plObject;
    //private PLObject.ExerciseProfile exerciseProfile;
    private PLObject.SetsPLObject setsPLObject;
    private PLObject.IntraSetPLObject intraSetPLObject;
    private ExerciseEditFragment exerciseEditFragment;
    private String tName;
    private Button backBtn;

    private OnExerciseChangeListener onExerciseChangeListener;

    private int plObjectPosition;

    // private RecyclerView.ViewHolder vh;

    private int currentTab;
    private boolean initiated;
    private RecyclerView mRecyclerView;
    public MyExpandableAdapter myExpandableAdapter;
    private ChooseAdapter adapter;
    // private LayoutManager.LayoutManagerHelper helper;
    private int setPosition;
    private String title = "";
    private View mainView;


    YouTubePlayer.OnInitializedListener onInitializedListener;
    List<VideoItem> searchResults;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    Handler handler = new Handler();
    YouTubePlayer YPlayer;

    public static ChooseDialogFragment getInstance(OnExerciseChangeListener onExerciseChangeListener,
                                                   int oldPosition, int position,
                                                   PLObject plObject) {
        ChooseDialogFragment f = new ChooseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PLOBJECT, plObject);
        bundle.putInt(POSITION, position);
        bundle.putInt(OLD_POSITION, oldPosition);
        f.setOnExerciseChangeListener(onExerciseChangeListener);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //      helper = ((BaseActivity) getActivity()).getProgrammer().layoutManager.mLayoutManagerHelper;
            plObject = (PLObject) getArguments().getSerializable(PLOBJECT);
           // title = LayoutManagerHelper.writeTitle(plObject);
            if (plObject instanceof PLObject.ExerciseProfile) {
               // exerciseProfile = (PLObject.ExerciseProfile) plObject;
                /*exerciseEditFragment = ExerciseEditFragment.newInstance(exerciseProfile, this);
                adapter = new ChooseAdapter(getChildFragmentManager(), exerciseEditFragment);*/

            } else if (plObject instanceof PLObject.SetsPLObject) {
                setsPLObject = (PLObject.SetsPLObject) plObject;
              //  exerciseProfile = setsPLObject.parent;
                adapter = new ChooseAdapter(getChildFragmentManager(), SetsChooseSingleFragment.getInstance());
            } else if (plObject instanceof PLObject.IntraSetPLObject) {
                intraSetPLObject = (PLObject.IntraSetPLObject) plObject;
               // exerciseProfile = intraSetPLObject.getParent();
                adapter = new ChooseAdapter(getChildFragmentManager(), SetsChooseSingleFragment.getInstance());
            }
            plObjectPosition = getArguments().getInt(POSITION);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_choose, container, false);
        mainView.setFocusableInTouchMode(true);
        mainView.requestFocus();
       /* mainView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mSearchView.getVisibility() == View.VISIBLE) {
                        mSearchView.setVisibility(View.GONE);
                        return true;
                    }

                    //this needs to be addressed!!!
                    //       ((HomeActivity) getActivity()).workoutsModelController.onExitChooseFragment(plObjectPosition, getArguments().getInt(OLD_POSITION));
                    plObject.setEditMode(false);
//                    onExerciseChangeListener.onExerciseChange(plObjectPosition, "");
                    getFragmentManager().popBackStack();
                    return true;
                }
                return true;
            }

        });*/
        return mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {


        mLayout = new ArrayList<>();
        dataManager = new DataManager(getContext());
        ArrayList<PLObject> layout = new ArrayList<>();
        layout.add(plObject);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(lm);

        myExpandableAdapter = new MyExpandableAdapter(layout, getContext());
        mRecyclerView.setAdapter(myExpandableAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);

        TextView title_tv = v.findViewById(R.id.choose_add_set);
        title_tv.setText(title);


        v.findViewById(R.id.fragment_choose_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this needs to be addressed!!!

                //((HomeActivity) getActivity()).workoutsModelController.onExitChooseFragment(plObjectPosition, getArguments().getInt(OLD_POSITION));
                // ((HomeActivity) getActivity()).reviveTabLayout();

                plObject.setEditMode(false);

                getFragmentManager().popBackStack();
            }
        });
        // Attach(workoutView);
        // intent = new Intent();

        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                YPlayer = youTubePlayer;
                YPlayer.setShowFullscreenButton(false);
                YPlayer.loadVideo(searchResults.get(0).getId());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

    }

    private void searchOnYoutube(final String keywords) {
        new Thread() {
            public void run() {
                YoutubeConnector yc = new YoutubeConnector(getContext());
                searchResults = yc.search(keywords);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (YPlayer != null) {
                            YPlayer.cueVideo(searchResults.get(0).getId());
                        } else {
                            youTubePlayerFragment.initialize(YoutubeConfig.getYoutubeApiKey(), onInitializedListener);
                        }

                    }

                    ;
                });
            }
        }.start();


    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /*public void setExerciseProfile(PLObject.ExerciseProfile exerciseProfile) {
        this.exerciseProfile = exerciseProfile;
    }*/

    public void setUniqueTransitionName(String tName) {
        this.tName = tName;

    }
    public void setOnExerciseChangeListener(OnExerciseChangeListener onExerciseChangeListener) {
        this.onExerciseChangeListener = onExerciseChangeListener;
    }

    public void setPlObjectPosition(int plObjectPosition) {
        this.plObjectPosition = plObjectPosition;
    }


    @Override
    public void notifyExerciseSetChange() {
/*
        if (exerciseProfile.getExercise() != null) {
            searchOnYoutube(exerciseProfile.getExercise().getName());
        }

        //myExpandableAdapter.notifyItemChanged(0);
*/
    }

}