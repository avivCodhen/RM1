package com.strongest.savingdata.MyViews.WorkoutView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuCustomItem;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyScaleAnimation;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.DragAndSwipeCallback;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ScrollToPositionListener;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmProgress.ProgressorManager;
import com.strongest.savingdata.AlgorithmProgress.ProgressorObserver;
import com.strongest.savingdata.AlgorithmProgress.ProgressorSubject;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.PLObjects.ExerciseProfile;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Database.Exercise.BeansHolder;
import com.strongest.savingdata.MyViews.WorkoutViewOnWorkoutListener;
import com.strongest.savingdata.R;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.ChooseDialogFragment;
import com.strongest.savingdata.createProgramFragments.Create.OnPositionViewListener;
import com.strongest.savingdata.createProgramFragments.Unused.AddOrDeleteFragment;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cohen on 12/16/2017.
 */

public class WorkoutView extends LinearLayout implements View.OnClickListener, WorkoutViewOnWorkoutListener,
        OnUpdateLayoutStatsListener, OnWorkoutViewExplode {

    private RecyclerView mRecyclerView;
    //private ItemTouchHelper mTouchHelper;
    private Context context;
    private Fragment fragment;
    private int workout;
    private FragmentManager fm;
    private LayoutManager layoutManager;
    //private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutParams layoutParams;
    private Button progressButton;
    private WorkoutViewPagerAdapter mAdapter;
    private ProgressorManager progressorManager;
    private DroppyMenuPopup droppyMenuPopup;
    private ViewGroup dropMenuButton;


    /*
        private OnClickListener onClickListener = this;
        private OnPositionViewListener onPositionListener = this;*/
    private int workoutPosition;


    private static boolean showBody;
    private boolean showStats = true;
    private boolean hideIcon;

    public WorkoutView(Context context) {
        super(context);
        this.context = context;
    }

    public WorkoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    private void createView() {
        //  programLayoutManager = ((BaseActivity)context).getProgrammer().getProgramLayoutManager();


      /*  if (showBody) {
            mProgramLayout = programLayoutManager.getCleanWorkout();

        } else {
            mProgramLayout = programLayoutManager.getSplitRecyclerWorkouts();
        }*/
        setOrientation(VERTICAL);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        setLayoutParams(layoutParams);
        inflate(context, R.layout.workout_view, this);

        //inflate(context, R.layout.keyboard_layout, this);


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
          /*  mAdapter = new WorkoutAdapter(
                    mProgramLayout.get(workout),
                    context,
                    onClickListener,
                    onPositionListener,
                    showStats

            );*/
            instantiateRecyclerView(workout);
        }
        progressButton = (Button) findViewById(R.id.progress_button);
        progressButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutManager.saveLayoutToDataBase(true);
            }
        });

    }

    private void instantiateRecyclerView(int workout) {
        //    configurateRecycler();

        //  ItemTouchHelper.Callback callback = new DragAndSwipeCallback(mAdapter);
        // mTouchHelper = new ItemTouchHelper(callback);
        //mRecyclerView.setAdapter(mAdapter);

        //mTouchHelper.attachToRecyclerView(mRecyclerView);
        // addView(mRecyclerView);
    }

    private void configurateRecycler() {
        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutParams(layoutParams);
        LinearLayoutManager lM = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(lM);
    }

    private void instantiateViewPager() {
        //configurateRecycler();
        RecyclerView.LayoutManager layM = new LinearLayoutManager(context);
        inflate(context, R.layout.workout_view, this);
        mViewPager = (ViewPager) findViewById(R.id.workoutview_viewpager);

      /*  mTabLayout = (TabLayout) findViewById(R.id.workoutview_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);*/
      if(layoutManager.getProgramTemplate() != null){
          final ArrayList<String> strings = layoutManager.getProgramTemplate().getWorkoutsNames();
          dropMenuButton = (ViewGroup) findViewById(R.id.workout_view_workoutmenubutton);
          DroppyMenuPopup.Builder builder = new DroppyMenuPopup.Builder(getContext(), dropMenuButton);
          for (String argh : strings) {
              builder.addMenuItem(new DroppyMenuItem(argh));


          }
          // DroppyMenuCustomItem it = new DroppyMenuCustomItem()

          builder.setOnClick(new DroppyClickCallbackInterface() {
              @Override
              public void call(View v, int id) {
                  ((TextView) dropMenuButton.getChildAt(0)).setText(strings.get(id));
                  mViewPager.setCurrentItem(id);
              }
          });
          droppyMenuPopup = builder.build();
       /* mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                workoutPosition = tab.getPosition();
                mViewPager.setCurrentItem(workoutPosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
          //  ItemTouchHelper.Callback callback = new DragAndSwipeCallback(mAdapter);
          // mTouchHelper = new ItemTouchHelper(callback);
          //mTouchHelper.attachToRecyclerView(mRecyclerView);

          mAdapter = new WorkoutViewPagerAdapter(this, this, fm,
                  layoutManager, this);
          //  mViewPager.setOffscreenPageLimit(programLayoutManager.getNumOfWorkouts());
          mViewPager.setAdapter(mAdapter);
          //mViewPager.addView(mRecyclerView);
          mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
              @Override
              public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                  ((TextView) dropMenuButton.getChildAt(0)).setText(strings.get(position));
              }

              @Override
              public void onPageSelected(int position) {

              }

              @Override
              public void onPageScrollStateChanged(int state) {

              }
          });
      }

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void explode(View v, RecyclerView recyclerView) {
      /*  final Rect rect = new Rect();
        v.getGlobalVisibleRect(rect);
        rect.top = rect.bottom;
        Transition explode = new Explode()
                .setEpicenterCallback(new Transition.EpicenterCallback() {
                    @Override
                    public Rect onGetEpicenter(Transition transition) {
                        return rect;
                    }
                }).setDuration(500).addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {

                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {

                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {

                    }
                });
        TransitionManager.beginDelayedTransition(recyclerView, explode);

        // remove all views from Recycler View
        recyclerView.setAdapter(null);
*/
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
        updateComponents.setWorkoutPosition(workoutPosition);
        layoutManager.updateLayout(updateComponents);
      //  layoutManager.updateLayoutStats(true);
       /* mAdapter.setmProgramLayout(layoutManager.getLayout());
        mAdapter.notifyDataSetChanged();*/
        return layoutManager.getSplitRecyclerWorkouts().get(workoutPosition);
        //   mAdapter.setmProgramLayout(layoutManager.getLayout());
        //   mAdapter.notifyDataSetChanged();
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


        public WorkoutViewPagerAdapter(OnWorkoutViewExplode onWorkoutViewExplode,
                                       OnUpdateLayoutStatsListener onUpdateListener,
                                       FragmentManager fm, LayoutManager plm,
                                       WorkoutViewOnWorkoutListener listener) {
            super(fm);
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

        @Override
        public int getCount() {
            return plm.getNumOfWorkouts();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return plm.getProgramTemplate().getWorkoutsNames().get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            //   f = new WorkoutViewFragment();
            // adapter.setExArray(programLayoutManager.getSplitRecyclerWorkouts().get(position));
            mProgramLayout = plm.getMusclesPerWorkout().get(position);
            WorkoutViewFragment frag = WorkoutViewFragment.getInstance(onUpdateListener,
                    mProgramLayout, false, false);
            frag.setOnUpdateLayoutStatsListener(onUpdateListener);
            list.add(position, frag);
            frag.setOnWorkoutViewExplode(onWorkoutViewExplode);
            return frag;
        }


        public ArrayList<WorkoutViewFragment> getList() {
            return list;
        }

        public void notiftyMyItemRemoved(int fragment, int position) {
            WorkoutViewFragment f = list.get(fragment);
            //f.updateView(programLayoutManager.getSplitRecyclerWorkouts().get(workoutPosition));
            f.notifyItemRemoved(position);
        }

        public void notifyItemAdded(int fragment, int position) {
            WorkoutViewFragment f = list.get(fragment);
            f.notifyItemAdded(position);
        }
    }

    public void setWorkout(int workout) {
        this.workout = workout;
    }


    /*/
       new class
        this class is a fragment that controls the recyclerview, it's adapter and all the callbacks.
        callbacks include: drag&drop, onclick, on long click.
     */

    public static class WorkoutViewFragment extends Fragment implements OnChooseClickListener, com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener, ScrollToPositionListener
            , OnPositionViewListener, WorkoutObserver, OnAddOrDeleteListener, ProgressorSubject,
            MyGroupCollapseExpand {

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


        public static WorkoutViewFragment getInstance(OnUpdateLayoutStatsListener onUpdateListener, ArrayList<PLObjects> exArray, boolean hideIcon, boolean disable) {
            WorkoutViewFragment frag = new WorkoutViewFragment();
            frag.setExArray(exArray);
            frag.setHideIcon(hideIcon);
            frag.setDisable(disable);
            frag.setOnUpdateLayoutStatsListener(onUpdateListener);
            return frag;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.workoutview_list, container, false);

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
                    this,
                    true,
                    hideIcon,
                    disable
            );


            adapter.hideIcon(hideIcon);

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
            LinearLayoutManager lm = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(lm);
            recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                }
            });
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

        public void notifyItemRemoved(int position) {
            adapter.notifyItemRemoved(position);
        }

        public void notifyItemAdded(int position) {
            adapter.notifyItemInserted(position);
        }

        @Override
        public void scrollToPosition(int position) {
            recycler.scrollToPosition(position);
        }

        @Override
        public void startDrag(RecyclerView.ViewHolder viewHolder) {
            touchHelper.startDrag(viewHolder);
            if (progressorObserver != null) {
                onProgressSwap(adapter.getFromPos(), adapter.getToPos());
            }

            if (onUpdateLayoutStatsListener != null) {
                LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.Update.Swap);
                onUpdateLayoutStatsListener.updateLayoutStats(uc);

            }

        }

        public void setWorkoutViewOnPositionListenener(WorkoutViewOnWorkoutListener listener) {
            this.listener = listener;
        }

        /*    @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.default_transition));
                    this.setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.no_transition));
                    ManagerFragment f = new ManagerFragment();
                    f.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.default_transition));
                    f.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.no_transition));

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
                addRow(position);
            } else {
                removeRow(position);
            }
        }

        private void addRow(int position) {
            ExerciseProfile tempProfile = (ExerciseProfile) exArray.get(position);
            ExerciseProfile exerciseProfile = new ExerciseProfile(null, tempProfile.getMuscle(),
                    tempProfile.getWorkoutId(),
                    tempProfile.getBodyId(),
                    tempProfile.getBodyId() + exArray.size());
            if (exArray.get(position - 1)
                    .getType() == WorkoutLayoutTypes.BodyView) {
                position = position + 1;
            }
            if (progressorObserver != null) {
                //position, exerciseProfile
                onProgressInserted(position, exerciseProfile);
            }
            // exArray.add(position, exerciseProfile);
            if (onUpdateLayoutStatsListener != null) {
                LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.Update.Insert);
                uc.setInsertPosition(position);
                uc.setExerciseProfile(exerciseProfile);
                exArray = onUpdateLayoutStatsListener.updateLayoutStats(uc);

            }
            adapter.myNotifyItemInserted(exArray, position);
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
                LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.Update.Remove);
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

            Bundle b = new Bundle();
            b.putInt(AddOrDeleteFragment.POS, pos);
            AddOrDeleteFragment aodf = new AddOrDeleteFragment();
            aodf.setArguments(b);
            aodf.setAddOrDeleteListener(this);
            //idf.setTargetFragment(fragment, CreateFragment.INTERRACT_FRAGMENT);
            aodf.show(getFragmentManager(), "Choose ExerciseBean");
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
                LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.Update.Change);
                exArray = onUpdateLayoutStatsListener.updateLayoutStats(uc);

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
            //this.setExitTransition(new Fade());
            //    this.setEnterTransition(new AutoTransition());
           /* if (onWorkoutViewExplode != null) {
                final Rect rect = new Rect();
                v.getGlobalVisibleRect(rect);
                rect.top = rect.bottom;
                Transition explode = new Explode();
                explode.setEpicenterCallback(new Transition.EpicenterCallback() {
                    @Override
                    public Rect onGetEpicenter(Transition transition) {
                        return rect;
                    }
                });
                explode.excludeTarget(v, false);
                explode.setDuration(2000);
                //explode.setInterpolator(new AnticipateOvershootInterpolator());
                explode.setPropagation(null);
                explode.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {
                        f = new ChooseDialogFragment();
                        f.setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
                        f.setEnterTransition(new AutoTransition());
                        f.setExitTransition(new AutoTransition());
                        f.setExerciseProfile(exerciseProfile);
                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.workout_nested_layout, f, "unique");
                        //fragmentTransaction.replace(R.id.activity_home_layout, f, "unique");
                        fragmentTransaction.addToBackStack("unique");
                        //fragmentTransaction.addSharedElement(v, tName);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {

                    }
                });
                TransitionManager.beginDelayedTransition(recycler, explode);

                // remove all views from Recycler View
                recycler.setAdapter(null);


            }
            */
               /* ChooseDialogFragment f;
                f = new ChooseDialogFragment();
                f.setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
                f.setEnterTransition(new AutoTransition());
                f.setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.no_transition));
                f.setExerciseProfile(exerciseProfile);*/
            //f.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
            //f.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.no_transition));
            //String tName = ViewCompat.getTransitionName(v);
            //f.setUniqueTransitionName(tName);
               /* FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.workout_nested_layout, f, "unique");
                //fragmentTransaction.replace(R.id.activity_home_layout, f, "unique");
                fragmentTransaction.addToBackStack("unique");
                //fragmentTransaction.addSharedElement(v, tName);
                fragmentTransaction.commit();*/

            f = new ChooseDialogFragment();
            f.setTargetFragment(this, 0);
            Bundle bundle = new Bundle();
            bundle.putInt("exercise_position", Integer.parseInt(v.getTag().toString()));
            f.setArguments(bundle);
            //    f.setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
            f.setEnterTransition(new AutoTransition());
            f.setExitTransition(new AutoTransition());
            f.setExerciseProfile(exerciseProfile);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.workout_nested_layout, f, "unique");
//            fragmentTransaction.addSharedElement(v, transitionName);
            //fragmentTransaction.replace(R.id.activity_home_layout, f, "unique");
            fragmentTransaction.addToBackStack("unique");
            //fragmentTransaction.addSharedElement(v, tName);
            fragmentTransaction.commitAllowingStateLoss();

        }


        public void setOnWorkoutViewExplode(OnWorkoutViewExplode onWorkoutViewExplode) {
            this.onWorkoutViewExplode = onWorkoutViewExplode;
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


        @Override
        public void expand(int position, ExpandableGroup group) {
            scrollToPosition(position);
        }
    }

}
