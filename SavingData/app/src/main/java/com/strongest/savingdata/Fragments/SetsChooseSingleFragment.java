package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.strongest.savingdata.AViewModels.SelectedExerciseViewModel;
import com.strongest.savingdata.AViewModels.SelectedSetViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Activities.BaseActivity;
import com.strongest.savingdata.Adapters.OnSingleChoiceAdapterOnclickListener;
import com.strongest.savingdata.Adapters.SingleChoiceAdapter;
import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.NumberChooseManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.RangeNumberChooseView;
import com.strongest.savingdata.MyViews.RestChooseView;
import com.strongest.savingdata.Fragments.Choose.ChooseDialogFragment;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.R;
import com.strongest.savingdata.Utils.MyUtils;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.GridLayout.HORIZONTAL;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REST;

/**
 * Created by Cohen on 3/15/2018.
 */

public class SetsChooseSingleFragment extends BaseCreateProgramFragment implements OnSingleChoiceAdapterOnclickListener {


    public static final String SETS_CHOOSE_FRAGMENT = "setsfragment";
    @BindView(R.id.fragment_sets_choose_rangenumberview)
    RangeNumberChooseView mRangeNumberChoose;

    @BindView(R.id.fragment_sets_choose_recycler_repetitions)
    RecyclerView mRepRecycler;

    @BindView(R.id.fragment_sets_choose_recycler_rest)
    RecyclerView mRestRecycler;


    @BindView(R.id.sets_choose_restchooseview)
    RestChooseView mRestChooseView;

    @BindView(R.id.choose_sets_customweight)
    CheckBox customWeightCB;

    @BindView(R.id.choose_sets_weight_layout)
    View weightLayout;

    @BindView(R.id.choose_sets_weight_et)
    EditText weightET;

    @BindView(R.id.choose_sets_keyboard_iv)
    ImageView keyboardIV;

    @BindView(R.id.sets_current_reps)
    TextSwitcher currentRepTv;
    @BindView(R.id.sets_current_rest)
    TextSwitcher currentRestTv;
    @BindView(R.id.sets_current_weight)
    TextSwitcher currentWeightTV;

    @BindView(R.id.set_edit_saveExitToolbar)
    SaveExitToolBar saveExitToolBar;
    SingleChoiceAdapter mRepAdapter, mRestAdapter;

    private ExerciseSet exerciseSet;

    private PLObject plObject;
    private PLObject.SetsPLObject setsPLObject;
    PLObject.ExerciseProfile parent;
    //private PLObject.IntraSetPLObject intraSetPLObject;
    private NumberChooseManager numberChooseManager;
    private DataManager dataManager;
    ArrayList<String> listRep;

    ArrayList<String> listRest;

    SelectedSetViewModel selectedSetViewModel;
    WorkoutsViewModel workoutsViewModel;
    SelectedExerciseViewModel selectedExerciseViewModel;

    public static final String SET_POS = "set_pos", INTRA_SET_POS = "intra_set_pos";

    public static SetsChooseSingleFragment getInstance() {
        SetsChooseSingleFragment f = new SetsChooseSingleFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plObject = (PLObject) getArguments().getSerializable(ChooseDialogFragment.PLOBJECT);
          /*  if (plObject instanceof PLObject.SetsPLObject) {
                setsPLObject = (PLObject.SetsPLObject) plObject;
                exerciseSet = setsPLObject.getExerciseSet();
                parent = setsPLObject.getParent();
            } else {
        //        intraSetPLObject = (PLObject.IntraSetPLObject) plObject;
               // parent = intraSetPLObject.getParent();

         //       exerciseSet = intraSetPLObject.getExerciseSet();
            }*/
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_sets, container, false);
        ButterKnife.bind(this, v);
        workoutsViewModel = ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);
        selectedSetViewModel = ViewModelProviders.of(getActivity()).get(SelectedSetViewModel.class);
        dataManager = workoutsViewModel.getDataManager();
        setsPLObject = selectedSetViewModel.getSelectedSets();
        exerciseSet = new ExerciseSet(setsPLObject.getExerciseSet());
        selectedExerciseViewModel = ViewModelProviders.of(getActivity()).get(SelectedExerciseViewModel.class);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

    }

    private void initViews(View v) {
        numberChooseManager = new NumberChooseManager(exerciseSet);


        initTextSwitcher(
                //make an array out of all the exerciseset's strings
                MyUtils.stringsToArray(exerciseSet.getRep(), exerciseSet.getRest(), String.valueOf(exerciseSet.getWeight())),
                currentRepTv, currentWeightTV, currentRestTv);
        numberChooseManager.setRepsTextSwitcher(currentRepTv);
        numberChooseManager.setRestTextSwitcher(currentRestTv);


        mRestChooseView.setUpWithManager(numberChooseManager);
        mRangeNumberChoose.setUpWithNumberChooseManager(numberChooseManager);
        RecyclerView.LayoutManager lmRep = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager lmRest = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        dataManager = ((BaseActivity) getActivity()).getDataManager();
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        mRepRecycler.addItemDecoration(itemDecor);
        mRestRecycler.addItemDecoration(itemDecor);

        listRep = dataManager.getExerciseDataManager().readListByTable(TABLE_REPS);

        listRest = dataManager.getExerciseDataManager().readListByTable(DBExercisesHelper.TABLE_REST);
        Collections.reverse(listRest);
        Collections.reverse(listRest);
        mRepAdapter = new SingleChoiceAdapter(getContext(), listRep, this, "rep");
        mRestAdapter = new SingleChoiceAdapter(getContext(), listRest, this, "rest");
        mRestRecycler.setLayoutManager(lmRep);
        mRepRecycler.setLayoutManager(lmRest);
        mRepRecycler.setAdapter(mRepAdapter);
        mRestRecycler.setAdapter(mRestAdapter);

        initWeightViews(v);
        initApplyBtns(v);

        initToolbar();

    }

    private void initToolbar() {
        saveExitToolBar.instantiate();

        saveExitToolBar.setSaveButton((v) ->{
            setsPLObject.setExerciseSet(exerciseSet);
            selectedSetViewModel.modifySetSelected(exerciseSet);
            getFragmentManager().popBackStack();
        });

        saveExitToolBar.setCancelButton(v -> getFragmentManager().popBackStack());
    }

    /**
     * instantiates and displays the current exercise set data in the textswitchers
     */

    private void initTextSwitcher(String[] texts, TextSwitcher... textSwitchers) {
        int i = 0;
        for (TextSwitcher textSwitcher : textSwitchers) {
            textSwitcher.setInAnimation(getContext(), android.R.anim.slide_in_left);
            textSwitcher.setOutAnimation(getContext(), android.R.anim.slide_out_right);
            textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    TextView t = new TextView(getContext());
                    t.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    t.setGravity(Gravity.CENTER_VERTICAL);
                    t.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                    return t;
                }
            });
            textSwitcher.setText(texts[i]);
            i++;
        }
    }

    private void initApplyBtns(View v) {
        v.findViewById(R.id.choose_sets_reps_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rep = mRangeNumberChoose.rep;
             //   applyForAll(rep, "rep");
                cancelAndToast(v);

            }
        });

        v.findViewById(R.id.choose_sets_rest_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rest = exerciseSet.getRest();
           //     applyForAll(rest, "rest");
                cancelAndToast(v);

            }
        });

        v.findViewById(R.id.choose_sets_weight_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double weight = exerciseSet.getWeight();
            //    applyForAll(String.valueOf(weight), "weight");
                cancelAndToast(v);
            }
        });
    }

    private void cancelAndToast(View v) {
        Toast.makeText(getContext(), "Data applied to all sets", Toast.LENGTH_SHORT).show();
        MyUtils.Interface.disableClick(v, 500);
    }

    /**
     * this is a function to provide "apply all" functionality where
     * on button press, the data is transfered to all the sets
     */
  /*  private void applyForAll(String object, String type) {
        ArrayList<PLObject.SetsPLObject> setsList = WorkoutIListHelper
                .getHelper(setsPLObject.getParent().getSets()).getAllOftype();
        if (intraSetPLObject != null) {
            if (intraSetPLObject.innerType == WorkoutLayoutTypes.SuperSetIntraSet) {
                for (int i = 0; i < intraSetPLObject.getParent().getIntraSets().size(); i++) {
                    if (type.equals("rep")) {
                        intraSetPLObject.getParent().getIntraSets().get(i).getExerciseSet().setRep(object);
                    }
                    if (type.equals("rest")) {
                        intraSetPLObject.getParent().getIntraSets().get(i).getExerciseSet().setRest(object);
                    }
                    if (type.equals("weight")) {
                        intraSetPLObject.getParent().getIntraSets().get(i).getExerciseSet().
                                setWeight(Double.parseDouble(object));

                    }
                }
            } else {
                int dropsetPosition = LayoutManagerHelper.findIntraSetPosition(intraSetPLObject);
                for (int i = 0; i < intraSetPLObject.getParent().getSets().size(); i++) {
                    PLObject.SetsPLObject set = intraSetPLObject.getParent().getSets().get(i);
                    if (dropsetPosition < set.getIntraSets().size()) {
                        if (type.equals("rep")) {
                            set.getIntraSets().get(dropsetPosition).getExerciseSet().setRep(object);
                        }
                        if (type.equals("rest")) {
                            set.getIntraSets().get(dropsetPosition).getExerciseSet().setRest(object);
                        }
                        if (type.equals("weight")) {
                            set.getIntraSets().get(dropsetPosition).getExerciseSet().
                                    setWeight(Double.parseDouble(object));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < setsPLObject.getParent().getSets().size(); i++) {
                if (type.equals("rep")) {
                    setsPLObject.getParent().getSets().get(i).getExerciseSet().setRep(object);
                }
                if (type.equals("rest")) {
                    setsPLObject.getParent().getSets().get(i).getExerciseSet().setRest(object);
                }
                if (type.equals("weight")) {
                    setsPLObject.getParent().getSets().get(i).getExerciseSet().
                            setWeight(Double.parseDouble(object));
                }
            }
        }

    }*/

    private void initWeightViews(View v) {
        weightET.setText(exerciseSet.getWeight() + "");
        customWeightCB.setChecked(exerciseSet.isCustomWeight());
        weightET.setEnabled(exerciseSet.isCustomWeight());
        keyboardIV.setEnabled(exerciseSet.isCustomWeight());
        customWeightCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                weightET.setEnabled(isChecked);
                keyboardIV.setEnabled(isChecked);
                exerciseSet.setCustomWeight(isChecked);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!weightET.getText().toString().equals(".")) {
                    double d = weightET.getText().toString().equals("") ? 0.0 : Double.parseDouble(weightET.getText().toString());

                    exerciseSet.setWeight(d);
                    //    onExerciseSetChange.notifyExerciseSetChange();
                    currentWeightTV.setText(d + " kg");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                weightET.setSelection(s.length());

            }
        };
        weightET.addTextChangedListener(textWatcher);


    }

    @Override
    public void onDetach() {
        saveIfNotExist();

        super.onDetach();
    }

    private void saveIfNotExist() {
        boolean repFlag = false;
        boolean restFlag = false;
        if (listRep.size() == 0) {
            dataManager.getExerciseDataManager().insertData(TABLE_REPS, numberChooseManager.getExerciseSet().getRep());
            repFlag = true;
        }
        if (listRest.size() == 0) {
            dataManager.getExerciseDataManager().insertData(TABLE_REST, numberChooseManager.getExerciseSet().getRest());
            restFlag = true;
        }

        for (String rep : listRep) {
            if (rep.equals(numberChooseManager.getExerciseSet().getRep())) {
                repFlag = true;
            }
        }
        for (String rest : listRest) {
            if (rest.equals(numberChooseManager.getExerciseSet().getRest())) {
                restFlag = true;
            }
        }
        if (!repFlag) {
            insertData(TABLE_REPS, numberChooseManager.getExerciseSet().getRep());
        }
        if (!restFlag) {
            insertData(TABLE_REST, numberChooseManager.getExerciseSet().getRest());
        }
    }

    public void insertData(String tableName, String name) {
        dataManager.getExerciseDataManager().insertData(tableName, name);

    }

    @Override
    public void onClick(String object, String type) {
        if (type.equals("rep")) {
            exerciseSet.setRep(object);
            mRangeNumberChoose.initRep(exerciseSet.getRep());
            currentRepTv.setText(object);

        } else if (type.equals("rest")) {
            exerciseSet.setRest(object);
            mRestChooseView.initRest(exerciseSet.getRest());
            currentRestTv.setText(object);
        }
        //    onExerciseSetChange.notifyExerciseSetChange();
        // helper.onOnlyItemChange();
    }

    @Override
    public void onLongclick(String object, String type) {
        String table;
        if (type.equals("rep")) {
            table = TABLE_REPS;
        } else {
            table = TABLE_REST;
        }
        dataManager.getExerciseDataManager().removeByName(table, object);

    }

}
