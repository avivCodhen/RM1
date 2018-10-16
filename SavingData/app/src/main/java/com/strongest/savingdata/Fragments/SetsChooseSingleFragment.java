package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
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
import com.strongest.savingdata.Handlers.MaterialDialogHandler;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.NumberChooseManager;
import com.strongest.savingdata.Fragments.Choose.ChooseDialogFragment;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.R;
import com.strongest.savingdata.Utils.DoneOnEditorActionListener;
import com.strongest.savingdata.Utils.MyUtils;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REST;

/**
 * Created by Cohen on 3/15/2018.
 */

public class SetsChooseSingleFragment extends BaseFragment implements OnSingleChoiceAdapterOnclickListener, CompoundButton.OnCheckedChangeListener {


    public static final String SETS_CHOOSE_FRAGMENT = "setsfragment";

    @BindView(R.id.choose_sets_rep_tv)
    EditText reps1ED;

    @BindView(R.id.choose_sets_reps_cb)
    CheckBox repsToCB;

    @BindView(R.id.choose_sets_reps_range_et)
    EditText reps2ED;

    @BindView(R.id.choose_sets_rest_sec_tv)
    EditText secED;

    @BindView(R.id.choose_sets_rest_min_tv)
    EditText minED;

    @BindView(R.id.seconds_wrapper)
    ViewGroup secondsWrapper;

    @BindView(R.id.minutes_wrapper)
    ViewGroup minutesWrapper;

    @BindView(R.id.fragment_sets_choose_recycler_repetitions)
    RecyclerView mRepRecycler;

    @BindView(R.id.fragment_sets_choose_recycler_rest)
    RecyclerView mRestRecycler;


 /*   @BindView(R.id.choose_sets_customweight)
    CheckBox customWeightCB;
*/

    @BindView(R.id.choose_sets_weight_tv)
    EditText currentWeightTV;
    @BindView(R.id.choose_sets_weight_layout)
    View weightLayout;


    @BindView(R.id.set_edit_saveExitToolbar)
    SaveExitToolBar saveExitToolBar;

    @BindView(R.id.checkbox_reps)
    CheckBox repsCB;
    @BindView(R.id.checkbox_rest)
    CheckBox restCB;

    @BindView(R.id.checkbox_weight)
    CheckBox weightCB;

    @BindView(R.id.checkbox_superset)
    CheckBox supersetCB;

    @BindView(R.id.checkbox_dropset)
    CheckBox dropsetCB;

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
    PLObject.ExerciseProfile exerciseProfile;

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
        guard(setsPLObject);
        exerciseSet = new ExerciseSet(setsPLObject.getExerciseSet());
        exerciseProfile = selectedSetViewModel.getSelectedExercise();

        setOnBackPress(v, ()->closeKeyBoard());
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

    }

    private void initViews(View v) {
        RecyclerView.LayoutManager lmRep = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager lmRest = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        dataManager = ((BaseActivity) getActivity()).getDataManager();
        /*DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        mRepRecycler.addItemDecoration(itemDecor);
        mRestRecycler.addItemDecoration(itemDecor);*/

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


        initToolbar();

        initReps();

        initRest();

        initWeight();


    }

    private void initWeight() {
        currentWeightTV.setText(exerciseSet.getWeight() + "");
        //currentWeightTV.setOnEditorActionListener(new DoneOnEditorActionListener());

    }

    private void initRest() {
        String[] rests = exerciseSet.getRest().split(":");
        minED.setText(rests[0]);
        if (rests.length > 1) {
            secED.setText(rests[1]);
        }

        secondsWrapper.setOnClickListener(sectv -> {
            secED.callOnClick();
        });

        minutesWrapper.setOnClickListener(mintv -> {
            minED.callOnClick();
        });
    }

    private void initReps() {

        if (exerciseSet.getRep().split("-").length > 1) {
            repsToCB.setChecked(true);
        } else {
            reps2ED.setAlpha(0.5f);
            reps2ED.setEnabled(false);
            repsToCB.setChecked(false);
        }
        String[] reps = exerciseSet.getRep().split("-");
        reps1ED.setText(reps[0]);
        if (reps.length > 1) {

            reps2ED.setText(reps[1]);
        } else {
            reps2ED.setText("10");

        }

        repsToCB.setOnCheckedChangeListener(this);

    }

    private void initToolbar() {
        saveExitToolBar.instantiate()
                .setOptionalText("Edit Set");

        this.saveExitToolBar.setSaveButton((v) -> {
            closeKeyBoard();
            String reps1 = "0";
            if (!reps1ED.getText().toString().equals("")) {
                reps1 = reps1ED.getText().toString();
                if (checkZeroRepAtStart(reps1)) {
                    reps1 = reps1.substring(1);
                }
            }

            if (repsToCB.isChecked()) {
                String reps2 = reps2ED.getText().toString();
                if (checkZeroRepAtStart(reps2)) {
                    reps2 = reps2.substring(1);
                }
                reps1 = reps1 + "-" + reps2;
            }

            exerciseSet.setRep(reps1);


            String rest = "00";
            if (!minED.getText().toString().equals("")) {
                rest = minED.getText().toString();
            }
            rest += ":";
            if (secED.getText().toString().equals("")) {
                rest += "00";
            } else {
                rest += secED.getText().toString();
            }

            exerciseSet.setRest(rest);

            String weight = currentWeightTV.getText().toString();
            if (weight.equals("")) {
                weight = "0";
            }
            exerciseSet.setWeight(Double.parseDouble(weight));

            setsPLObject.setExerciseSet(exerciseSet);
            selectedSetViewModel.modifySetSelected(exerciseSet);
            applyForAll();
            getFragmentManager().popBackStack();
        });

        this.saveExitToolBar.setCancelButton(v -> exitRevealAnimation(r -> getFragmentManager().popBackStack()));
    }

    public boolean checkZeroRepAtStart(String rep) {
        if (rep.charAt(0) == '0' && rep.length() > 1) {
            return true;
        }
        return false;
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

    private void cancelAndToast(View v) {
        Toast.makeText(getContext(), "Data applied to all sets", Toast.LENGTH_SHORT).show();
        MyUtils.Interface.disableClick(v, 500);
    }

    /**
     * this is a function to provide "apply all" functionality where
     * on button press, the data is transfered to all the sets
     */
    private void applyForAll() {
        for (PLObject.SetsPLObject set : exerciseProfile.getSets()) {
            apploSettingsForSet(set);
            if (supersetCB.isChecked()) {
                for (PLObject.SetsPLObject superset : set.superSets) {
                    apploSettingsForSet(superset);
                }
            }

            if (dropsetCB.isChecked()) {
                for (PLObject.SetsPLObject intraSet : set.intraSets) {
                    apploSettingsForSet(intraSet);
                }
            }

        }
    }

    private void apploSettingsForSet(PLObject.SetsPLObject set) {
        if (repsCB.isChecked()) {
            set.getExerciseSet().setRep(exerciseSet.getRep());
        }

        if (restCB.isChecked()) {
            set.getExerciseSet().setRest(exerciseSet.getRest());

        }

        if (weightCB.isChecked()) {
            set.getExerciseSet().setWeight(exerciseSet.getWeight());
        }

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
            dataManager.getExerciseDataManager().insertData(TABLE_REPS, exerciseSet.getRep());
            repFlag = true;
        }
        if (listRest.size() == 0) {
            dataManager.getExerciseDataManager().insertData(TABLE_REST, exerciseSet.getRest());
            restFlag = true;
        }

        for (String rep : listRep) {
            if (rep.equals(exerciseSet.getRep())) {
                repFlag = true;
            }
        }
        for (String rest : listRest) {
            if (rest.equals(exerciseSet.getRest())) {
                restFlag = true;
            }
        }
        if (!repFlag) {
            insertData(TABLE_REPS, exerciseSet.getRep());
        }
        if (!restFlag) {
            insertData(TABLE_REST, exerciseSet.getRest());
        }
    }

    public void insertData(String tableName, String name) {
        dataManager.getExerciseDataManager().insertData(tableName, name);

    }

    @Override
    public void onClick(String object, String type) {
        if (type.equals("rep")) {
            exerciseSet.setRep(object);
            initReps();


        } else if (type.equals("rest")) {
            exerciseSet.setRest(object);
            initRest();

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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) {
            reps2ED.setEnabled(false);
            reps2ED.setAlpha(0.5f);
        } else {
            reps2ED.setEnabled(true);
            reps2ED.setAlpha(1f);
        }
    }
}
