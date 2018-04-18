package com.strongest.savingdata.MyViews.WorkoutView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.shehabic.droppy.DroppyMenuPopup;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.DragAndSwipeCallback;
import com.strongest.savingdata.Adapters.WorkoutAdapter.MyExpandableLinearLayoutManager;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnExerciseProfileEditClick;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ScrollToPositionListener;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmLayout.LayoutManagerAlertdialog;
import com.strongest.savingdata.AlgorithmLayout.LayoutManagerHelper;
import com.strongest.savingdata.AlgorithmLayout.LayoutManagerOperator;
import com.strongest.savingdata.AlgorithmLayout.OnLayoutManagerDialogPress;
import com.strongest.savingdata.AlgorithmProgress.ProgressorManager;
import com.strongest.savingdata.AlgorithmProgress.ProgressorObserver;
import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AlgorithmLayout.PLObject.ExerciseProfile;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.MyViews.LongClickMenu.OnLongClickMenuActionListener;
import com.strongest.savingdata.MyViews.WorkoutViewOnWorkoutListener;
import com.strongest.savingdata.R;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.ChooseDialogFragment;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;

import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.DELETE_EXERCISE;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.DELETE_WORKOUT;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.DRAW_DIVIDER;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.NEW_EXERCISE;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.NEW_SUPERSET;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.NEW_WORKOUT;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.SWAP;
import static com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView.Action.Back;
import static com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView.Action.Intraset;

/**
 * Created by Cohen on 12/16/2017.
 */

public class WorkoutView implements WorkoutViewOnWorkoutListener,
        OnUpdateLayoutStatsListener, OnProgramToolsActionListener, OnEnterExitChooseFragment, OnLongClickMenuActionListener {

    //private ItemTouchHelper mTouchHelper;
    private Context context;
    private FragmentManager fm;
    private static LayoutManager layoutManager;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Button progressButton;
    private WorkoutViewPagerAdapter mAdapter;
    private ProgressorManager progressorManager;
    private DroppyMenuPopup droppyMenuPopup;
    private ViewGroup dropMenuButton;
    private static ImageView lock;
    // private ViewGroup addExercise;
    private boolean editMode;
    private static ArrayList<WorkoutViewFragment> workoutViewFragments;
    private static AppBarLayout mAppBarLayout;
    private static LongClickMenuView longClickMenu;


    private int workoutPosition;
    private RecyclerView currentRecyclerView;

    private static boolean showBody;
    private boolean showStats = true;
    private boolean hideIcon;
    private static ExpandableLayout expandableLayout;

    private static ProgramToolsView programToolsView;
    // private static LayoutManager.LayoutManagerHelper layoutManagerHelper;

    public static View getLongClickMenu() {
        return longClickMenu;
    }

    public void setLongClickMenu(LongClickMenuView longClickMenu) {
        WorkoutView.longClickMenu = longClickMenu;
        longClickMenu.instantiate(this);
    }

    public void instantiate(Context context, FragmentManager fm, LayoutManager layoutManager, ViewPager viewPager, TabLayout tabLayout) {
        this.context = context;
        this.fm = fm;
        this.mTabLayout = tabLayout;
        this.mViewPager = viewPager;
        this.layoutManager = layoutManager;
        createView();
    }

    private void createView() {
        workoutViewFragments = new ArrayList<>();
        // inflate(context, R.layout.workout_view, this);

        //   programToolsView = (ProgramToolsView) findViewById(R.id.workout_view_program_tools);
        instantiateViewPager();
    }


    public void enterEditMode() {
        programToolsView.expand();

    }

    private void instantiateViewPager() {
        //configurateRecycler();
        final RecyclerView.LayoutManager layM = new LinearLayoutManager(context);
        //   inflate(context, R.layout.workout_view, this);
        //   layoutManagerHelper = layoutManager.mLayoutManagerHelper;
        //  mViewPager = (ViewPager) findViewById(R.id.workoutview_viewpager);

        //  mTabLayout = (TabLayout) findViewById(R.id.workoutview_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

        mAdapter = new WorkoutViewPagerAdapter(this,
                this,
                fm,
                layoutManager,
                this);

        mViewPager.setOffscreenPageLimit(layoutManager.getNumOfWorkouts());
        mViewPager.setAdapter(mAdapter);
    /*    LinearLayout tabStrip = (LinearLayout) mTabLayout.getChildAt(0);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setTag(i);
            tabStrip.getChildAt(i).setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "lkj" + v.getTag(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }*/
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                longClickMenu.onHideMenu();
                //layoutManager.mLayoutManagerHelper.updateLayoutManagerHelper(mViewPager.getCurrentItem());
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
             /*   ((TextView) dropMenuButton.getChildAt(0))
                        .setText(ProgramTemplate.ProgramTemplateFactory
                                .WhatsYourWorkoutName(position));
                ObjectAnimator colorAnim = ObjectAnimator.ofInt(((TextView) dropMenuButton.getChildAt(0)), "textColor",
                        Color.RED, Color.WHITE);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();*/
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

//        expandableLayout.expand();
    }


    public void setmAppBarLayout(AppBarLayout mAppBarLayout) {
        this.mAppBarLayout = mAppBarLayout;
    }

    public WorkoutViewPagerAdapter getmAdapter() {
        return mAdapter;
    }

    public WorkoutViewPagerAdapter getWorkoutViewPAgerAdapter() {
        return mAdapter;
    }

    public void setProgramToolsView(ProgramToolsView programToolsView) {
        WorkoutView.programToolsView = programToolsView;
        programToolsView.setFragmentManager(fm);
        programToolsView.setOnProgramToolsActionListener(this);
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
    public void updateWorkoutPosition(int workoutPosition) {
        this.workoutPosition = workoutPosition;
    }

    @Override
    public ArrayList<PLObject> updateLayout(LayoutManager.UpdateComponents updateComponents) {
        updateComponents.setWorkoutPosition(mViewPager.getCurrentItem());
        layoutManager.updateLayout(updateComponents);

        //mAdapter.setmProgramLayout(layoutManager.getLayout());
        if (updateComponents.isUpdate()) {
            mAdapter.setUpdate(true);
            mAdapter.notifyDataSetChanged();
            mAdapter.setUpdate(false);

        }
        layoutManager.saveLayoutToDataBase(true);
        return layoutManager.getSplitRecyclerWorkouts().get(mViewPager.getCurrentItem());

    }

    /*
     * types are adding new exercise, new flipped exercise
     * new workout, attaching superset, attaching dropset
     * deleting workout
     * this callback delivers commands to make the necessary actions
     * */

    @Override
    public void onProgramToolsAction(String command, LayoutManager.UpdateComponents updateComponents) {
        WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
        LayoutManager.UpdateComponents upd;

        switch (command) {

            case NEW_EXERCISE:


                LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.NEW_EXERCISE);
                uc.setInsertPosition(f.exArray.size());
                uc.setInnerType(WorkoutLayoutTypes.ExerciseProfile);
                f.setExArray(updateLayout(uc));
                f.adapter.myNotifyItemInserted(f.exArray, f.exArray.size());
                f.scrollToPosition(f.exArray.size(), true, false);
                //  f.addRow(f.exArray.size(), WorkoutLayoutTypes.ExerciseProfile);
                break;
            case NEW_WORKOUT:
                upd = new LayoutManager.UpdateComponents(NEW_WORKOUT);
                upd.setUpdate(true);
                updateLayout(upd);
                //  mAdapter.setmProgramLayout(layoutManager.getLayout());
                mViewPager.setCurrentItem(workoutViewFragments.size() - 1);
                break;
            case DELETE_WORKOUT:
                if (layoutManager.getWorkouts().size() == 1) {
                    Toast.makeText(context, "You cannot delete the only workout", Toast.LENGTH_SHORT).show();
                } else {
                    final LayoutManager.UpdateComponents updt = new LayoutManager.UpdateComponents(DELETE_WORKOUT);
                    f.deleteTransition();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updt.setUpdate(true);
                            updateLayout(updt);
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem());
                        }
                    }, 300);
                }
                break;
            case DRAW_DIVIDER:
                upd = new LayoutManager.UpdateComponents(DRAW_DIVIDER);
                f.addBodyView(f.exArray.size(), upd);
                break;
            case SWAP:
                Collections.swap(workoutViewFragments, updateComponents.getFromPosition(), updateComponents.getToPosition());
                mAdapter.notifyDataSetChanged();
            case "notify":
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void scrollToPositionCallBack(int position) {
        WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
        f.scrollToPosition(position, false, false);

    }

    public void addSet(int position, PLObject.SetsPLObject setsPLObject) {
        WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
        f.exArray.add(position, setsPLObject);
        f.adapter.notifyItemInserted(position);
    }

    @Override
    public void onEnterChooseFragment(final int position) {
        final WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
//        ExerciseProfile ep = (ExerciseProfile) f.exArray.get(vh.getAdapterPosition());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                f.recycler.findViewHolderForAdapterPosition(position).itemView.setAlpha(0f);
            }
        }, 200);
        //MyJavaAnimator.fadeOut(vh.itemView);
//        f.getAdapter().deTailExpand(vh);
    }

    @Override
    public void onExitChooseFragment(final int position, int oldPosition) {
        WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
        if (f.exArray.get(position + 1) instanceof PLObject.AddExercise) {
            f.getAdapter().detailCollapse(position);
            f.scrollToPosition(oldPosition, true, false);
        }
        layoutManager.saveLayoutToDataBase(true);
        f.adapter.notifyItemRangeChanged(0, f.exArray.size() + 1);
        //  f.adapter.notifyItemRangeChanged(vh.getAdapterPosition(), layoutManagerHelper.countExerciseRangeChange(vh.getAdapterPosition(), f.exArray));
        f.recycler.findViewHolderForAdapterPosition(position).itemView.setAlpha(1f);

    }

    public TabLayout getmTabLayout() {
        return mTabLayout;
    }

    @Override
    public void onAction(LongClickMenuView.Action action, RecyclerView.ViewHolder currentVH, WorkoutLayoutTypes type) {
        WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
        switch (action) {
            case Back:
                longClickMenu.onHideMenu();
                f.notifyExerciseChanged(currentVH.getAdapterPosition());
                break;
            case Superset:
                f.onLongClickMenuAddSuperset(currentVH);
                break;
            case Delete:
                f.deleteItem(currentVH.getAdapterPosition(), false);
                break;
            case Intraset:
                f.onAddNormalIntraSet(currentVH);
                break;
            case Set:
                f.onSetsDoubleClick(currentVH);
                break;
            case Duplicate:
                f.duplicateItem(currentVH, type);

                break;

        }
    }

    /*/
            new class
            this class is an adapter that controls the workoutviewfragments
         */
    public static class WorkoutViewPagerAdapter extends FragmentStatePagerAdapter {

        private final FragmentManager fm;
        private WorkoutViewOnWorkoutListener listener;
        private ArrayList<WorkoutViewFragment> list = new ArrayList<>();
        private ArrayList<PLObject> mProgramLayout;
        private LayoutManager plm;
        private OnUpdateLayoutStatsListener onUpdateListener;
        private boolean update;
        private OnEnterExitChooseFragment onEnterExitChooseFragment;

        public WorkoutViewPagerAdapter(OnEnterExitChooseFragment onEnterExitChooseFragment,
                                       OnUpdateLayoutStatsListener onUpdateListener,
                                       FragmentManager fm, LayoutManager plm,
                                       WorkoutViewOnWorkoutListener listener) {
            super(fm);
            this.onEnterExitChooseFragment = onEnterExitChooseFragment;
            this.fm = fm;
            this.plm = plm;
            this.onUpdateListener = onUpdateListener;
            this.listener = listener;
        }

        public void setmProgramLayout(ArrayList<PLObject> mProgramLayout) {
            this.mProgramLayout = mProgramLayout;
        }

        public void setUpdate(boolean update) {
            this.update = update;
        }

        @Override
        public int getCount() {
            return plm.getWorkouts().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ((PLObject.WorkoutPLObject) layoutManager.getWorkouts().get(position)).getWorkoutName();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            WorkoutViewFragment frag = null;
            mProgramLayout = plm.getSplitRecyclerWorkouts().get(position);
            frag = WorkoutViewFragment.getInstance(onEnterExitChooseFragment, onUpdateListener,
                    mProgramLayout);
            frag.setOnUpdateLayoutStatsListener(onUpdateListener);
            return frag;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            WorkoutViewFragment f = (WorkoutViewFragment) super.instantiateItem(container, position);
            if (workoutViewFragments.size() <= position) {
                workoutViewFragments.add(position, f);
            } else {
                workoutViewFragments.set(position, f);
            }
            return f;
        }

        public ArrayList<WorkoutViewFragment> getList() {
            return list;
        }

    }




    /*/
       new class
        this class is a fragment that controls the recyclerview, it's adapter and all the callbacks.
        callbacks include: drag&drop, onclick, on long click.
     */

    public static class WorkoutViewFragment extends Fragment implements com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener,
            ScrollToPositionListener,
            OnExerciseProfileEditClick, OnExerciseChangeListener, OnWorkoutViewInterfaceClicksListener,
            OnLayoutManagerDialogPress {

        private RecyclerView recycler;
        //private WorkoutAdapter adapter;
        private MyExpandableAdapter adapter;
        private ItemTouchHelper touchHelper;
        private ArrayList<PLObject> exArray;
        private WorkoutViewOnWorkoutListener listener;
        private boolean hideIcon;
        private boolean disable;
        private ProgressorObserver progressorObserver;
        private OnUpdateLayoutStatsListener onUpdateLayoutStatsListener;
        private ChooseDialogFragment f;
        private MyExpandableLinearLayoutManager lm;
        private View mainView;
        private ProgramToolsView.WorkoutViewModes workoutViewModes;
        private OnEnterExitChooseFragment onEnterExitChooseFragment;
        private LayoutManagerOperator layoutManagerOperator;
        // private LayoutManagerInterfaceHelper interfaceHelper;


        public static WorkoutViewFragment getInstance(OnEnterExitChooseFragment onEnterExitChooseFragment,
                                                      OnUpdateLayoutStatsListener onUpdateListener,
                                                      ArrayList<PLObject> exArray) {
            WorkoutViewFragment frag = new WorkoutViewFragment();
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
                    this
            );
            adapter.setOnExerciseProfileEditClick(this);
            if (adapter != null) {
                configurateRecycler();
            } else {
                Log.d("aviv", "initViews:  trouble in workoutviewfragment");
            }
            // longClickMenu.setOnWorkoutViewInterfaceClicksListener(this);
            layoutManagerOperator = new LayoutManagerOperator(getContext(), adapter);
        }

        private void configurateRecycler() {
            lm = new MyExpandableLinearLayoutManager(getContext());
            recycler.setLayoutManager(lm);
            recycler.setAdapter(adapter);
            adapter.setOnDragListener(this);
            adapter.setScrollListener(this);
            //   adapter.setHelper(layoutManagerHelper);
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
                /*LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.SWAP);
                onUpdateLayoutStatsListener.updateLayout(uc);*/
            }

        }

        private void addRow(int position, WorkoutLayoutTypes innerType) {


            if (onUpdateLayoutStatsListener != null) {
                LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.NEW_EXERCISE);
                uc.setInsertPosition(position);
                uc.setInnerType(innerType);
                exArray = onUpdateLayoutStatsListener.updateLayout(uc);
            }
            adapter.myNotifyItemInserted(exArray, position);
            scrollToPosition(position, true, false);
        }

        public void addBodyView(int position, LayoutManager.UpdateComponents upd) {
            if (onUpdateLayoutStatsListener != null) {
                upd.setInsertPosition(position);
                exArray = onUpdateLayoutStatsListener.updateLayout(upd);
            }
            adapter.myNotifyItemInserted(exArray, position);
            scrollToPosition(position, true, false);
        }

        public void setOnEnterExitChooseFragment(OnEnterExitChooseFragment onEnterExitChooseFragment) {
            this.onEnterExitChooseFragment = onEnterExitChooseFragment;
        }

        public void setExArray(ArrayList<PLObject> exArray) {
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
        public void onEditExerciseClick(boolean enter, RecyclerView.ViewHolder vh, int position, PLObject plObject) {
            if (!enter) {
                // onEnterExitChooseFragment.onExitChooseFragment(vh, true);
                //getFragmentManager().popBackStack();
            } else {
                int oldPosition = lm.findFirstVisibleItemPosition();
                f = ChooseDialogFragment.getInstance(this, oldPosition, position, plObject);
                f.setTargetFragment(this, 0);
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.activity_home_framelayout, f, "unique")
                        .addToBackStack("unique")
                        .commit();
                onEnterExitChooseFragment.onEnterChooseFragment(vh.getAdapterPosition());
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

        @Override
        public void duplicateExercise(RecyclerView.ViewHolder vh) {
            int position = vh.getAdapterPosition();
            ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
            ExerciseProfile newCopy = new ExerciseProfile(
                    ep.getMuscle(),
                    0,
                    0,
                    0
            );

            //copies the sets
            for (int i = 0; i < ep.getSets().size(); i++) {
                newCopy.getSets().add(new PLObject.SetsPLObject(newCopy, new ExerciseSet(ep.getSets().get(i).getExerciseSet())));
            }

            // copies the intrasets
            for (int i = 0; i < ep.getSets().size(); i++) {
                for (int j = 0; j < ep.getSets().get(i).getIntraSets().size(); j++) {
                    PLObject.SetsPLObject copyParentSet = newCopy.getSets().get(i);
                    copyParentSet.getIntraSets().add(new PLObject.IntraSetPLObject(
                            newCopy,
                            new ExerciseSet(ep.getSets().get(i).getIntraSets().get(j).getExerciseSet()),
                            WorkoutLayoutTypes.IntraSetNormal,
                            copyParentSet
                    ));
                }

            }

            LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(NEW_EXERCISE);
            int toPosition = position;
            if (ep.isExpand()) {
                toPosition += LayoutManagerHelper.calcBlockLength(ep);
            } else {
                toPosition += (ep.getExerciseProfiles().size());
            }
            updateComponents.setToPosition(toPosition);
            updateComponents.setPlObject(newCopy);
            newCopy.setExercise(ep.getExercise());
            newCopy.setInnerType(ep.getInnerType());
            exArray = onUpdateLayoutStatsListener.updateLayout(updateComponents);
            adapter.setExArray(exArray);
            adapter.notifyItemInserted(toPosition);
        }

        @Override
        public void duplicateItem(RecyclerView.ViewHolder vh, WorkoutLayoutTypes type) {
            switch (type) {
                case SetsPLObject:
                    onSetsDoubleClick(vh);
                    break;
                case ExerciseProfile:
                    duplicateExercise(vh);
                    break;
                case IntraSetNormal:
                    onAddNormalIntraSet(vh);
                    break;
            }

        }

        @Override
        public void notifyExerciseChanged(int position) {
            adapter.notifyItemChanged(position);
        }

        @Override
        public void onExerciseClick(MyExpandableAdapter.ExerciseViewHolder vh) {
            ExerciseProfile exerciseProfile = (ExerciseProfile) exArray.get(vh.getAdapterPosition());
            if (exerciseProfile.isMore()) {
                vh.card.animate().x(0);
                exerciseProfile.setMore(false);
            }
            if (exerciseProfile.isExpand()) {
                collapseExercise(vh);
            } else {
                expandExercise(vh);
            }
        }

        @Override
        public void onLongSupersetClick(final int position, boolean delete) {
            ExerciseProfile ep = null;
            ep = ((ExerciseProfile) exArray.get(position));
            if (delete) {
                int length = ep.getParent().getExerciseProfiles().size() + ep.getParent().getSets().size();
                boolean isExpand = ep.getParent().isExpand();

                ep.getParent().getExerciseProfiles().remove(ep);
                LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(DELETE_EXERCISE);
                updateComponents.setPlObject(ep);
                updateComponents.setRemovePosition(position);
                exArray = onUpdateLayoutStatsListener.updateLayout(updateComponents);

                adapter.setExArray(exArray);
                adapter.notifyItemRemoved(position);
                if (isExpand) {

                    for (int i = position; i < exArray.size(); i++) {
                        for (int j = 0; j < ep.getParent().getSets().size(); j++) {
                            if (exArray.get(i).type == WorkoutLayoutTypes.IntraSet && ep.getIntraSets().get(j) == exArray.get(i)) {
                                exArray.remove(i);
                                adapter.notifyItemRemoved(i);
                            }
                        }
                    }
                }
                int start = 0;
                int end = 0;
                int fatherPos = LayoutManagerHelper.findPLObjectPosition(ep.getParent(), exArray);
                adapter.notifyItemRangeChanged(fatherPos, LayoutManagerHelper.calcBlockLength(ep.getParent()));
                //adapter.notifyItemRangeRemoved(position+start, ep.getParent().getSets().size()*2);

                return;
            }
        }

        @Override
        public void onLongClick(final RecyclerView.ViewHolder vh, boolean delete) {
            String item = null;
            View v = null;

            longClickMenu.onShowMenu((MyExpandableAdapter.MyExpandableViewHolder) vh,
                    LayoutManagerHelper.findPLObjectDefaultType(exArray.get(vh.getAdapterPosition())), delete);

        }

        @Override
        public void onBodyViewLongClick(final RecyclerView.ViewHolder vh, boolean delete) {
            if (delete) {
                exArray.remove(vh.getAdapterPosition());
                adapter.notifyItemRemoved(vh.getAdapterPosition());
            }
            new AlertDialog.Builder(getContext())
                    .setCancelable(true)
                    .setMessage("Delete this Exercise and all it's data?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onLongClick(vh, true);

                        }
                    }).setNegativeButton("No", null).show();
        }

        @Override
        public void collapseExercise(int adapterPosition) {
            if (lm.findFirstVisibleItemPosition() <= adapterPosition) {
                MyExpandableAdapter.ExerciseViewHolder vh =
                        (MyExpandableAdapter.ExerciseViewHolder) recycler.findViewHolderForLayoutPosition(adapterPosition);
                collapseExercise(vh);
            }

        }

        @Override
        public void expandExercise(int adapterPosition) {
            MyExpandableAdapter.ExerciseViewHolder vh = (MyExpandableAdapter.ExerciseViewHolder) recycler.findViewHolderForLayoutPosition(adapterPosition);
            expandExercise(vh);
        }

        @Override
        public void onScrollPosition(int position, boolean flag1, boolean flag2) {

        }

        @Override
        public void onLongClickMenuAddSuperset(final RecyclerView.ViewHolder vh) {
            if (lm.findFirstCompletelyVisibleItemPosition() < vh.getAdapterPosition()) {
                scrollToPosition(vh.getAdapterPosition(), true, true);
            }
            ExerciseProfile ep = (ExerciseProfile) exArray.get(vh.getAdapterPosition());
            ExerciseProfile superset = new ExerciseProfile(null, 0, 0, 0);
            superset.setInnerType(WorkoutLayoutTypes.IntraExerciseProfile);
            superset.setParent(ep);
            if (ep.isExpand()) {
                collapseExercise((MyExpandableAdapter.ExerciseViewHolder) vh);
                //   onCollapse(vh);
            }
            for (int i = 0; i < ep.getSets().size(); i++) {
                superset.getIntraSets().add(new PLObject.IntraSetPLObject(superset, new ExerciseSet(),
                        WorkoutLayoutTypes.SuperSetIntraSet, null));
            }
            ep.getExerciseProfiles().add(superset);
            LayoutManager.UpdateComponents up = new LayoutManager.UpdateComponents(NEW_SUPERSET);
            up.setToPosition(vh.getAdapterPosition() + ep.getExerciseProfiles().size());
            up.setPlObject(superset);
            exArray = onUpdateLayoutStatsListener.updateLayout(up);
            // exArray.add(vh.getAdapterPosition() + ep.getExerciseProfiles().size(), superset);
            adapter.setExArray(exArray);
            adapter.notifyItemInserted(vh.getAdapterPosition() + ep.getExerciseProfiles().size());
        }

        @Override
        public void onLongClickHideMenu() {
            longClickMenu.onHideMenu();
        }

        @Override
        public void onAddNormalIntraSet(RecyclerView.ViewHolder vh) {
            PLObject.SetsPLObject setsPLObject;
            PLObject.IntraSetPLObject intra;
            int addPosition = -1;
            if (exArray.get(vh.getAdapterPosition()) instanceof PLObject.IntraSetPLObject) {
                intra = (PLObject.IntraSetPLObject) exArray.get(vh.getAdapterPosition());
                setsPLObject = intra.getParentSet();
                addPosition = vh.getAdapterPosition() + 1;
            } else {
                setsPLObject = (PLObject.SetsPLObject) exArray.get(vh.getAdapterPosition());
                addPosition = vh.getAdapterPosition() + setsPLObject.getIntraSets().size() + 1;

            }
            intra = layoutManagerOperator.injectNewIntraSet(setsPLObject);
            if (addPosition != -1) {
                exArray.add(addPosition, intra);
                adapter.notifyItemInserted(addPosition);
            }
            int parentposition = LayoutManagerHelper.findPLObjectPosition(setsPLObject.getParent(), exArray);
            adapter.notifyItemChanged(parentposition);
            layoutManager.saveLayoutToDataBase(true);
        }

        @Override
        public void deleteItem(int position, boolean delete) {
            if (delete) {
                PLObject plObject = exArray.get(position);
                WorkoutLayoutTypes type = LayoutManagerHelper.findPLObjectDefaultType(plObject);
                switch (type) {
                    case IntraExerciseProfile:
                        onLongSupersetClick(position, delete);
                        //layoutManagerOperator.deleteSuperset((ExerciseProfile) plObject, onUpdateLayoutStatsListener,this);
                        break;
                    case ExerciseProfile:
                        deleteExercise(position);
                        // layoutManagerOperator.deleteExercise((ExerciseProfile) plObject, onUpdateLayoutStatsListener, this);
                        break;
                    case SetsPLObject:
                        layoutManagerOperator.deleteSet((PLObject.SetsPLObject) plObject, exArray);
                        break;
                    case IntraSetNormal:
                        adapter.notifyItemRemoved(
                                layoutManagerOperator.deleteIntraSet((PLObject.IntraSetPLObject) plObject, exArray)
                        );
                        int parentposition = LayoutManagerHelper.findPLObjectPosition(((PLObject.IntraSetPLObject) plObject).getParent(), exArray);
                        adapter.notifyItemChanged(parentposition);
                        break;
                    case BodyView:
                        exArray.remove(position);
                        adapter.notifyItemRemoved(position);
                }

                longClickMenu.onHideMenu();
                return;
            }
            LayoutManagerAlertdialog.getAlertDialog(getContext(), this, position);

        }

        @Override
        public void deleteExercise(int position) {
            ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
            int length = 1;
            if (ep.isExpand()) {
                collapseExercise(position);
                //adapter.onCollapse((MyExpandableAdapter.ExerciseViewHolder) vh);
            }
            LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(DELETE_EXERCISE);
            updateComponents.setPlObject(ep);
            updateComponents.setRemovePosition(position);
            exArray = onUpdateLayoutStatsListener.updateLayout(updateComponents);
            adapter.setExArray(exArray);
            adapter.notifyItemRangeRemoved(position, ep.getExerciseProfiles().size() + 1);
            return;
        }

        @Override
        public void collapseExercise(MyExpandableAdapter.ExerciseViewHolder vh) {
            int adapterPosition = vh.getAdapterPosition();


            ExerciseProfile ep = (ExerciseProfile) exArray.get(adapterPosition);
            int block = LayoutManagerHelper.calcBlockLength(ep);
            if (longClickMenu.getViewPosition() > adapterPosition && longClickMenu.getViewPosition() < block + 1) {
                longClickMenu.onHideMenu();
            }
            ArrayList<PLObject.SetsPLObject> sets = ep.getSets();
            int newPosition = ep.getExerciseProfiles().size() + 1;
            int count = 0;
            if (sets != null) {
                for (int i = 0; i < sets.size(); i++) {
                    exArray.remove(adapterPosition + newPosition);
                    count++;

                    for (int k = 0; k < sets.get(i).getIntraSets().size(); k++) {
                        exArray.remove(adapterPosition + newPosition);
                        count++;

                    }


                    if (ep.getExerciseProfiles().size() != 0) {
                        for (int j = 0; j < ep.getExerciseProfiles().size(); j++) {
                            exArray.remove(adapterPosition + newPosition);
                            count++;
                        }
                    }


                }
                adapter.notifyItemRangeRemoved(adapterPosition + newPosition, count);
                scrollToPosition(adapterPosition, true, true);

            }
            ep.setExpand(false);
            adapter.animateCollapse(vh);
            vh.expand = false;

        }

        @Override
        public void expandExercise(MyExpandableAdapter.ExerciseViewHolder vh) {
            int adapterPosition = vh.getAdapterPosition();
            ExerciseProfile ep = (ExerciseProfile) exArray.get(adapterPosition);
            ArrayList<PLObject.SetsPLObject> sets = ep.getSets();
            ArrayList<PLObject> block = new ArrayList<>();

            int newPosition = ep.getExerciseProfiles().size() + 1;
            int row = 0;
            if (sets != null) {
                for (int i = 0; i < sets.size(); i++) {
                    block.add(sets.get(i));

                    for (int j = 0; j < sets.get(i).getIntraSets().size(); j++) {
                        ++row;

                        if (sets.get(i).getIntraSets().size() != 0) {
                            block.add(sets.get(i).getIntraSets().get(j));
                            ++row;

                        }
                        //exArray.add(position + i + newPosition, obj);
                    }

                    if (ep.getExerciseProfiles().size() != 0) {
                        for (int j = 0; j < ep.getExerciseProfiles().size(); j++) {
                            LayoutManagerHelper.attachSupersetIntraSetsByParent(ep.getExerciseProfiles().get(j));
                            block.add(ep.getExerciseProfiles().get(j).getIntraSets().get(i));

                        }
                    }
                    ++row;

                }
                int i = 0;
                for (PLObject plObject : block) {
                    exArray.add(adapterPosition + newPosition + i, plObject);
                    adapter.notifyItemInserted(adapterPosition + newPosition + i);
                    i++;
                }
                //  adapter.notifyItemRangeInserted(adapterPosition + newPosition, row);
                scrollToPosition(adapterPosition, true, true);
            }
            ep.setExpand(true);
            adapter.animateExpand(vh);
            vh.expand = true;
        }

        @Override
        public void onSetsDoubleClick(final RecyclerView.ViewHolder vh) {
            int position = vh.getAdapterPosition();
            PLObject plObject = exArray.get(position);
            PLObject.SetsPLObject setsPLObject;
            if (plObject instanceof PLObject.SetsPLObject || plObject.getInnerType() == WorkoutLayoutTypes.ExerciseProfile) {

                boolean returnToMenu = false;
                if (plObject.getInnerType() == WorkoutLayoutTypes.ExerciseProfile) {
                    ExerciseProfile ep = (ExerciseProfile) plObject;
                    if (!ep.isExpand()) {
                        expandExercise(position);
                    }
                    position += (LayoutManagerHelper.calcBlockLength(ep)-1);
                    returnToMenu = true;
                    setsPLObject = ep.getSets().get(ep.getSets().size()-1);
                    if(setsPLObject.getIntraSets().size() > 0){
                        position--;
                    }
                }else{
                    setsPLObject = (PLObject.SetsPLObject) exArray.get(position);
                    position++;
                }
                int setBlockLength = setsPLObject.getIntraSets().size() + setsPLObject.getParent().getExerciseProfiles().size();
                int newPosition = position + setBlockLength;
                ArrayList<PLObject> block = layoutManagerOperator.injectCopySet(setsPLObject);
                for (int i = 0; i < block.size(); i++) {
                    exArray.add(newPosition + i, block.get(i));
                    adapter.notifyItemInserted(newPosition + i);
                }
                int numOfBlock = 0;
                for (int i = position; i < exArray.size(); i++) {
                    if (exArray.get(i).getType() != WorkoutLayoutTypes.ExerciseProfile) {
                        numOfBlock++;
                    } else {
                        break;
                    }
                }
                adapter.notifyItemRangeChanged(position + 1, numOfBlock);
                if (!returnToMenu) {
                    notifyExerciseChanged(LayoutManagerHelper.findPLObjectPosition(setsPLObject.getParent(), exArray));
                }
                layoutManager.saveLayoutToDataBase(true);
            }

        }

        @Override
        public void onExerciseDoubleClick(ExerciseProfile ep, int position) {

        }

        @Override
        public void onSetsLongClick(final RecyclerView.ViewHolder vh, final int childPosition, boolean delete) {
            int position = vh.getAdapterPosition();
            int count = 0;
            PLObject.SetsPLObject setsPLObject = (PLObject.SetsPLObject) exArray.get(position);
            if (setsPLObject.getParent().getSets().size() == 1) {
                Toast.makeText(getContext(), "You cannot delete the only set.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (delete) {
                setsPLObject.getParent().getSets().remove(childPosition);
                for (int i = 0; i < setsPLObject.getParent().getExerciseProfiles().size(); i++) {
                    count++;
                    setsPLObject.getParent().getExerciseProfiles().get(i).getIntraSets().remove(childPosition);
                    exArray.remove(position);
                }
                for (int i = 0; i < setsPLObject.getIntraSets().size() + 1; i++) {
                    exArray.remove(position);
                    count++;
                }
                int end = 0;
                for (int i = position; i < exArray.size(); i++) {
                    if (exArray.get(i) instanceof ExerciseProfile) {
                        break;
                    } else {
                        end++;
                    }
                }
                adapter.notifyItemRangeRemoved(position, count);
                adapter.notifyItemRangeChanged(position, end);
                return;
            }

        }

        @Override
        public void onMoreClick(RecyclerView.ViewHolder vh) {
            View v = null;
            View expandV = null;
            if (vh instanceof MyExpandableAdapter.ExerciseViewHolder) {
                v = ((MyExpandableAdapter.ExerciseViewHolder) vh).card;
                expandV = ((MyExpandableAdapter.ExerciseViewHolder) vh).expandableLayout;
            } else if (vh instanceof MyExpandableAdapter.SetsViewHolder) {
                v = ((MyExpandableAdapter.SetsViewHolder) vh).card;
                // expandV = ((MyExpandableAdapter.SetsViewHolder) vh).expandableLayout;


            }
            PLObject plObject = exArray.get(vh.getAdapterPosition());
            if (plObject.isMore()) {
                v.animate().x(0);

                plObject.setMore(false);
            } else {
                plObject.setMore(true);
                v.animate().x(-expandV.getWidth());
            }
        }

        @Override
        public void onSettingsClick(RecyclerView.ViewHolder vh) {
            View v = null;
            if (vh instanceof MyExpandableAdapter.ExerciseViewHolder) {
                v = ((MyExpandableAdapter.ExerciseViewHolder) vh).card;
                ExerciseProfile ep = (ExerciseProfile) exArray.get(vh.getAdapterPosition());
                ep.rawPosition = LayoutManagerHelper.findExercisePosition(ep, exArray);

            } else if (vh instanceof MyExpandableAdapter.SetsViewHolder) {
                v = ((MyExpandableAdapter.SetsViewHolder) vh).card;
            }

            PLObject plObject = exArray.get(vh.getAdapterPosition());
            if (plObject.isMore()) {
                v.animate().x(0);
                plObject.setMore(false);

            }
            if (plObject.isEditMode()) {
                adapter.detailCollapse(vh.getAdapterPosition());
                plObject.setEditMode(false);
                // exerciseProfile.setEditMode(false);

            } else {
                plObject.setEditMode(true);
                //  exerciseProfile.setEditMode(true);
                adapter.deTailExpand(vh);
            }
        }

        @Override
        public void onSwapExercise(int fromPosition, int toPosition) {

        }

        @Override
        public void onLMDialogOkPressed(int viewHolderPosition) {
            deleteItem(viewHolderPosition, true);
        }

        @Override
        public void onLMDialogOkPressed(String input) {

        }
    }
}
