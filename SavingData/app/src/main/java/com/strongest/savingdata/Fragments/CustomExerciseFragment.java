package com.strongest.savingdata.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.GridViewMusclesAdapter;
import com.strongest.savingdata.Adapters.OnGridViewMuscleAdapterClickListener;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.MyViews.WorkoutView.OnProgramToolsActionListener;
import com.strongest.savingdata.R;
import com.strongest.savingdata.Utils.MyUtils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomExerciseFragment extends Fragment implements OnGridViewMuscleAdapterClickListener {

    public ExpandableLayout mExpandable;
    private GridView mGridView;
    private GridViewMusclesAdapter mGridViewAdapter;
    private RecyclerView recyclerView;
    private DataManager dataManager;
    private Muscle choosedMuscle;
    private Button saveBtn;
    private String choosedText;

    @BindView(R.id.custom_exercise_name)
     EditText editText;

    @BindView(R.id.custom_exercise_icon_iv)
    ImageView iconIV;

    @BindView(R.id.custom_exercise_tv)
    TextView muscleTV;

    @BindView(R.id.custom_exercise_toolbar)
    SaveExitToolBar toolBar;

    public static ProgramSettingsFragment getInstance(OnProgramToolsActionListener onProgramToolsActionListener) {
        ProgramSettingsFragment f = new ProgramSettingsFragment();
        f.setOnProgramToolsActionListener(onProgramToolsActionListener);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_custom_exercise, container, false);
        ButterKnife.bind(this, v);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    getActivity().getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View v) {
        toolBar.instantiate()
                .showBack(true)
                .setOptionalText("Custom Exercise")
                .setSaveButton(view->{
                    InputMethodManager inputManager = (InputMethodManager) editText
                            .getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    IBinder binder = editText.getWindowToken();
                    inputManager.hideSoftInputFromWindow(binder,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    getFragmentManager().popBackStack();
                });
        dataManager = ((HomeActivity) getActivity()).dataManager;

        mExpandable = (ExpandableLayout) v.findViewById(R.id.expandable);
        mGridView = (GridView) v.findViewById(R.id.fragment_choose_exercise_gridview);
        int height = ((HomeActivity)getActivity()).getScreenHeight();

        mGridViewAdapter = new GridViewMusclesAdapter(height,getContext(), dataManager, this);
        mGridView.setAdapter(mGridViewAdapter);

        v.findViewById(R.id.choose_change_Tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandable.toggle();
            }
        });
        saveBtn = (Button) v.findViewById(R.id.custom_exercise_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosedText = editText.getText().toString();
                if (validate()) {
                    Beans exercise = new Beans();
                    exercise.name = choosedText;
                    exercise.muscle = choosedMuscle;
                    exercise.primaryMuscle = choosedMuscle.getMuscle_display();
                    exercise.setDefault_int(1); // 1 = exercise is not deafult(is not a part of my database)
                    ArrayList<Beans> userExercises = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(DBExercisesHelper.TABLE_EXERCISES_CUSTOM);
                    if (validateNameInList(userExercises)) {
                        dataManager.getExerciseDataManager().insertData(DBExercisesHelper.TABLE_EXERCISES_CUSTOM, exercise);
                        Toast.makeText(getContext(), "Exercise Saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Such Exercise exists!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                }

                MyUtils.Interface.disableClick(saveBtn, 1000);
            }
        });
    }

    private boolean validateNameInList(ArrayList<Beans> list) {
        for (Beans b : list) {
            if (b.getName().equals(choosedText)) {
                return false;
            }
        }
        return true;
    }

    private boolean validate() {
        if (choosedText == null || choosedText.equals("")) {
            return false;
        }
        if (choosedMuscle == null) {
            return false;
        }
        return true;
    }

    @Override
    public void onMuscleChange(GridViewMusclesAdapter.MusclesContentHolder mch) {
        muscleTV.setText(mch.text);
        iconIV.setImageResource(mch.icon);
        choosedMuscle = mch.m;
        mExpandable.collapse();
    }

    @Override
    public void collapseExpandableLayout() {

    }
}
