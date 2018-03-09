package com.strongest.savingdata.MyViews.WorkoutView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
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
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.DragAndSwipeCallback;
import com.strongest.savingdata.Adapters.WorkoutAdapter.MyExpandableLinearLayoutManager;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnExerciseProfileEditClick;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ScrollToPositionListener;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmProgress.ProgressorManager;
import com.strongest.savingdata.AlgorithmProgress.ProgressorObserver;
import com.strongest.savingdata.AlgorithmProgress.ProgressorSubject;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.PLObjects.ExerciseProfile;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Database.Exercise.BeansHolder;
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
        //initProgramToolsViews();
        expandableLayout = (ExpandableLayout) findViewById(R.id.workoutview_expandable_layout);
        lock = (ImageView) findViewById(R.id.workout_view_lock);
        programToolsView = (ProgramToolsView) findViewById(R.id.workout_view_program_tools);
        programToolsView.setOnProgramToolsActionListener(this);
        programToolsView.setLayoutManager(layoutManager);

        // addExercise = (ViewGroup) findViewById(R.id.workout_view_add_exercise);

        lock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                programToolsView.toggleMode(true);
                //mWorkoutViewModes.setShowAnimation(true);
                //workoutViewFragments.get(mViewPager.getCurrentItem()).adapter.notifyDataSetChanged();

                mAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       /* mWorkoutViewModes.setShowAnimation(false);
                        */
                    }
                }, 1350);
            }
        });


        if (workout == -1) {
          /*  mAdapter = new WorkoutAdapter(
                    mProgramLayout.get(workoutPosition),
                    context,
                    onClickListener,
                    onPositionListener,
                    showStats

            );
*/
            instantiateViewPager();
        } else {
            instantiateRecyclerView(workout);
        }
        progressButton = (Button) findViewById(R.id.progress_button);
        DragAndDrop dragAndDrop = new DragAndDrop(context, currentRecyclerView, this);
        RelativeLayout.LayoutParams laParams = new RelativeLayout.LayoutParams(150, 150);
        progressButton.setLayoutParams(laParams);
        //progressButton.setOnTouchListener(dragAndDrop);
        progressButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDialogFragment f;
                f = new ChooseDialogFragment();
                //f.setTargetFragment(this, 0);
                // String name = ViewCompat.getTransitionName(v);
                //f.setUniqueTransitionName(name);
                Bundle bundle = new Bundle();
                //bundle.putInt("exercise_position", position);
                f.setArguments(bundle);
                //    f.setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.onEditExerciseClick.move));
            /*f.setEnterTransition(new AutoTransition());
            f.setExitTransition(new AutoTransition());*/
                // f.setPlObject(ep);
                //  f.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.onEditExerciseClick.move));
//            fragmentTransaction.add(R.id.workout_nested_layout, f, "unique");
               /* f.setEnterTransition(new AutoTransition());
                f.setExitTransition(new AutoTransition());*/
                fm.beginTransaction()
                        .replace(R.id.workout_nested_layout, f, "unique")
                        .addToBackStack("unique")
                        .addSharedElement(v, "fuckyou")
                        .commit();
            }
        });
    }


    public void enterEditMode() {
        programToolsView.toggleMode(true);
        //mWorkoutViewModes.setShowAnimation(true);
        //workoutViewFragments.get(mViewPager.getCurrentItem()).adapter.notifyDataSetChanged();

        mAdapter.notifyDataSetChanged();
    }

    public void fadeTabLayout() {
        MyJavaAnimator.fadeOut(mTabLayout);
        mTabLayout.setVisibility(GONE);
    }

    private void instantiateRecyclerView(int workout) {

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

    public void hideIcon(boolean hideIcon) {
        this.hideIcon = hideIcon;
        // mAdapter.hideIcon(hideIcon);
    }

    public WorkoutViewPagerAdapter getmAdapter() {
        return mAdapter;
    }

    public WorkoutViewPagerAdapter getWorkoutViewPAgerAdapter() {
        return mAdapter;
    }

    public void updateProgramLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        //mProgramLayout = programLayoutManager.getSplitRecyclerWorkouts();
        //mAdapter.notifyDataSetChanged();
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
        //  layoutManager.updateLayoutStats(true);
       /* mAdapter.setmProgramLayout(layoutManager.getLayout());
        mAdapter.notifyDataSetChanged();*/
        mAdapter.setmProgramLayout(layoutManager.getLayout());
        if (updateComponents.isUpdate()) {
            mAdapter.setUpdate(true);
            mAdapter.notifyDataSetChanged();
            mAdapter.setUpdate(false);

        }
        return layoutManager.getSplitRecyclerWorkouts(true).get(mViewPager.getCurrentItem());
        //   mAdapter.setmProgramLayout(layoutManager.getLayout());
        //   mAdapter.notifyDataSetChanged();
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
                f.addRow(f.exArray.size(), WorkoutLayoutTypes.ExerciseViewLeftMargin);
                break;
            case NEW_EXERCISE_FLIPPED:
                f.addRow(f.exArray.size(), WorkoutLayoutTypes.ExerciseViewRightMargin);
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
       f.scrollToPosition(vh.getAdapterPosition()+position, false, false);

    }

    @Override
    public void onEnterChooseFragment(RecyclerView.ViewHolder vh) {
        WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
        ExerciseProfile ep = (ExerciseProfile) f.exArray.get(vh.getAdapterPosition());
//        f.getAdapter().deTailExpand(vh);
    }

    @Override
    public void onExitChooseFragment(RecyclerView.ViewHolder vh) {
        WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
        f.getAdapter().detailCollapse(vh);
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
                    mProgramLayout, false, false);
            frag.setOnUpdateLayoutStatsListener(onUpdateListener);
            //list.add(position, frag);
            frag.setOnWorkoutViewExplode(onWorkoutViewExplode);
            // workoutViewFragments.add(position, frag);
            return frag;
            //return workoutViewFragments.get(position);
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

    public static class WorkoutViewFragment extends Fragment implements OnChooseClickListener, com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener, ScrollToPositionListener
            , OnPositionViewListener,
            WorkoutObserver, OnAddOrDeleteListener,
            ProgressorSubject, OnExerciseProfileEditClick, OnExerciseChangeListener {

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
                                                      ArrayList<PLObjects> exArray,
                                                      boolean hideIcon, boolean disable) {
            WorkoutViewFragment frag = new WorkoutViewFragment();
            frag.setWorkoutViewModes(workoutViewModes);
            frag.setExArray(exArray);
            frag.setHideIcon(hideIcon);
            frag.setDisable(disable);
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

            //adding a new Method element for testing

            // exArray.add(new PLObjects.Method())

            //

            adapter = new MyExpandableAdapter(
                    exArray,
                    getContext(),
                    this,
                    this,
                    this,
                    this,
                    workoutViewModes
            );
            adapter.setOnExerciseProfileEditClick(this);


            adapter.setWorkoutFragmentHeight(workoutFragmentHeight);

            if (adapter != null) {
                // recycler.setLayoutManager(lm);
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
           /* recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                }
            });*/
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

        public void notifyItemRemoved(int position) {
            adapter.notifyItemRemoved(position);
        }

        public void notifyItemAdded(int position) {
            adapter.notifyItemInserted(position);
        }

        @Override
        public void scrollToPosition(int position, boolean enableScroll, boolean lastVisible) {
            if (lastVisible) {
                ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
                if (position == lm.findLastCompletelyVisibleItemPosition()) {
                    lm.scrollToPosition(position + ep.getBeansHolders().size());
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
            if (progressorObserver != null) {
                onProgressSwap(adapter.getFromPos(), adapter.getToPos());
            }

            if (onUpdateLayoutStatsListener != null) {
                LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.SWAP);
                onUpdateLayoutStatsListener.updateLayoutStats(uc);

            }

        }

        public void setWorkoutViewOnPositionListenener(WorkoutViewOnWorkoutListener listener) {
            this.listener = listener;
        }

        /*    @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.onEditExerciseClick.default_transition));
                    this.setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.onEditExerciseClick.no_transition));
                    ManagerFragment f = new ManagerFragment();
                    f.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.onEditExerciseClick.default_transition));
                    f.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.onEditExerciseClick.no_transition));

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.workout_fragment_layout, f, "unique");
                    fragmentTransaction.addToBackStack("unique");
                    fragmentTransaction.addSharedElement(v, );
                    fragmentTransaction.commit();
                }
                    *//* if (onWorkoutViewExplode != null) {
                onWorkoutViewExplode.explode(v, recycler);
            }*//*
           *//*
            ChooseDialogFragment dialog = new ChooseDialogFragment();

            ExerciseProfile exerciseProfile;
            exerciseProfile = (ExerciseProfile) exArray.get((int) v.getTag());

            bundle.putSerializable("beans_holder", exerciseProfile.getBeansHolder());
            bundle.putInt(MUSCLE, exerciseProfile.getMuscle().ordinal());
            Bundle bundle = new Bundle();
            bundle.putInt("exercise_position", Integer.parseInt(v.getTag().toString()));
            dialog.setArguments(bundle);
            dialog.setTargetFragment(this, 1);
            //mainView.setAlpha(0.2f);
            dialog.show(getFragmentManager(), "Choose ExerciseBean");*//*

        }
*/
        @Override
        public void addOrDeleteResults(int results, int position) {

            if (results == 0) {
                // addRow(position);
            } else {
                removeRow(position);
            }
        }

        private void addRow(int position, WorkoutLayoutTypes innerType) {
          /*  ExerciseProfile tempProfile = (ExerciseProfile) exArray.get(position - 1);
            ExerciseProfile exerciseProfile = new ExerciseProfile(null, null, null,
                    tempProfile.getWorkoutId(),
                    tempProfile.getBodyId(),
                    tempProfile.getBodyId() + exArray.size());

            exerciseProfile.setInnerType(innerType);*/
            if (progressorObserver != null) {
                //   onProgressInserted(position, exerciseProfile);
            }
            if (onUpdateLayoutStatsListener != null) {
                LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.NEW_EXERCISE);
                uc.setInsertPosition(position);
                //uc.setPlObject(exerciseProfile);
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

        private void removeRow(int position) {

            if (exArray.get(position - 1).getType() == WorkoutLayoutTypes.BodyView) {
                Toast.makeText(getContext(), "You cannot delete the first exercise", Toast.LENGTH_SHORT).show();
                return;
            }
            if (progressorObserver != null) {
                //position
                onProgressRemoved(position);
            }

            //exArray.remove(position);
            //programmer.updateRecyclerLayout(exArray);
            if (onUpdateLayoutStatsListener != null) {
                LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.REMOVE);
                uc.setRemovePosition(position);
                exArray = onUpdateLayoutStatsListener.updateLayoutStats(uc);
            }
            adapter.setExArray(exArray);
            adapter.notifyItemRemoved(position);

            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
            //adapter.myNotifyItemRemoved(exArray, position);

        }

        @Override
        public void longClick(View v, int pos) {

           /* Bundle b = new Bundle();
            b.putInt(AddOrDeleteFragment.POS, pos);
            AddOrDeleteFragment aodf = new AddOrDeleteFragment();
            aodf.setArguments(b);
            aodf.setAddOrDeleteListener(this);
            //idf.setTargetFragment(fragment, CreateFragment.INTERRACT_FRAGMENT);
            aodf.show(getFragmentManager(), "Choose ExerciseBean");*/
            //addRow(pos);
        }

        @Override
        public void exerciseOnClick(ArrayList<BeansHolder> beansHolders, int position, boolean changed) {
            ExerciseProfile exerciseProfile = (ExerciseProfile) exArray.get(position);
          /*  if (progressorObserver != null && changed) {
                onProgressChanged(exerciseProfile.getBeansHolder(), v);
            }*/
            exerciseProfile.setBeansHolders(beansHolders);
            //adapter.setExArray(exArray);
            if (onUpdateLayoutStatsListener != null) {
              /*  LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.Update.Change);
                exArray = onUpdateLayoutStatsListener.updateLayoutStats(uc);*/

            }
            adapter.setExArray(exArray);
            adapter.notifyDataSetChanged();

            //  ExerciseProfileEventMessage eventM = new ExerciseProfileEventMessage(exerciseProfile, pos);
            // EventBus.getDefault().post(eventM);
            //  programmer.getProgramLayoutManager().updateLayoutStats(true);
            //mAdapter.getList().get(workoutPosition).updateView(programLayoutManager.getSplitRecyclerWorkouts().get(workoutPosition));

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void click(View v, final ExerciseProfile exerciseProfile, String transitionName) {

            f.setTargetFragment(this, 0);
            Bundle bundle = new Bundle();
            bundle.putInt("exercise_position", Integer.parseInt(v.getTag().toString()));
            f.setArguments(bundle);
            //    f.setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.onEditExerciseClick.move));
            f.setEnterTransition(new AutoTransition());
            f.setExitTransition(new AutoTransition());
            f.setExerciseProfile(exerciseProfile);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.workout_fragment_layout, f, "unique");
//            fragmentTransaction.addSharedElement(v, transitionName);
            //fragmentTransaction.replace(R.id.activity_home_layout, f, "unique");
            fragmentTransaction.addToBackStack("unique");
            //fragmentTransaction.addSharedElement(v, tName);
            fragmentTransaction.commitAllowingStateLoss();

        }


        public void setOnWorkoutViewExplode(OnWorkoutViewExplode onWorkoutViewExplode) {
            this.onWorkoutViewExplode = onWorkoutViewExplode;
        }

        public void setOnEnterExitChooseFragment(OnEnterExitChooseFragment onEnterExitChooseFragment) {
            this.onEnterExitChooseFragment = onEnterExitChooseFragment;
        }

        public void setExArray(ArrayList<PLObjects> exArray) {
            this.exArray = exArray;
        }

        public void setHideIcon(boolean hideIcon) {
            this.hideIcon = hideIcon;
        }

        public void setDisable(boolean disable) {
            this.disable = disable;
        }

        public void setProgressorObserver(ProgressorObserver progressorObserver) {
            this.progressorObserver = progressorObserver;
        }


        @Override
        public void onProgressChanged(BeansHolder prev, BeansHolder next) {

        }

        @Override
        public void onProgressInserted(int pos, ExerciseProfile ep) {

        }

        @Override
        public void onProgressRemoved(int pos) {

        }

        @Override
        public void onProgressSwap(int from, int to) {

        }

        public void setOnUpdateLayoutStatsListener(OnUpdateLayoutStatsListener onUpdateLayoutStatsListener) {
            this.onUpdateLayoutStatsListener = onUpdateLayoutStatsListener;
        }

        @Subscribe
        public void passEvent(int pos) {
            Toast.makeText(getContext(), "this is working i confirm", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDetach() {
            EventBus.getDefault().unregister(this);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    MyJavaAnimator.fadeOut(mainView);
                }
            });
            super.onDetach();

        }

        @Override
        public void onAttach(Context context) {
            EventBus.getDefault().register(this);
            super.onAttach(context);

        }

        @Override
        public void onEditExerciseClick(boolean enter, RecyclerView.ViewHolder vh, int position, ExerciseProfile ep) {
            if (!enter) {
                // onEnterExitChooseFragment.onExitChooseFragment(vh);
                getFragmentManager().popBackStack();
            } else {
                //      expandableLayout.collapse();
                //    programToolsView.toggleMode(false);
                //  lock.setEnabled(false);
                f = ChooseDialogFragment.getInstance(this, position, ep, vh);
                f.setTargetFragment(this, 0);


                /*getFragmentManager().beginTransaction()
                        .add(R.id.choose_container, f, "unique")
                        .addToBackStack("unique")
                        .commit();*/

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


     /*   @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public void explode(View v, RecyclerView recyclerView, final FragmentTransaction f) {
            final Rect rect = new Rect();
            v.getGlobalVisibleRect(rect);
            rect.top = rect.bottom;
            Transition explode = new Explode();
            explode.setEpicenterCallback(new Transition.EpicenterCallback() {
                @Override
                public Rect onGetEpicenter(Transition onEditExerciseClick) {
                    return rect;
                }
            });
            explode.setDuration(100).addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition onEditExerciseClick) {
                }

                @Override
                public void onTransitionEnd(Transition onEditExerciseClick) {
                    f.commit();

                }

                @Override
                public void onTransitionCancel(Transition onEditExerciseClick) {

                }

                @Override
                public void onTransitionPause(Transition onEditExerciseClick) {

                }

                @Override
                public void onTransitionResume(Transition onEditExerciseClick) {

                }
            });

            explode.excludeTarget(v, true);
            TransitionManager.beginDelayedTransition(recyclerView, explode);

            // remove all views from Recycler View
            recyclerView.setAdapter(null);

        }*/

    }


}
