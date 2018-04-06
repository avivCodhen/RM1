package com.strongest.savingdata.MyViews.WorkoutView.Choose;

import android.content.res.Resources;
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
import android.widget.TextView;

import com.strongest.savingdata.Activities.BaseActivity;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmLayout.LayoutManagerHelper;
import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AlgorithmLayout.ReactLayoutManager;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Fragments.ExerciseChooseFragment;
import com.strongest.savingdata.Fragments.SetsChooseSingleFragment;
import com.strongest.savingdata.MyViews.MySelector.MySelectorOnBeansHolderChange;
import com.strongest.savingdata.MyViews.WorkoutView.OnExerciseChangeListener;
import com.strongest.savingdata.MyViews.WorkoutView.OnWorkoutViewInterfaceClicksListener;
import com.strongest.savingdata.MyViews.WorkoutView.WorkoutView;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChooseDialogFragment extends BaseCreateProgramFragment implements View.OnClickListener,
        TabLayout.OnTabSelectedListener, MySelectorOnBeansHolderChange, OnExerciseSetChange {

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

    private PLObject plObject;
    private PLObject.ExerciseProfile exerciseProfile;
    private PLObject.SetsPLObject setsPLObject;
    private PLObject.IntraSetPLObject intraSetPLObject;

    private String tName;
    private Button backBtn;
    private ReactLayoutManager mReactLayoutManager;

    private OnExerciseChangeListener onExerciseChangeListener;

    private int plObjectPosition;

    private RecyclerView.ViewHolder vh;

    private int currentRecyclerPosition;
    private int currentTab;
    private boolean initiated;
    private RecyclerView mRecyclerView;
    public MyExpandableAdapter myExpandableAdapter;
    private ChooseAdapter adapter;
   // private LayoutManager.LayoutManagerHelper helper;
    private int setPosition;
    private String title = "";

    public static ChooseDialogFragment getInstance(OnExerciseChangeListener onExerciseChangeListener,
                                                   int oldPosition, int position,
                                                   PLObject plObject, RecyclerView.ViewHolder vh) {
        ChooseDialogFragment f = new ChooseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PLOBJECT, plObject);
        bundle.putInt(POSITION, position);
        bundle.putInt(OLD_POSITION, oldPosition);
        f.setOnExerciseChangeListener(onExerciseChangeListener);
        f.setVh(vh);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
      //      helper = ((BaseActivity) getActivity()).getProgrammer().layoutManager.mLayoutManagerHelper;
            plObject = (PLObject) getArguments().getSerializable(PLOBJECT);
            title = LayoutManagerHelper.writeTitle(plObject);
            if (plObject instanceof PLObject.ExerciseProfile) {
                exerciseProfile = (PLObject.ExerciseProfile) plObject;
                adapter = new ChooseAdapter(getChildFragmentManager(), ExerciseChooseFragment.newInstance(exerciseProfile, this));

            } else if (plObject instanceof PLObject.SetsPLObject) {
                setsPLObject = (PLObject.SetsPLObject) plObject;
                exerciseProfile = setsPLObject.getParent();
                adapter = new ChooseAdapter(getChildFragmentManager(), SetsChooseSingleFragment.getInstance(setsPLObject, this));
            } else if (plObject instanceof PLObject.IntraSetPLObject) {
                intraSetPLObject = (PLObject.IntraSetPLObject) plObject;
                exerciseProfile = intraSetPLObject.getParent();
                adapter = new ChooseAdapter(getChildFragmentManager(), SetsChooseSingleFragment.getInstance(intraSetPLObject, this));
            }
            plObjectPosition = getArguments().getInt(POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                getActionBar().show();

            }
        }, 500);
        // ((HomeActivity) getActivity()).begoneTabLayout();
        View v = inflater.inflate(R.layout.fragment_choose, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
    //    nestedScrollView = (NestedScrollView) v.findViewById(R.id.fragment_choose_nestedscrollview);
        mLayout = new ArrayList<>();
        //  makeLayout(exerciseProfile);
        currentRecyclerPosition = plObjectPosition;
        dataManager = new DataManager(getContext());
        // i = getArguments();
        ArrayList<PLObject> layout = new ArrayList<>();
        layout.add(plObject);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_choose_recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAlpha(0f);
        MyJavaAnimator.fadeIn(mRecyclerView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 200);
        myExpandableAdapter = new MyExpandableAdapter(layout, getContext(), null, null, null);
        mRecyclerView.setAdapter(myExpandableAdapter);
      //  ((BaseActivity) getActivity()).programmer.layoutManager.mLayoutManagerHelper.setUpWithLayout(myExpandableAdapter, layout);
        // ((MyExpandableAdapter.ExerciseViewHolder) vh).card.setOnClickListener(null);
        viewPager = (ViewPager) v.findViewById(R.id.choose_dialog_viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);

        TextView title_tv = (TextView) v.findViewById(R.id.choose_add_set);
        title_tv.setText(title);


        v.findViewById(R.id.fragment_choose_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).workoutView.onExitChooseFragment(vh, getArguments().getInt(OLD_POSITION));
                // ((HomeActivity) getActivity()).reviveTabLayout();

                plObject.setEditMode(false);
                getFragmentManager().popBackStack();
            }
        });
        // Attach(workoutView);
        // intent = new Intent();
    }

    public Resources resources() {
        return getContext().getResources();
    }


    @Override
    public void onClick(View v) {
        ArrayList<ExerciseSet> mBH = new ArrayList<>();


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

    public void setExerciseProfile(PLObject.ExerciseProfile exerciseProfile) {
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
        onExerciseChangeListener.onExerciseChange(plObjectPosition, beanType);
*/
    }

    @Override
    public void notifyMuscleChange(Muscle m) {
        exerciseProfile.setMuscle(m);
        onExerciseChangeListener.onExerciseChange(plObjectPosition, "muscle");
    }

    public void setOnExerciseChangeListener(OnExerciseChangeListener onExerciseChangeListener) {
        this.onExerciseChangeListener = onExerciseChangeListener;
    }

    public void setPlObjectPosition(int plObjectPosition) {
        this.plObjectPosition = plObjectPosition;
    }

    public void setVh(RecyclerView.ViewHolder vh) {
        this.vh = vh;
    }


    @Override
    public void notifyExerciseSetChange() {
        myExpandableAdapter.notifyItemChanged(0);
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