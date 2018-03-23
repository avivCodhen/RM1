package com.strongest.savingdata.MyViews.WorkoutView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
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
import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AlgorithmLayout.PLObject.ExerciseProfile;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.DragNDrop.DragAndDrop;
import com.strongest.savingdata.MyViews.WorkoutViewOnWorkoutListener;
import com.strongest.savingdata.R;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.ChooseDialogFragment;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.DELETE_EXERCISE;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.DELETE_WORKOUT;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.DRAW_DIVIDER;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.NEW_EXERCISE;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.NEW_WORKOUT;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.SWAP;

/**
 * Created by Cohen on 12/16/2017.
 */

public class WorkoutView extends LinearLayout implements WorkoutViewOnWorkoutListener,
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
       // expandableLayout = (ExpandableLayout) findViewById(R.id.workoutview_expandable_layout);
        //lock = (ImageView) findViewById(R.id.workout_view_lock);
        programToolsView = (ProgramToolsView) findViewById(R.id.workout_view_program_tools);
        programToolsView.setOnProgramToolsActionListener(this);

        instantiateViewPager();
        //progressButton = (Button) findViewById(R.id.progress_button);
      //  DragAndDrop dragAndDrop = new DragAndDrop(context, currentRecyclerView, this);
       // RelativeLayout.LayoutParams laParams = new RelativeLayout.LayoutParams(150, 150);
       // progressButton.setLayoutParams(laParams);
    }


    public void enterEditMode() {
        programToolsView.expand();

    }

    private void instantiateViewPager() {
        //configurateRecycler();
        RecyclerView.LayoutManager layM = new LinearLayoutManager(context);
        inflate(context, R.layout.workout_view, this);
        mViewPager = (ViewPager) findViewById(R.id.workoutview_viewpager);

        mTabLayout = (TabLayout) findViewById(R.id.workoutview_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);


       // dropMenuButton = (ViewGroup) findViewById(R.id.workout_view_workoutmenubutton);
       /* DroppyMenuPopup.Builder builder = new DroppyMenuPopup.Builder(getContext(), dropMenuButton);
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
//        droppyMenuPopup = builder.build();
*/

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
    public void updateWorkoutPosition(int workoutPosition) {
        this.workoutPosition = workoutPosition;
    }

    @Override
    public ArrayList<PLObject> updateLayout(LayoutManager.UpdateComponents updateComponents) {
        updateComponents.setWorkoutPosition(mViewPager.getCurrentItem());
        layoutManager.updateLayout(updateComponents);

        mAdapter.setmProgramLayout(layoutManager.getLayout());
        if (updateComponents.isUpdate()) {
            mAdapter.setUpdate(true);
            mAdapter.notifyDataSetChanged();
            mAdapter.setUpdate(false);

        }
        return layoutManager.getSplitRecyclerWorkouts().get(mViewPager.getCurrentItem());

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
                int position = f.exArray.size();

                LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.NEW_EXERCISE);
                uc.setLayout(f.exArray);
                uc.setInsertPosition(position);
                uc.setInnerType(WorkoutLayoutTypes.ExerciseProfile);
                f.setExArray(updateLayout(uc));
                f.adapter.myNotifyItemInserted(f.exArray, position);
                f.scrollToPosition(position, true, false);
                //  f.addRow(f.exArray.size(), WorkoutLayoutTypes.ExerciseProfile);
                break;
            case NEW_WORKOUT:
                upd = new LayoutManager.UpdateComponents(NEW_WORKOUT);
                upd.setUpdate(true);
                updateLayout(upd);
                mAdapter.setmProgramLayout(layoutManager.getLayout());
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
    public void onEnterChooseFragment(final RecyclerView.ViewHolder vh) {
        WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
//        ExerciseProfile ep = (ExerciseProfile) f.exArray.get(vh.getAdapterPosition());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                vh.itemView.setAlpha(0f);
            }
        }, 200);
        //MyJavaAnimator.fadeOut(vh.itemView);
//        f.getAdapter().deTailExpand(vh);
    }

    @Override
    public void onExitChooseFragment(final RecyclerView.ViewHolder vh, boolean isCollapsed) {
        WorkoutViewFragment f = workoutViewFragments.get(mViewPager.getCurrentItem());
        if (f.exArray.get(vh.getAdapterPosition() + 1) instanceof PLObject.AddExercise) {
            f.getAdapter().detailCollapse(vh);
            f.scrollToPosition(vh.getAdapterPosition(), true, false);
        }
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        vh.itemView.startAnimation(anim);
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
        private ArrayList<PLObject> mProgramLayout;
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
            return ProgramTemplate.ProgramTemplateFactory.WhatsYourWorkoutName(position);
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
            workoutViewFragments.add(position, f);
            return f;
        }

        public ArrayList<WorkoutViewFragment> getList() {
            return list;
        }

     /*   public void notiftyMyItemRemoved(int fragment, int position) {
            WorkoutViewFragment f = list.get(fragment);
            //f.updateView(programLayoutManager.initRecyclerMatrixLayout().get(workoutPosition));
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

    public static class WorkoutViewFragment extends Fragment implements com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener,
            ScrollToPositionListener,
            OnExerciseProfileEditClick, OnExerciseChangeListener, OnWorkoutViewInterfaceClicksListener {

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
        private OnWorkoutViewExplode onWorkoutViewExplode;
        private ChooseDialogFragment f;
        private MyExpandableLinearLayoutManager lm;
        private View mainView;
        private ProgramToolsView.WorkoutViewModes workoutViewModes;
        private OnEnterExitChooseFragment onEnterExitChooseFragment;


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
                onEnterExitChooseFragment.onExitChooseFragment(vh, true);
                getFragmentManager().popBackStack();
            } else {

                f = ChooseDialogFragment.getInstance(this, position, plObject, vh);
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

        @Override
        public void onExerciseClick(MyExpandableAdapter.ExerciseViewHolder vh) {
            ExerciseProfile exerciseProfile = (ExerciseProfile) exArray.get(vh.getAdapterPosition());
            if (exerciseProfile.isMore()) {
                vh.card.animate().x(0);
                exerciseProfile.setMore(false);
            }
            if (exerciseProfile.isExpand()) {
                adapter.onCollapse(vh);
            } else {
                adapter.onExpand(vh);
            }
        }

        @Override
        public void onLongSupersetClick(final RecyclerView.ViewHolder vh, boolean delete) {
            ExerciseProfile ep = null;
            final int position = vh.getAdapterPosition();
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
                for (int i = position; i < exArray.size(); i++) {
                    if (exArray.get(i).getType() == WorkoutLayoutTypes.SetsPLObject) {
                        break;
                    } else {
                        start++;
                    }
                }

                //adapter.notifyItemRangeRemoved(position+start, ep.getParent().getSets().size()*2);

                return;
            }

            new AlertDialog.Builder(getContext())
                    .setCancelable(true)
                    .setMessage("Delete this Exercise and all it's data?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onLongSupersetClick(vh, true);

                        }
                    }).setNegativeButton("No", null).show();
        }

        @Override
        public void onLongClick(final RecyclerView.ViewHolder vh, boolean delete) {

            boolean isExpand = false;
            ExerciseProfile ep = null;
            final int position = vh.getAdapterPosition();
            ep = ((ExerciseProfile) exArray.get(position));
            isExpand = ep.isExpand();


            if (delete) {
                int length = 1;
                if (isExpand) {
                    adapter.onCollapse((MyExpandableAdapter.ExerciseViewHolder) vh);
                }
                LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(DELETE_EXERCISE);
                updateComponents.setPlObject(ep);
                updateComponents.setRemovePosition(position);
                exArray = onUpdateLayoutStatsListener.updateLayout(updateComponents);
                adapter.setExArray(exArray);
                adapter.notifyItemRangeRemoved(position, ep.getExerciseProfiles().size() + 1);
                return;
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
        public void onBodyViewLongClick(final RecyclerView.ViewHolder vh, boolean delete) {
            if(delete){
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
        public void onSetsDoubleClick(RecyclerView.ViewHolder vh, int childPosition) {
            int position = vh.getAdapterPosition();
            PLObject.SetsPLObject setsPLObject = (PLObject.SetsPLObject) exArray.get(position);
            PLObject.SetsPLObject newSetsPLObject = new PLObject.SetsPLObject(
                    setsPLObject.getParent(),
                    setsPLObject.getExerciseSet());
            newSetsPLObject.setInnerType(WorkoutLayoutTypes.SetsPLObject);
            setsPLObject.getParent().getSets().add(childPosition + 1, newSetsPLObject);
            int count = 1;
            int setBlockLength = setsPLObject.getIntraSets().size() + setsPLObject.getParent().getExerciseProfiles().size();
            int newPosition = position + 1 + setBlockLength;
            exArray.add(newPosition, newSetsPLObject);
            for (int i = 0; i < setsPLObject.getParent().getExerciseProfiles().size(); i++) {
                exArray.add(newPosition + 1 + i, new PLObject.IntraSetPLObject(
                        setsPLObject.getParent().getExerciseProfiles().get(i),
                        setsPLObject.getExerciseSet(),
                        WorkoutLayoutTypes.SuperSetIntraSet,
                        setsPLObject
                ));
                count++;
                setsPLObject.getParent().getExerciseProfiles().get(i).getIntraSets().add(childPosition + 1, (PLObject.IntraSetPLObject) exArray.get(newPosition + 1 + i));
            }

            for (int i = 0; i < setsPLObject.getIntraSets().size(); i++) {
                exArray.add(newPosition + count, new PLObject.IntraSetPLObject(
                        setsPLObject.getParent(),
                        setsPLObject.getExerciseSet(),
                        WorkoutLayoutTypes.IntraSetNormal,
                        setsPLObject
                ));
                setsPLObject.getIntraSets().add((PLObject.IntraSetPLObject) exArray.get(newPosition + count));
                count++;

            }
            adapter.notifyItemRangeInserted(position + 1, count);
            int numOfSupersets = setsPLObject.getParent().getExerciseProfiles().size();
            int numOfMainExerciseSet = setsPLObject.getParent().getSets().size();
            int numOfBlock = 0;
            for (int i = position; i < exArray.size(); i++) {
                if (exArray.get(i).getType() != WorkoutLayoutTypes.ExerciseProfile) {
                    numOfBlock++;
                } else {
                    break;
                }
            }
            int blockLength = (numOfSupersets * numOfMainExerciseSet) * 2 - childPosition;
            adapter.notifyItemRangeChanged(position + 1, numOfBlock);
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
            new AlertDialog.Builder(getContext())
                    .setCancelable(true)
                    .setMessage("Delete this set?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onSetsLongClick(vh, childPosition, true);

                        }
                    }).setNegativeButton("No", null).show();
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
            } else if (vh instanceof MyExpandableAdapter.SetsViewHolder) {
                v = ((MyExpandableAdapter.SetsViewHolder) vh).card;
            }
            PLObject plObject = exArray.get(vh.getAdapterPosition());
            if (plObject.isMore()) {
                v.animate().x(0);
                plObject.setMore(false);

            }
            if (plObject.isEditMode()) {
                adapter.detailCollapse(vh);
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
    }
}
