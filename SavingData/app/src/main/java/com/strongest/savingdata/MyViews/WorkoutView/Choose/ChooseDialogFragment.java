package com.strongest.savingdata.MyViews.WorkoutView.Choose;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.strongest.savingdata.Activities.BaseActivity;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.ReactLayoutManager;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.MyViews.MySelector.MySelectorOnBeansHolderChange;
import com.strongest.savingdata.MyViews.WorkoutView.OnExerciseChangeListener;
import com.strongest.savingdata.MyViews.WorkoutView.WorkoutView;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

import java.util.ArrayList;

public class ChooseDialogFragment extends BaseCreateProgramFragment implements View.OnClickListener,
        TabLayout.OnTabSelectedListener, MySelectorOnBeansHolderChange {

    public static final String LISTENER = "listener";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Bundle i;
    private ArrayList<PLObjects> mLayout;
    private DataManager dataManager;
    public AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;

    private PLObjects.ExerciseProfile exerciseProfile;
    private String tName;
    private Button backBtn;
    private ReactLayoutManager mReactLayoutManager;

    private OnExerciseChangeListener onExerciseChangeListener;

    private int exercisePosition;

    private RecyclerView.ViewHolder vh;

    private int currentRecyclerPosition;
    private int currentTab;
    private boolean initiated;
    private RecyclerView mRecyclerView;
    private MyExpandableAdapter myExpandableAdapter;

    public static ChooseDialogFragment getInstance(OnExerciseChangeListener onExerciseChangeListener,
                                                   int position,
                                                   PLObjects.ExerciseProfile ep, RecyclerView.ViewHolder vh) {
        ChooseDialogFragment f = new ChooseDialogFragment();
        f.setExerciseProfile(ep);
        f.setExercisePosition(position);
        f.setOnExerciseChangeListener(onExerciseChangeListener);
        f.setVh(vh);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActionBar().show();

            }
        }, 500);
        ((HomeActivity) getActivity()).begoneTabLayout();
        /*Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(100);
*/
        View v = inflater.inflate(R.layout.fragment_choose, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
        nestedScrollView = (NestedScrollView) v.findViewById(R.id.fragment_choose_nestedscrollview);
        mLayout = new ArrayList<>();
        makeLayout(exerciseProfile);
        currentRecyclerPosition = exercisePosition;
        dataManager = new DataManager(getContext());
        // i = getArguments();
        mReactLayoutManager = ReactLayoutManager.newInstance(
                ((BaseActivity) getActivity()).getProgrammer().getLayoutManager(),
                ((WorkoutView.WorkoutViewFragment) getParentFragment()),
                exerciseProfile

        );


        ArrayList<PLObjects> layout = new ArrayList<>();
        layout.add(exerciseProfile);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_choose_recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAlpha(0f);
        MyJavaAnimator.fadeIn(mRecyclerView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        },200);
        myExpandableAdapter = new MyExpandableAdapter(layout, getContext(), null, null, null);
        mRecyclerView.setAdapter(myExpandableAdapter);


        ((MyExpandableAdapter.ExerciseViewHolder) vh).card.setOnClickListener(null);
        final ChooseAdapter adapter = new ChooseAdapter(getChildFragmentManager(), mLayout);
        viewPager = (ViewPager) v.findViewById(R.id.choose_dialog_viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) v.findViewById(R.id.fragment_choose_tablayout);

        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabsDec = (tab.getPosition() - currentTab);
                if (tabsDec == 1) {
                    currentRecyclerPosition++;
                } else if (tabsDec == -1) {
                    currentRecyclerPosition--;
                } else if (tabsDec < -1) {
                    currentRecyclerPosition -= tabsDec;
                } else if (tabsDec > 1) {
                    currentRecyclerPosition += tabsDec;

                }
                ((HomeActivity) getActivity()).workoutView.scrollToPositionCallBack(currentRecyclerPosition);
                currentTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        v.findViewById(R.id.choose_add_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ExerciseSet> set = new ArrayList<>();
                ExerciseSet aSet = new ExerciseSet();
                set.add(new ExerciseSet());
                // exerciseProfile.getSets().add(set);
                PLObjects.SetsPLObject setsPLObject = new PLObjects.SetsPLObject(exerciseProfile, aSet);
                mLayout.add(mLayout.size() - 1, setsPLObject);
                ((HomeActivity) getActivity()).
                        workoutView.addSet(
                        exercisePosition + exerciseProfile.getSets().size(),
                        setsPLObject);

                //makeLayout(exerciseProfile);
                adapter.notifyDataSetChanged();
            }
        });

        v.findViewById(R.id.choose_add_intra_exercise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLObjects.ExerciseProfile ep = new PLObjects.ExerciseProfile(null, 0, 0, 0);
                ep.setInnerType(WorkoutLayoutTypes.IntraExerciseProfile);
                exerciseProfile.getExerciseProfiles().add(ep);
                ep.setTag(LayoutManager.intraWorkoutsLetters[exerciseProfile.getExerciseProfiles().size()]);
                ArrayList<ExerciseSet> set = new ArrayList<>();
                ExerciseSet aSet = new ExerciseSet();
                set.add(new ExerciseSet());
                //    ep.setIntraSets(set);
                mLayout.add(exerciseProfile.getExerciseProfiles().size(), ep);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(exerciseProfile.getExerciseProfiles().size());

            }
        });
        v.findViewById(R.id.fragment_choose_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).workoutView.onExitChooseFragment(vh, false);
                ((HomeActivity) getActivity()).reviveTabLayout();

                exerciseProfile.setEditMode(false);
                getFragmentManager().popBackStack();
            }
        });
        // Attach(workoutView);
        // intent = new Intent();
    }

    private void makeLayout(PLObjects.ExerciseProfile ep) {
        mLayout.clear();
        mLayout.add(ep);
        for (int j = 0; j < ep.getExerciseProfiles().size(); j++) {
            mLayout.add(ep.getExerciseProfiles().get(j));
        }
        for (int j = 0; j < ep.getSets().size(); j++) {
            //  PLObjects.SetsPLObject setsPLObject = new PLObjects.SetsPLObject(exerciseProfile,ep.getSets().get(j).get(0));
            //       mLayout.add(setsPLObject);
        }
    }

    public Resources resources() {
        return getContext().getResources();
    }


    @Override
    public void onClick(View v) {
        ArrayList<ExerciseSet> mBH = new ArrayList<>();


    }

    public NestedScrollView getNestedScrollView() {
        return nestedScrollView;
    }

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
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

    public void setExerciseProfile(PLObjects.ExerciseProfile exerciseProfile) {
        this.exerciseProfile = exerciseProfile;
    }

    public void setUniqueTransitionName(String tName) {
        this.tName = tName;

    }

    @Override
    public void notifyExerciseProfileBeanChange(String beanType, Beans bean) {
      /*  if (!exerciseProfile.isHasBeansHolders()) {
            exerciseProfile.setmSets(new Sets());
            ArrayList<Sets> arr= new ArrayList<>();
            arr.add(exerciseProfile.getmSets());
            exerciseProfile.setSets(arr);
        }
        switch (beanType) {
            case "exercise":
                exerciseProfile.getmSets().setExercise(bean);
                break;
            case "reps":
                exerciseProfile.getmSets().setRep(bean);
                break;
            case "sets":
                exerciseProfile.getmSets().setSets(bean);
                break;
            case "rest":
                exerciseProfile.getmSets().setRest(bean);
                break;
        }
        onExerciseChangeListener.onExerciseChange(exercisePosition, beanType);
*/
    }

    @Override
    public void notifyMuscleChange(Muscle m) {
        exerciseProfile.setMuscle(m);
        onExerciseChangeListener.onExerciseChange(exercisePosition, "muscle");
    }

    public void setOnExerciseChangeListener(OnExerciseChangeListener onExerciseChangeListener) {
        this.onExerciseChangeListener = onExerciseChangeListener;
    }

    public void setExercisePosition(int exercisePosition) {
        this.exercisePosition = exercisePosition;
    }

    public void setVh(RecyclerView.ViewHolder vh) {
        this.vh = vh;
    }

    public ReactLayoutManager getmReactLayoutManager() {
        return mReactLayoutManager;
    }



    /*  @Subscribe
    public void updateBeansHolder(double weight, Beans method) {
        this.newBeansHolder.setMethod(method);
        this.newBeansHolder.setWeight(weight);

    }

    @Subscribe
    public void updateBeansHolder(Beans ex, Beans reps, Beans rest, int sets) {
        this.newBeansHolder.setExercise(ex);
        this.newBeansHolder.setRep(reps);
        this.newBeansHolder.setRest(rest);
        this.newBeansHolder.setSets(sets);
    }*/
}

   /* private boolean validate() {
        *//*if (doYouHaveWeapon() && checkedItems[0] == -1) {
            if (checkedItems[1] != -1 || checkedItems[2] != -1) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_noLevelChoose),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        }*//*

        if (checkedItems[0] == -1 && checkedItems[1] == -1) {
            return false;
        }

        return true;
    }
*/



  /*  @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int pos = 0;
        int counter = 0;
        while (radioGroups[pos].getWorkoutId() != group.getWorkoutId()) {
            pos++;
        }


        if (unCheck(checkedId, pos)) {
            for (int j = 0; j < group.getChildCount(); j++) {
                ((ToggleButton) group.getChildAt(j)).setChecked(false);
            }
            return;
        }
        for (int i = 0; i < group.getChildCount(); i++) {
            if (group.getChildAt(i).getWorkoutId() == checkedId) {
                ((ToggleButton) group.getChildAt(i)).setChecked(true);
            } else {
                ((ToggleButton) group.getChildAt(i)).setChecked(false);
            }
        }


    }
*/


/* private boolean unCheck(int checkedId, int pos) {
        if (checkedId == prevIds[pos]) {
            checkCounter++;
            if (checkCounter == 1) {
                checkCounter = 0;
                prevIds[pos] = 0;
                checkedItems[pos] = -1;
                showTextViewOrNot(8, pos);
            }
            return true;
        } else {
            showTextViewOrNot(0, pos);
            prevIds[pos] = checkedId;
            checkedItems[pos] = checkedId;
        }
        return false;
    }
*/


   /* exerciseNameTV = (TextView) v.findViewById(R.id.choose_exercise_name);
        exerciseDetailTV = (TextView) v.findViewById(R.id.choose_exercise_tv);
        methodsTV = (TextView) v.findViewById(R.id.choose_method_description_tv);
        setCommanders(v);
        // exProfileCheckedItems = new int[]{newBeansHolder.getExPosition(), newBeansHolder.getRepPosition(), newBeansHolder.getMetPosition()};
        //setExerciseNameArray(newBeansHolder.getTag().ordinal());
        checkedItems = new int[]{-1, -1, -1};
        radioGroups = new RadioGroup[]{rg1, rg2, rg3};
        prevIds = new int[radioGroups.length];
        @DrawableRes int exBg = R.drawable.selector;
        @DrawableRes int restBg = R.drawable.strongest_choose_buttons_selector;
        letTheDogsOut(v, rg1, exerciseBean, exBg, true, false);
        letTheDogsOut(v, rg2, repsBean, restBg, false, true);
        letTheDogsOut(v, rg3, methodBean, restBg, false, false);*/


   /*

    private void setExerciseNameArray(String tag) {
        methodsArray = getContext().getResources().getStringArray(R.array.methods_array);
        repsArray = getContext().getResources().getStringArray(R.array.reps_array);
        bodyArray = i.getStringArray(BODY_ARRAY);

        listofLists = helper.allOfExerciseDetails(bodyArray);
        listofLists2 = helper.allOfExerciseNames(bodyArray);
        getArray(tag);
    }
*/

      /* public void showTextViewOrNot(int visibility, int i) {
        switch (i) {
            case 0:
                exerciseDetailTV.setVisibility(visibility);
                exerciseNameTV.setVisibility(visibility);
                break;
            case 2:
                methodsTV.setVisibility(visibility);
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //   cListener.onClick(buttonView);
    }
*/

    /*private void getArray(String t) {
        int tag = helper.whichBody(bodyArray, t );
        for (int k = 0; k < bodyArray.length; k++) {
            if (tag == k) {
                exerciseDetailsArray = listofLists[k];
                exerciseNamesArray = listofLists2[k];
            }
        }

    }*/

  /*  private void initBeans(String tableExercisesAll, String tableReps, String tableMethods) {
        exerciseBean = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByString(TABLE_EXERCISES_ALL, DBExercisesHelper.MUSCLE, i.getInt(MUSCLE));
        methodBean = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(TABLE_METHODS);
        repsBean = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(TABLE_REPS);
    }*/

  /*
    public void letTheDogsOut(View v, RadioGroup rg, ArrayList<Beans> array, @DrawableRes int bG, boolean exercise, boolean reps) {
        rg.setOnCheckedChangeListener(this);
        RadioGroup.LayoutParams params;
        if (exercise) {
            params = new RadioGroup.LayoutParams(150, 150);
        } else {
            params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.setMargins(10, 0, 0, 0);

        for (int j = 0; j < array.size(); j++) {
            ToggleButton tg = new ToggleButton(getContext());
            tg.setBackgroundResource(bG);
            tg.setLayoutParams(params);
            tg.setBean(j + hundred);
            tg.setOnCheckedChangeListener(this);
            if (!exercise) {
                if (reps) {
                    String s = array.get(j).getDetails().replaceAll("\\\\n", "\\\n");
                    tg.setText(s);
                    tg.setTextOn(s);
                    tg.setTextOff(s);
                    tg.setAllCaps(false);
                } else {
                    tg.setText(array.get(j).getName());
                    tg.setTextOn(array.get(j).getName());
                    tg.setTextOff(array.get(j).getName());
                    tg.setAllCaps(false);
                }


            } else {
                tg.setText("");
                tg.setTextOn("");
                tg.setTextOff("");
                tg.setAllCaps(false);
            }
            if (j == 0) {
                tg.setChecked(true);
                prevIds[prevCounter++] = tg.getWorkoutId();
                exerciseNameTV.setText(exerciseBean.get(j).getName());
                exerciseDetailTV.setText(exerciseBean.get(j).getDetails());
                methodsTV.setText(methodBean.get(j).getName());
            }
            tg.setOnClickListener(cListener);
            rg.addView(tg);

        }
        rg.setOnCheckedChangeListener(this);
    }

    private void checkItems(int index) {
        if (index < 0)
            return;

        int position = exProfileCheckedItems[index];
        RadioGroup rg = radioGroups[index];
        ToggleButton tg = ((ToggleButton) rg.findViewById(position + hundred));
        tg.performClick();
        tg.setChecked(true);
        checkItems(index - 1);
    }

    public boolean doYouHaveWeapon() {
        return newBeansHolder.getExercise() == null;
    }

   *//* public void setCommanders(View v) {
        rg1 = (RadioGroup) v.findViewById(R.id.choose_fragment_rG1);
        rg2 = (RadioGroup) v.findViewById(R.id.choose_fragment_rG2);
        rg3 = (RadioGroup) v.findViewById(R.id.choose_fragment_rG3);

    }*//*
*/