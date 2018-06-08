package com.strongest.savingdata.Fragments;

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

import com.strongest.savingdata.Activities.BaseActivity;
import com.strongest.savingdata.Adapters.OnSingleChoiceAdapterOnclickListener;
import com.strongest.savingdata.Adapters.SingleChoiceAdapter;
import com.strongest.savingdata.AlgorithmLayout.LayoutManagerHelper;
import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.NumberChooseManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.RangeNumberChooseView;
import com.strongest.savingdata.MyViews.RestChooseView;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.ChooseDialogFragment;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.OnExerciseSetChange;
import com.strongest.savingdata.R;
import com.strongest.savingdata.Utils.MyUtils;

import java.util.ArrayList;
import java.util.Collections;

import static android.widget.GridLayout.HORIZONTAL;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REST;

/**
 * Created by Cohen on 3/15/2018.
 */

public class SetsChooseSingleFragment extends BaseCreateProgramFragment implements OnSingleChoiceAdapterOnclickListener {

    private RangeNumberChooseView mRangeNumberChoose;
    private RecyclerView mRepRecycler, mRestRecycler;
    private SingleChoiceAdapter mRepAdapter, mRestAdapter;
    private RestChooseView mRestChooseView;
    private CheckBox customWeightCB;
    private View weightLayout;
    private EditText weightET;
    private ImageView keyboardIV;

    private TextSwitcher currentRestTv, currentRepTv, currentWeightTV;

    private ExerciseSet exerciseSet;

    private PLObject plObject;
    private PLObject.SetsPLObject setsPLObject;
    PLObject.ExerciseProfile parent;
    private PLObject.IntraSetPLObject intraSetPLObject;
    // private LayoutManager.LayoutManagerHelper helper;
    private NumberChooseManager numberChooseManager;
    private DataManager dataManager;
    ArrayList<String> listRep;
    private OnExerciseSetChange onExerciseSetChange;

    ArrayList<String> listRest;


    //   private int setPos;
    //   private int intraSetPos;

    public static final String SET_POS = "set_pos", INTRA_SET_POS = "intra_set_pos";

    public static SetsChooseSingleFragment getInstance(PLObject setsPLObject, OnExerciseSetChange onExerciseSetChange) {
        SetsChooseSingleFragment f = new SetsChooseSingleFragment();
        Bundle b = new Bundle();
        b.putSerializable(ChooseDialogFragment.PLOBJECT, setsPLObject);
        f.setOnExerciseSetChange(onExerciseSetChange);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  helper = ((BaseActivity) getActivity()).programmer.layoutManager.mLayoutManagerHelper;
        if (getArguments() != null) {
            plObject = (PLObject) getArguments().getSerializable(ChooseDialogFragment.PLOBJECT);
            if (plObject instanceof PLObject.SetsPLObject) {
                setsPLObject = (PLObject.SetsPLObject) plObject;
                exerciseSet = setsPLObject.getExerciseSet();
                parent = setsPLObject.getParent();
            } else {
                intraSetPLObject = (PLObject.IntraSetPLObject) plObject;
                parent = intraSetPLObject.getParent();

                exerciseSet = intraSetPLObject.getExerciseSet();
            }
            /*setPos = getArguments().getInt(SET_POS);
            intraSetPos = getArguments().getInt(INTRA_SET_POS);*/
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_sets, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

    }

    private void initViews(View v) {
        numberChooseManager = new NumberChooseManager(onExerciseSetChange, exerciseSet);
        currentRepTv = (TextSwitcher) v.findViewById(R.id.sets_current_reps);
        currentRestTv = (TextSwitcher) v.findViewById(R.id.sets_current_rest);
        currentWeightTV = (TextSwitcher) v.findViewById(R.id.sets_current_weight);

        initTextSwitcher(currentRestTv, exerciseSet.getRest());
        initTextSwitcher(currentRepTv, exerciseSet.getRep());
        initTextSwitcher(currentWeightTV, exerciseSet.getRep());
        numberChooseManager.setRepsTextSwitcher(currentRepTv);
        numberChooseManager.setRestTextSwitcher(currentRestTv);


        mRestChooseView = (RestChooseView) v.findViewById(R.id.sets_choose_restchooseview);
        mRestChooseView.setUpWithManager(numberChooseManager);
        mRangeNumberChoose = (RangeNumberChooseView) v.findViewById(R.id.fragment_sets_choose_rangenumberview);
        mRangeNumberChoose.setUpWithNumberChooseManager(numberChooseManager);
        RecyclerView.LayoutManager lmRep = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager lmRest = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mRepRecycler = (RecyclerView) v.findViewById(R.id.fragment_sets_choose_recycler_repetitions);
        mRestRecycler = (RecyclerView) v.findViewById(R.id.fragment_sets_choose_recycler_rest);
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

        weightLayout = v.findViewById(R.id.choose_sets_weight_layout);
        initWeightViews(v);
        initApplyBtns(v);




    }

    private void initTextSwitcher(TextSwitcher textSwitcher, String text) {
        textSwitcher.setInAnimation( getContext(),android.R.anim.slide_in_left);
        textSwitcher.setOutAnimation(getContext(),  android.R.anim.slide_out_right);
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView t = new TextView(getContext());
                t.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                t.setGravity(Gravity.CENTER_VERTICAL);
                t.setTextColor(ContextCompat.getColor(getContext(),R.color.text_gray));
                return t;
            }
        });
        textSwitcher.setText(text);

    }

    private void initApplyBtns(View v) {
        v.findViewById(R.id.choose_sets_reps_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rep = mRangeNumberChoose.rep;
                applyForAll(rep, "rep");
                cancelAndToast(v);

            }
        });

        v.findViewById(R.id.choose_sets_rest_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rest = exerciseSet.getRest();
                applyForAll(rest, "rest");
                cancelAndToast(v);

            }
        });

        v.findViewById(R.id.choose_sets_weight_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double weight = exerciseSet.getWeight();
                applyForAll(String.valueOf(weight), "weight");
                cancelAndToast(v);
            }
        });
    }

    private void cancelAndToast(View v) {
        Toast.makeText(getContext(), "Data applied to all sets", Toast.LENGTH_SHORT).show();
        MyUtils.Interface.disableClick(v, 500);
    }

    private void applyForAll(String object, String type) {
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

    }

    private void initWeightViews(View v) {
        weightET = (EditText) v.findViewById(R.id.choose_sets_weight_et);
        weightET.setText(exerciseSet.getWeight() + "");
        keyboardIV = (ImageView) v.findViewById(R.id.choose_sets_keyboard_iv);
        customWeightCB = (CheckBox) v.findViewById(R.id.choose_sets_customweight);
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
                    onExerciseSetChange.notifyExerciseSetChange();
                    currentWeightTV.setText(d+" kg");
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
        onExerciseSetChange.notifyExerciseSetChange();
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

    public void setOnExerciseSetChange(OnExerciseSetChange onExerciseSetChange) {
        this.onExerciseSetChange = onExerciseSetChange;
    }
}
