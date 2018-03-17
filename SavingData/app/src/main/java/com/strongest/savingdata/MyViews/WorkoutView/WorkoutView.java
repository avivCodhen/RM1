package com.strongest.savingdata.MyViews.WorkoutView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.AutoTransition;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.strongest.savingdata.Activities.BaseActivity;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.DragAndSwipeCallback;
import com.strongest.savingdata.Adapters.WorkoutAdapter.MyExpandableLinearLayoutManager;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnExerciseProfileEditClick;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ScrollToPositionListener;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmLayout.ReactLayoutManager;
import com.strongest.savingdata.AlgorithmProgress.ProgressorManager;
import com.strongest.savingdata.AlgorithmProgress.ProgressorObserver;
import com.strongest.savingdata.AlgorithmProgress.ProgressorSubject;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.PLObjects.ExerciseProfile;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Database.Exercise.Sets;
import com.strongest.savingdata.DragNDrop.DragAndDrop;
import com.strongest.savingdata.MyViews.WorkoutViewOnWorkoutListener;
import com.strongest.savingdata.R;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.ChooseDialogFragment;
import com.strongest.savingdata.createProgramFragments.Create.OnPositionViewListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.DELETE_WORKOUT;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.DRAW_DIVIDER;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.NEW_EXERCISE;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.NEW_EXERCISE_FLIPPED;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.NEW_WORKOUT;

/**
 * Created by Cohen on 12/16/2017.
 */

public class WorkoutView extends LinearLayout implements View.OnClickListener, WorkoutViewOnWorkoutListener,
        OnUpdateLayoutStatsListener, OnProgramToolsActionListener, OnEnterExitChooseFragment {

    //private ItemTouchHelper mTouchHelper;
    private Context context;
    private Fragment fragment;
    private int workout;
    private FragmentManager fm;
    private LayoutManager layoutManager;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutParams layoutParams;
    private Button progressButton;
    private WorkoutViewPagerAdapter mAdapter;
    private ProgressorManager progressorManager;
    private DroppyMenuPopup droppyMenuPopup;
    private ViewGroup dropMenuButton;
    private static ImageView lock;
    // private ViewGroup addExercise;
    private boolean editMode;
    private static ArrayList<WorkoutViewFragment> workoutViewFragments;
    public static int workoutFragmentHeight;


    private int workoutPosition;
    private RecyclerView currentRecyclerView;

    private static boolean showBody;
    private boolean showStats = true;
    private boolean hideIcon;
    private static ExpandableLayout expandableLayout;

    private static ProgramToolsView programToolsView;


    public WorkoutView(Context context) {
        super(context);
        this.context = context;
    }

    public WorkoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    private void createView() {
        workoutViewFragments = new ArrayList<>();
        inflate(context, R.layout.workout_view, this);
        expandableLayout = (ExpandableLayout) findViewById(R.id.workoutview_expandable_layout);
        lock = (ImageView) findViewById(R.id.workout_view_lock);
        programToolsView = (ProgramToolsView) findViewById(R.id.workout_view_program_tools);
        programToolsView.setOnProgramToolsActionListener(this);
        programToolsView.setLayoutManager(layoutManager);


        lock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                programToolsView.toggleMode(true);


                mAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 1350);
            }
        });

        instantiateViewPager();
        progressButton = (Button) findViewById(R.id.progress_button);
        DragAndDrop dragAndDrop = new DragAndDrop(context, currentRecyclerView, this);
        RelativeLayout.LayoutParams laParams = new RelativeLayout.LayoutParams(150, 150);
        progressButton.setLayoutParams(laParams);
    }


    public void enterEditMode() {
        programToolsView.toggleMode(true);

    }

    private void instantiateViewPager() {
        //configurateRecycler();
        RecyclerView.LayoutManager layM = new LinearLayoutManager(context);
        inflate(context, R.layout.workout_view, this);
        mViewPager = (ViewPager) findViewById(R.id.workoutview_viewpager);

        mTabLayout = (TabLayout) findViewById(R.id.workoutview_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);


        dropMenuButton = (ViewGroup) findViewById(R.id.workout_view_workoutmenubutton);
        DroppyMenuPopup.Builder builder = new DroppyMenuPopup.Builder(getContext(), dropMenuButton);
        for (int i = 0; i < layoutManager.getNumOfWorkouts(); i++) {
            builder.addMenuItem(new DroppyMenuItem(ProgramTemplate.ProgramTemplateFactory.WhatsYourWorkoutName(i)));
        }

        builder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                ((TextView) dropMenuButton.getChildAt(0)).setText(ProgramTemplate.ProgramTemplateFactory.WhatsYourWorkoutName(id));
                mViewPager.setCurrentItem(id);
            }
        });
        droppyMenuPopup = builder.build();


        mAdapter = new WorkoutViewPagerAdapter(this,
                programToolsView.getmWorkoutViewModes(),
                this,
                fm,
                layoutManager,
                this);

        mViewPager.setOffscreenPageLimit(layoutManager.getNumOfWorkouts());
        mViewPager.setAdapter(mAdapter);
        LinearLayout tabStrip = (LinearLayout) mTabLayout.getChildAt(0);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setTag(i);
            tabStrip.getChildAt(i).setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "lkj" + v.getTag(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((TextView) dropMenuButton.getChildAt(0))
                        .setText(ProgramTemplate.ProgramTemplateFactory
                                .WhatsYourWorkoutName(position));
                ObjectAnimator colorAnim = ObjectAnimator.ofInt(((TextView) dropMenuButton.getChildAt(0)), "textColor",
                        Color.RED, Color.WHITE);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
                WorkoutViewFragment f = (WorkoutViewFragment) mAdapter.getItem(position);
                currentRecyclerView = f.getRecycler();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        expandableLayout.expand();
    }


    //-1 = viewpager with all workouts, 0+ = specific workout
    public void instantiate(int workout, FragmentManager fm, boolean mBody, LayoutManager layoutManager) {
        // this.fragment = fragment;
        this.showBody = mBody;
        this.workout = workout;
        this.fm = fm;
        this.layoutManager = layoutManager;
        createView();
    }

    public WorkoutViewPagerAdapter getmAdapter() {
        return mAdapter;
    }

    public WorkoutViewPagerAdapter getWorkoutViewPAgerAdapter() {
        return mAdapter;
    }

    public void updateProgramLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        mAdapter.setmProgramLayout(layoutManager.getLayout());
        mAdapter.notifyDataSetChanged();
    }

    public int getWorkoutPosition() {
        return workoutPosition;
    }

    @Override
    public void onClick(View v) {


    }


    @Override
    public void updateWorkoutPosition(int workoutPosition) {
        this.workoutPosition = workoutPosition;
    }

    public void setProgressorManager(ProgressorManager progressorManager) {
        this.progressorManager = progressorManager;
    }

    @Override
    public ArrayList<PLObjects> updateLayoutStats(LayoutManager.UpdateComponents updateComponents) {
        updateComponents.setWorkoutPosition(mViewPager.getCurrentItem());
        layoutManager.updateLayout(updateComponents);

        mAdapter.setmProgramLayout(layoutManager.getLayout());
        if (updateComponents.isUpdate()) {
            mAdapter.setUpdate(true);
            mAdapter.notifyDataSetChanged();
            mAdapter.setUpdate(false);

        }
        return layoutManager.getSplitRecyclerWorkouts(true).get(mViewPager.getCurrentItem());

    }

    /*
     * types are adding new exercise, new flipped exercise
     * new workout, attaching superset, attaching dropset
     * deleting workout
     * this callback delivers commands to make the necessary actions
     * */

    @Override
    public void onProgramToolsAction(String command) {
        WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
        LayoutManager.UpdateComponents upd;

        switch (command) {
            case NEW_EXERCISE:
                f.addRow(f.exArray.size(), WorkoutLayoutTypes.ExerciseProfile);
                break;
            case NEW_EXERCISE_FLIPPED:
                f.addRow(f.exArray.size(), WorkoutLayoutTypes.ExerciseProfile);
                break;
            case NEW_WORKOUT:
                upd = new LayoutManager.UpdateComponents(NEW_WORKOUT);
                upd.setUpdate(true);
                updateLayoutStats(upd);
                mAdapter.setmProgramLayout(layoutManager.getLayout());
                // mAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(workoutViewFragments.size() - 1);
                //f.adapter.notifyItemInserted(0);
                break;
            case DELETE_WORKOUT:
                if (layoutManager.getWorkouts(false).size() == 1) {
                    Toast.makeText(context, "You cannot delete the only workout", Toast.LENGTH_SHORT).show();
                } else {
                    final LayoutManager.UpdateComponents updt = new LayoutManager.UpdateComponents(DELETE_WORKOUT);
                    f.deleteTransition();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updt.setUpdate(true);
                            updateLayoutStats(updt);
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem());
                        }
                    }, 300);
                }
                break;
            case DRAW_DIVIDER:
                upd = new LayoutManager.UpdateComponents(DRAW_DIVIDER);
                f.addBodyView(f.exArray.size(), upd);
                break;
        }
    }

    public void scrollToPositionCallBack(RecyclerView.ViewHolder vh, int position) {
        WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
        f.scrollToPosition(vh.getAdapterPosition() + position, false, false);

    }

    @Override
    public void onEnterChooseFragment(RecyclerView.ViewHolder vh) {
        WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
        ExerciseProfile ep = (ExerciseProfile) f.exArray.get(vh.getAdapterPosition());
//        f.getAdapter().deTailExpand(vh);
    }

    @Override
    public void onExitChooseFragment(RecyclerView.ViewHolder vh, boolean isCollapsed) {
        if (!isCollapsed) {
            WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
            f.getAdapter().detailCollapse(vh);
        }
//        layoutManager.saveLayoutToDataBase(true);

    }

    public TabLayout getmTabLayout() {
        return mTabLayout;
    }

    /*/
            new class
            this class is an adapter that controls the workoutviewfragments
         */
    public static class WorkoutViewPagerAdapter extends FragmentStatePagerAdapter {

        private OnWorkoutViewExplode onWorkoutViewExplode;
        private final FragmentManager fm;
        private WorkoutViewOnWorkoutListener listener;
        private ArrayList<WorkoutViewFragment> list = new ArrayList<>();
        private ArrayList<PLObjects> mProgramLayout;
        private LayoutManager plm;
        private OnUpdateLayoutStatsListener onUpdateListener;
        private boolean update;
        private OnEnterExitChooseFragment onEnterExitChooseFragment;
        private ProgramToolsView.WorkoutViewModes workoutViewModes;

        public WorkoutViewPagerAdapter(OnEnterExitChooseFragment onEnterExitChooseFragment,
                                       ProgramToolsView.WorkoutViewModes workoutViewModes, OnUpdateLayoutStatsListener onUpdateListener,
                                       FragmentManager fm, LayoutManager plm,
                                       WorkoutViewOnWorkoutListener listener) {
            super(fm);
            this.onEnterExitChooseFragment = onEnterExitChooseFragment;
            this.workoutViewModes = workoutViewModes;
            this.onWorkoutViewExplode = onWorkoutViewExplode;
            this.fm = fm;
            this.plm = plm;
            this.onUpdateListener = onUpdateListener;
            /*mProgramLayout = mProgramLayout;
            this.mProgramLayout = mProgramLayout;*/
            this.listener = listener;
        }

        public void setmProgramLayout(ArrayList<PLObjects> mProgramLayout) {
            this.mProgramLayout = mProgramLayout;
        }

        public void setUpdate(boolean update) {
            this.update = update;
        }

        @Override
        public int getCount() {
            return plm.getWorkouts(false).size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ProgramTemplate.ProgramTemplateFactory.WhatsYourWorkoutName(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            WorkoutViewFragment frag = null;
            mProgramLayout = plm.getSplitRecyclerWorkouts(true).get(position);
            frag = WorkoutViewFragment.getInstance(onEnterExitChooseFragment, workoutViewModes, onUpdateListener,
                    mProgramLayout);
            frag.setOnUpdateLayoutStatsListener(onUpdateListener);
            return frag;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            WorkoutViewFragment f = (WorkoutViewFragment) super.instantiateItem(container, position);
            workoutViewFragments.add(position, f);
            return f;
        }

        public ArrayList<WorkoutViewFragment> getList() {
            return list;
        }

     /*   public void notiftyMyItemRemoved(int fragment, int position) {
            WorkoutViewFragment f = list.get(fragment);
            //f.updateView(programLayoutManager.getSplitRecyclerWorkouts().get(workoutPosition));
            f.notifyItemRemoved(position);
        }

        public void notifyItemAdded(int fragment, int position) {
            WorkoutViewFragment f = list.get(fragment);
            f.notifyItemAdded(position);
        }*/
    }

    public void setWorkout(int workout) {
        this.workout = workout;
    }

    public int getWorkoutFragmentHeight() {
        return workoutFragmentHeight;
    }

    public void setWorkoutFragmentHeight(int workoutFragmentHeight) {
        WorkoutView.workoutFragmentHeight = workoutFragmentHeight;
    }


    /*/
       new class
        this class is a fragment that controls the recyclerview, it's adapter and all the callbacks.
        callbacks include: drag&drop, onclick, on long click.
     */

    public static class WorkoutViewFragment extends Fragment implements com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener, ScrollToPositionListener
            , OnPositionViewListener,
            OnExerciseProfileEditClick, OnExerciseChangeListener, OnMoreClickListener {

        private RecyclerView recycler;
        //private WorkoutAdapter adapter;
        private MyExpandableAdapter adapter;
        private ItemTouchHelper touchHelper;
        private ArrayList<PLObjects> exArray;
        private WorkoutViewOnWorkoutListener listener;
        private boolean hideIcon;
        private boolean disable;
        private ProgressorObserver progressorObserver;
        private OnUpdateLayoutStatsListener onUpdateLayoutStatsListener;
        private OnWorkoutViewExplode onWorkoutViewExplode;
        private ChooseDialogFragment f;
        private MyExpandableLinearLayoutManager lm;
        private View mainView;
        private ProgramToolsView.WorkoutViewModes workoutViewModes;
        private OnEnterExitChooseFragment onEnterExitChooseFragment;


        public static WorkoutViewFragment getInstance(OnEnterExitChooseFragment onEnterExitChooseFragment, ProgramToolsView.WorkoutViewModes workoutViewModes,
                                                      OnUpdateLayoutStatsListener onUpdateListener,
                                                      ArrayList<PLObjects> exArray) {
            WorkoutViewFragment frag = new WorkoutViewFragment();
            frag.setWorkoutViewModes(workoutViewModes);
            frag.setExArray(exArray);
            frag.setOnUpdateLayoutStatsListener(onUpdateListener);
            frag.setOnEnterExitChooseFragment(onEnterExitChooseFragment);
            return frag;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            mainView = inflater.inflate(R.layout.workoutview_list, container, false);
            return mainView;

        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            initViews(view);
        }

        private void initViews(View view) {
            recycler = (RecyclerView) view.findViewById(R.id.workoutview_list_recycler);
            adapter = new MyExpandableAdapter(
                    exArray,
                    getContext(),
                    this,
                    this,
                    this,
                    workoutViewModes
            );
            adapter.setOnExerciseProfileEditClick(this);
            if (adapter != null) {
                configurateRecycler();
            } else {
                Log.d("aviv", "initViews:  trouble in workoutviewfragment");
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

        }

        @Override
        public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
            super.onViewStateRestored(savedInstanceState);
        }

        private void configurateRecycler() {
            lm = new MyExpandableLinearLayoutManager(getContext());
            recycler.setLayoutManager(lm);
            recycler.setAdapter(adapter);
            adapter.setOnDragListener(this);
            adapter.setScrollListener(this);
            ItemTouchHelper.Callback callback = new DragAndSwipeCallback(adapter);
            touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recycler);
        }

        public RecyclerView getRecycler() {
            return recycler;
        }

        public MyExpandableAdapter getAdapter() {
            return adapter;
        }


        @Override
        public void scrollToPosition(int position, boolean enableScroll, boolean lastVisible) {
            if (lastVisible) {
                ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
                if (position == lm.findLastCompletelyVisibleItemPosition()) {
                    lm.scrollToPosition(position + ep.getSets().size());
                }
            } else if (enableScroll) {
                lm.setScrollEnabled(true);
                if (position != -1) {
                    lm.scrollToPosition(position);
                }
            } else {
                lm.scrollToPositionWithOffset(position, 0);
                lm.setScrollEnabled(false);
            }
        }

        @Override
        public void startDrag(RecyclerView.ViewHolder viewHolder) {
            touchHelper.startDrag(viewHolder);
            if (onUpdateLayoutStatsListener != null) {
                LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.SWAP);
                onUpdateLayoutStatsListener.updateLayoutStats(uc);

            }

        }

        private void addRow(int position, WorkoutLayoutTypes innerType) {


            if (onUpdateLayoutStatsListener != null) {
                LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.NEW_EXERCISE);
                uc.setInsertPosition(position);
                uc.setInnerType(innerType);
                exArray = onUpdateLayoutStatsListener.updateLayoutStats(uc);
            }
            adapter.myNotifyItemInserted(exArray, position);
            scrollToPosition(position, true, false);
        }

        public void addBodyView(int position, LayoutManager.UpdateComponents upd) {
            if (onUpdateLayoutStatsListener != null) {
                upd.setInsertPosition(position);
                exArray = onUpdateLayoutStatsListener.updateLayoutStats(upd);
            }
            adapter.myNotifyItemInserted(exArray, position);
            scrollToPosition(position, true, false);
        }

        @Override
        public void longClick(View v, int pos) {

        }


        public void setOnEnterExitChooseFragment(OnEnterExitChooseFragment onEnterExitChooseFragment) {
            this.onEnterExitChooseFragment = onEnterExitChooseFragment;
        }

        public void setExArray(ArrayList<PLObjects> exArray) {
            this.exArray = exArray;
        }


        public void setOnUpdateLayoutStatsListener(OnUpdateLayoutStatsListener onUpdateLayoutStatsListener) {
            this.onUpdateLayoutStatsListener = onUpdateLayoutStatsListener;
        }


        @Override
        public void onDetach() {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    MyJavaAnimator.fadeOut(mainView);
                }
            });
            super.onDetach();

        }

        @Override
        public void onEditExerciseClick(boolean enter, RecyclerView.ViewHolder vh, int position, ExerciseProfile ep) {
            if (!enter) {
                onEnterExitChooseFragment.onExitChooseFragment(vh, true);
                getFragmentManager().popBackStack();
            } else {

                f = ChooseDialogFragment.getInstance(this, position, ep, vh);
                f.setTargetFragment(this, 0);
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.activity_home_framelayout, f, "unique")
                        .addToBackStack("unique")
                        .commit();
                onEnterExitChooseFragment.onEnterChooseFragment(vh);
            }
        }

        //updates the item that has been changed in the choosedialogfragment
        @Override
        public void onExerciseChange(int position, String change) {
            adapter.notifyItemChanged(position, change);
        }

        public void deleteTransition() {
            MyJavaAnimator.fadeOut(mainView);
        }

        public ProgramToolsView.WorkoutViewModes getWorkoutViewModes() {
            return workoutViewModes;
        }

        public void setWorkoutViewModes(ProgramToolsView.WorkoutViewModes workoutViewModes) {
            this.workoutViewModes = workoutViewModes;
        }

        @Override
        public void showMoreMenu(RecyclerView.ViewHolder vh) {

        }
    }
}
