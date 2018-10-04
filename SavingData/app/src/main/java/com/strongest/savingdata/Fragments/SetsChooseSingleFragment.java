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
    TextView reps1TV;

    @BindView(R.id.choose_sets_reps_cb)
    CheckBox repsToCB;

    @BindView(R.id.choose_sets_reps_range_et)
    TextView reps2TV;

    @BindView(R.id.choose_sets_rest_sec_tv)
    TextView secTV;

    @BindView(R.id.choose_sets_rest_min_tv)
    TextView minTV;

    @BindView(R.id.fragment_sets_choose_recycler_repetitions)
    RecyclerView mRepRecycler;

    @BindView(R.id.fragment_sets_choose_recycler_rest)
    RecyclerView mRestRecycler;


 /*   @BindView(R.id.choose_sets_customweight)
    CheckBox customWeightCB;
*/

    @BindView(R.id.choose_sets_weight_tv)
    TextView currentWeightTV;
    @BindView(R.id.choose_sets_weight_layout)
    View weightLayout;


    @BindView(R.id.set_edit_saveExitToolbar)
    SaveExitToolBar saveExitToolBar;

    @BindView(R.id.apply_settings_for_allsets_btn)
    Button applyAll;

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
        setUpMainViewForCircularAnimation(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

    }

    private void initViews(View v) {
        makeRevealAnimation(300,null);
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

        if(setsPLObject.getType() != WorkoutLayoutTypes.SetsPLObject){
            applyAll.setVisibility(View.GONE);
        }else{
            applyAll.setOnClickListener(view-> applyForAll(view));
        }

    }

    private void initWeight() {
        currentWeightTV.setText(exerciseSet.getWeight() + "");
        currentWeightTV.setOnClickListener(v -> {
            MaterialDialogHandler.get()
                    .showInputDialog(getContext(),
                            InputType.TYPE_NUMBER_FLAG_SIGNED,
                            currentWeightTV.getText().toString(),
                            "Change Weight",
                            ((dialog, input) -> {
                                currentWeightTV.setText(input.toString());

                            }));
        });

    }

    private void initRest() {
        String[] rests = exerciseSet.getRest().split(":");
        minTV.setText(rests[0]);
        if (rests.length > 1) {
            secTV.setText(rests[1]);
        }

        secTV.setOnClickListener(sectv -> {
            MaterialDialogHandler.get()
                    .showInputDialog(getContext(),
                            InputType.TYPE_CLASS_NUMBER,
                            secTV.getText().toString(),
                            "Change minutes",
                            ((dialog, input) -> {
                                secTV.setText(input.toString());

                            }));
        });

        minTV.setOnClickListener(mintv -> {
            MaterialDialogHandler.get()
                    .showInputDialog(getContext(),
                            InputType.TYPE_CLASS_NUMBER,
                            minTV.getText().toString(),
                            "Change seconds",
                            ((dialog, input) -> {
                                minTV.setText(input.toString());

                            }));
        });
    }

    private void initReps() {

        if (exerciseSet.getRep().split("-").length > 1) {
            repsToCB.setChecked(true);
        } else {
            reps2TV.setAlpha(0.5f);
            reps2TV.setEnabled(false);
        }
        String[] reps = exerciseSet.getRep().split("-");
        reps1TV.setText(reps[0]);
        if (reps.length > 1) {

            reps2TV.setText(reps[1]);
        } else {
            reps2TV.setText("10");

        }

        reps1TV.setOnClickListener(repClickedView -> {
            MaterialDialogHandler.get()
                    .showInputDialog(getContext(),
                            InputType.TYPE_CLASS_NUMBER,
                            reps1TV.getText().toString(),
                            getString(R.string.change_repetitions_title),
                            ((dialog, input) -> {
                                reps1TV.setText(input.toString());

                            }));
        });

        repsToCB.setOnCheckedChangeListener(this);

        reps2TV.setOnClickListener(repClickedView -> {
            MaterialDialogHandler.get()
                    .showInputDialog(getContext(),
                            InputType.TYPE_CLASS_NUMBER,
                            reps1TV.getText().toString(),
                            getString(R.string.change_repetitions_title),
                            ((dialog, input) -> {
                                reps2TV.setText(input.toString());
                            }));
        });
    }

    private void initToolbar() {
        saveExitToolBar.instantiate()
        .setOptionalText("Edit Set");

        saveExitToolBar.setSaveButton((v) -> {

            String reps = reps1TV.getText().toString();
            if (repsToCB.isChecked()) {
                reps += "-" + reps2TV.getText().toString();
            }

            exerciseSet.setRep(reps);


            String rest = minTV.getText().toString() + ":";
            if(secTV.getText().toString().equals("")){
                rest +="00";
            }

            exerciseSet.setRest(rest);

            exerciseSet.setWeight(Double.parseDouble(currentWeightTV.getText().toString()));

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

    private void cancelAndToast(View v) {
        Toast.makeText(getContext(), "Data applied to all sets", Toast.LENGTH_SHORT).show();
        MyUtils.Interface.disableClick(v, 500);
    }

    /**
     * this is a function to provide "apply all" functionality where
     * on button press, the data is transfered to all the sets
     */
    private void applyForAll(View v) {
        for (PLObject.SetsPLObject set : exerciseProfile.getSets()){
            set.setExerciseSet(exerciseSet);
        }
        cancelAndToast(v);
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
            reps2TV.setEnabled(false);
            reps2TV.setAlpha(0.5f);
        } else {
            reps2TV.setEnabled(true);
            reps2TV.setAlpha(1f);
        }
    }
}
