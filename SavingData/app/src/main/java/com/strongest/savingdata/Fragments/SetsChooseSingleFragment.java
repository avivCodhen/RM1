package com.strongest.savingdata.Fragments;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.strongest.savingdata.Activities.BaseActivity;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.SingleChoiceAdapter;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.NumberChooseManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.RangeNumberChooseView;
import com.strongest.savingdata.MyViews.RestChooseView;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.ChooseDialogFragment;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REST;

/**
 * Created by Cohen on 3/15/2018.
 */

public class SetsChooseSingleFragment extends BaseCreateProgramFragment {

    private RangeNumberChooseView mRangeNumberChoose;
    private RecyclerView mRepRecycler, mRestRecycler;
    private SingleChoiceAdapter mRepAdapter, mRestAdapter;
    private RestChooseView mRestChooseView;
    private CheckBox customWeightCB;
    private View weightLayout;
    private EditText weightET;
    private ImageView keyboardIV;

    private ExerciseSet exerciseSet;

    private PLObject.ExerciseProfile exerciseProfile;
    private PLObject plObject;
    private PLObject.SetsPLObject setsPLObject;
    private PLObject.IntraSetPLObject intraSetPLObject;
    private LayoutManager.LayoutManagerHelper helper;
    private NumberChooseManager numberChooseManager;
    private DataManager dataManager;
    ArrayList<String> listRep;

    ArrayList<String> listRest;


    //   private int setPos;
    //   private int intraSetPos;

    public static final String SET_POS = "set_pos", INTRA_SET_POS = "intra_set_pos";

    public static SetsChooseSingleFragment getInstance(PLObject setsPLObject) {
        SetsChooseSingleFragment f = new SetsChooseSingleFragment();
        Bundle b = new Bundle();
        b.putSerializable(ChooseDialogFragment.PLOBJECT, setsPLObject);
        /*b.putInt(SET_POS, setPos);
        b.putInt(INTRA_SET_POS, intraSetPos);*/
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = ((BaseActivity) getActivity()).programmer.layoutManager.mLayoutManagerHelper;
        if (getArguments() != null) {
            plObject = (PLObject) getArguments().getSerializable(ChooseDialogFragment.PLOBJECT);
            if (plObject instanceof PLObject.SetsPLObject) {
                setsPLObject = (PLObject.SetsPLObject) plObject;
                exerciseSet = setsPLObject.getExerciseSet();
            } else {
                intraSetPLObject = (PLObject.IntraSetPLObject) plObject;
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
        numberChooseManager = new NumberChooseManager(helper, exerciseSet);
        mRestChooseView = (RestChooseView) v.findViewById(R.id.sets_choose_restchooseview);
        mRestChooseView.setUpWithManager(numberChooseManager);
        mRangeNumberChoose = (RangeNumberChooseView) v.findViewById(R.id.fragment_sets_choose_rangenumberview);
        mRangeNumberChoose.setUpWithNumberChooseManager(numberChooseManager);
        RecyclerView.LayoutManager lmRep = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager lmRest = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mRepRecycler = (RecyclerView) v.findViewById(R.id.fragment_sets_choose_recycler_repetitions);
        mRestRecycler = (RecyclerView) v.findViewById(R.id.fragment_sets_choose_recycler_rest);
        dataManager = ((BaseActivity) getActivity()).getDataManager();

        listRep = dataManager.getExerciseDataManager().readListByTable(TABLE_REPS);

        listRest = dataManager.getExerciseDataManager().readListByTable(DBExercisesHelper.TABLE_REST);
        mRepAdapter = new SingleChoiceAdapter(getContext(), listRep);
        mRestAdapter = new SingleChoiceAdapter(getContext(), listRest);
        mRestRecycler.setLayoutManager(lmRep);
        mRepRecycler.setLayoutManager(lmRest);
        mRepRecycler.setAdapter(mRepAdapter);
        mRestRecycler.setAdapter(mRestAdapter);

        weightLayout = v.findViewById(R.id.choose_sets_weight_layout);
        initWeightViews(v);



    }

    private void initWeightViews(View v){
        weightET = (EditText) v.findViewById(R.id.choose_sets_weight_et);
        weightET.setText(exerciseSet.getWeight()+"");
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
                double d = weightET.getText().toString().equals("") ? 0.0 : Double.parseDouble(weightET.getText().toString());
               exerciseSet.setWeight(d);
               helper.onExerciseSetChange();
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
        if(listRep.size() == 0){
            dataManager.getExerciseDataManager().insertData(TABLE_REPS, numberChooseManager.getExerciseSet().getRep());
            repFlag = true;
        }
        if(listRest.size() == 0){
            dataManager.getExerciseDataManager().insertData(TABLE_REST, numberChooseManager.getExerciseSet().getRest());
            restFlag = true;
        }

        for (String rep : listRep){
            if(rep.equals(numberChooseManager.getExerciseSet().getRep())){
                repFlag = true;
            }
        }
        for (String rest : listRest){
            if(rest.equals(numberChooseManager.getExerciseSet().getRest())){
                restFlag = true;
            }
        }
        if(!repFlag){
            insertData(TABLE_REPS, numberChooseManager.getExerciseSet().getRep());
        }
        if(!restFlag){
            insertData(TABLE_REST, numberChooseManager.getExerciseSet().getRest());
        }
    }

    public void insertData(String tableName, String name){
        dataManager.getExerciseDataManager().insertData(tableName, name);

    }
}
