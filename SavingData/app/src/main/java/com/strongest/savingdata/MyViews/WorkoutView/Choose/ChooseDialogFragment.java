package com.strongest.savingdata.MyViews.WorkoutView.Choose;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.TimingLogger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.BeansHolder;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.MyViews.MySelector.ChooseSelectorAdapter;
import com.strongest.savingdata.MyViews.MyViewPager;
import com.strongest.savingdata.MyViews.WorkoutView.WorkoutSubject;
import com.strongest.savingdata.MyViews.WorkoutView.WorkoutView;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

import java.util.ArrayList;

public class ChooseDialogFragment extends BaseCreateProgramFragment implements View.OnClickListener,
        TabLayout.OnTabSelectedListener, WorkoutSubject {

    public static final String LISTENER = "listener";
    private TabLayout tabLayout;
    private MyViewPager viewPager;
    /* private TextView exerciseNameTV, exerciseDetailTV;
     private TextView methodsTV;*/
    private Bundle i;
    private Intent intent;
    private ArrayList<BeansHolder> newBeansHolders;
    private ArrayList<BeansHolder> prevBeansHolders;

    private DataManager dataManager;
    private ArrayList<ArrayList<Beans>> beans_array = new ArrayList<>();
    //  private static final int hundred = 100;
    private ChooseSelectorAdapter adapter;
    private ChooseDataListener chooseDataListener;

    private OptionalChooseDialogFragment optionalFragment;
    private MandatoryChooseDialogFragment mandatoryFragment;

    private WorkoutView workoutView;
    //private View exerciseView;
    private PLObjects.ExerciseProfile exerciseProfile;
    private String tName;
    private Button backBtn;
    TimingLogger timings;

  /*  private RadioGroup rg1;
    private RadioGroup rg2;
    private RadioGroup rg3;
    private RadioGroup rg4;*/

    /*private View.OnClickListener cListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getParent() == radioGroups[0]) {
                exerciseNameTV.setText(exerciseBean.get(v.getWorkoutId() - hundred).getName());
                exerciseDetailTV.setText(exerciseBean.get(v.getWorkoutId() - hundred).getDetails());
            } else if (v.getParent() == radioGroups[2]) {
                methodsTV.setText(methodBean.get(v.getWorkoutId() - hundred).getDetails());
            }
            onCheckedChanged((RadioGroup) v.getParent(), v.getWorkoutId());
        }
    };*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x22000000));
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(100);

        //getDialog().getWindow().setBackgroundDrawable(d);
        View v = inflater.inflate(R.layout.fragment_choose_dialog, container, false);
        //only for this to be fragment (and not dialog).
        /*TimingLogger t = new TimingLogger("aviv", "forlilipop");
        // forLOLLIPOP(v);
        //forLOLLIPOP(v);
        t.addSplit("after lolipop");
        t.dumpToLog();*/
        return v;
    }

   /* @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void forLOLLIPOP(View v) {
        View exerciseView = LayoutInflater.from(getContext()).inflate(R.layout.exercise_choose_transition, (ViewGroup) v, false);
        //exerciseView.setTransitionName(exerciseProfile.getExerciseProfileId()+"");
        ((ViewGroup) v).addView(exerciseView, 0);
        ExerciseViewHolder vh3 = new ExerciseViewHolder(exerciseView);
        nothing(vh3);
    }*/


   /* @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void nothing(ExerciseViewHolder vh3) {
        if (exerciseProfile.getBeansHolder() != null && exerciseProfile.getBeansHolder() != null) {
            //vh3.name.setTransitionName(exerciseProfile.getExerciseProfileId() + vh3.name.getId() + "");
            // vh3.layout.setTransitionName(tName);
            vh3.name.setText(exerciseProfile.getBeansHolder().getExercise().getName());
            vh3.reps.setText(exerciseProfile.getBeansHolder().getRep().getName());
            vh3.sets.setTransitionName(tName);
            vh3.sets.setText(exerciseProfile.getBeansHolder().getSets() + "");
            vh3.rest.setText(exerciseProfile.getBeansHolder().getRest().getName());
            if (exerciseProfile.getBeansHolder().getMethod() != null) {
                vh3.method.setText(exerciseProfile.getBeansHolder().getMethod().getName());
            } else {
                vh3.method.setText("");
            }
            if ((exerciseProfile.getBeansHolder().getWeight() == 0)) {
                vh3.weight.setText("W: -");
            } else {
                vh3.weight.setText("W: " + exerciseProfile.getBeansHolder().getWeight());
            }
        } else

        {
            vh3.name.setText("");
            vh3.reps.setText("");
            vh3.method.setText("");
            vh3.sets.setText("");
            vh3.rest.setText("");
            vh3.weight.setText("W: -");
            vh3.icon.setImageResource(R.drawable.ic_add_black_18dp);
        }
        //timings.addSplit("after nothingvh3");
        startPostponedEnterTransition();

    }*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
        backBtn = (Button) v.findViewById(R.id.choose_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getParentFragment().getFragmentManager().popBackStack();
                getParentFragment().getChildFragmentManager().popBackStack();

                //getFragmentManager().popBackStackImmediate("unique", 0);
            }
        });
        dataManager = new DataManager(getContext());
        tabLayout = (TabLayout) v.findViewById(R.id.choose_dialog_tablayout);
        i = getArguments();
      //  if (exerciseView)
            prevBeansHolders = exerciseProfile.getBeansHolders();

        //not for lolipop!
        //prevBeansHolders = (BeansHolder) i.getSerializable("beans_holder");

        ChooseData data = new ChooseData();
        //data.setBeansHolder(newBeansHolder);
        data.setBeansHolder(prevBeansHolders);

        //not for lolipop!
        //data.setMuscle(getArguments().getInt(MUSCLE));

        data.setMuscle(exerciseProfile.getMuscle());
        //   TimingLogger t = new TimingLogger("aviv", "for viewpager");

        mandatoryFragment = MandatoryChooseDialogFragment.newInstance(data);
        optionalFragment = OptionalChooseDialogFragment.newInstance(data);
        ChooseAdapter adapter = new ChooseAdapter(getChildFragmentManager(),
                data,
                chooseDataListener,
                mandatoryFragment,
                optionalFragment);
        viewPager = (MyViewPager) v.findViewById(R.id.choose_dialog_viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        //    t.addSplit("after after viewpager");
        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
        v.findViewById(R.id.choose_floating_button_confirm).setOnClickListener(this);
        Attach(workoutView);
        intent = new Intent();
        //  t.addSplit("after after all");
        //  t.dumpToLog();

//        timings.addSplit("after all");

    }

    public Resources resources() {
        return getContext().getResources();
    }

    @Override
    public void onClick(View v) {
        ArrayList<BeansHolder> mBH = new ArrayList<>();

        if (mandatoryFragment.getMandatoryBeansHolder() != null) {
            mBH.add(mandatoryFragment.getMandatoryBeansHolder());
           /* if (prevBeansHolders == null) {
                newBeansHolder = new BeansHolder();
            }*/
           /* newBeansHolder = new BeansHolder();
            newBeansHolder.setExercise(mBH.getExercise());
            newBeansHolder.setSets(mBH.getSets());
            newBeansHolder.setRep(mBH.getRep());
            newBeansHolder.setRest(mBH.getRest());*/
        } else {
            Toast.makeText(getContext(), "To save the exercise you need to pick all the Mandatory options.", Toast.LENGTH_SHORT).show();
            return;
        }
       /* if (optionalFragment.getOptionalBeansHolder() != null) {
            BeansHolder oBh = optionalFragment.getOptionalBeansHolder();
            if (oBh.getMethod() != null) {
                newBeansHolder.setMethod(oBh.getMethod());
                newBeansHolder.setHasmethod(true);
            }
            newBeansHolder.setWeight(oBh.getWeight());

        }*/

        Notify(mBH, exerciseProfile.getExerciseProfileId(), false/*BeansHolder.compareBeansHolders(prevBeansHolders, mBH)*/);
        //dismiss();
    }


  /*  private boolean vaildate(MySelector.CheckedHolder[] checkHolder) {
        if (checkHolder[Exercise.ordinal()] == null && newBeansHolders.getExercise() == null ||
                checkHolder[Reps.ordinal()] == null && newBeansHolders.getRep() == null) {
            return false;
        }
        return true;
    }*/

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        chooseDataListener = (ChooseDataListener) context;
    }

    @Override
    public void Attach(WorkoutView o) {
        workoutView = (WorkoutView) o;
    }

    @Override
    public void Dettach(WorkoutView o) {

    }

    @Override
    public void Notify(ArrayList<BeansHolder> beanHolders, int position, boolean changed) {
        ((WorkoutView.WorkoutViewFragment) getTargetFragment()).exerciseOnClick(beanHolders, position, changed);
        getParentFragment().getChildFragmentManager().popBackStack();
    }

    public void setWorkoutView(WorkoutView workoutView) {
        this.workoutView = workoutView;
    }

    public void setExerciseProfile(PLObjects.ExerciseProfile exerciseProfile) {
        this.exerciseProfile = exerciseProfile;
    }

    public void setUniqueTransitionName(String tName) {
        this.tName = tName;

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
            tg.setId(j + hundred);
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