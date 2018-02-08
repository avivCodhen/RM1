package com.strongest.savingdata.createProgramFragments.CreateProgram;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;
import com.strongest.savingdata.Activities.CreateWorkoutActivity;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Activities.OnCreateProgramListener;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.R;
import com.strongest.savingdata.tabFragments.WorkoutFragment;


public class GeneratorFragment2 extends BaseCreateProgramFragment implements View.OnClickListener {

    private SwipeSelector exerciseSelector;
    private SwipeSelector complexitySelector;
    private SwipeSelector volumeSelector;
    private SwipeSelector routineSelector;
    private DataManager dm;
    private EditText et;
    private OnCreateProgramListener onCreateProgramListener;

    public static GeneratorFragment2 getInstance(OnCreateProgramListener onCreateProgramListener) {
        GeneratorFragment2 f = new GeneratorFragment2();
        f.setOnCreateProgramListener(onCreateProgramListener);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_generator_fragment2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
        dm = ((HomeActivity) getActivity()).getDataManager();
        exerciseSelector = (SwipeSelector) v.findViewById(R.id.exercise_difficulty_selector);
        volumeSelector = (SwipeSelector) v.findViewById(R.id.workout_volume_selector);
        complexitySelector = (SwipeSelector) v.findViewById(R.id.complexity_selector);
        routineSelector = (SwipeSelector) v.findViewById(R.id.exercise_routine_selector);
        et = (EditText) v.findViewById(R.id.template_program_edit_text);

        exerciseSelector.setItems(
                new SwipeItem(0, "Beginner", "The Generator will pick exercise specificly for Beginners."),
                new SwipeItem(1, "Balanced(recommended)", "The Generator will supply a wide range of exercise difficulties."),
                new SwipeItem(2, "Advanced", "The Generator will favor advanced exercises.")
        );
        volumeSelector.setItems(
                new SwipeItem(0, "Low", "The Generator will focus on low reps workouts."),
                new SwipeItem(1, "Balanced(recommended)", "The Generator will provide a balanced volume."),
                new SwipeItem(2, "High", "The Generator will focus on high reps workouts.")

        );
        complexitySelector.setItems(
                new SwipeItem(0, "Simple", "The Generator will not choose complex methods."),
                new SwipeItem(1, "Balanced(recommended)", "The Generator will provide a balanced workout complexities."),
                new SwipeItem(2, "Complex", "The generator will make the workouts complex.")

        );

        routineSelector.setItems(
                new SwipeItem(0, "FBW", ""),
                new SwipeItem(1, "AB", ""),
                new SwipeItem(2, "ABC", ""),
                new SwipeItem(3, "ABCDE", "")
        );
        v.findViewById(R.id.fragment_generator_create_button).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        getPrefsEditor().putBoolean(MODE_CUSTOM, false).commit();
        dm.getPrefsEditor().putInt(LOAD_EXERCISE_LEVEL, (int) exerciseSelector.getSelectedItem().value)
                .putInt(LOAD_INTENSITY, (int) volumeSelector.getSelectedItem().value)
                .putInt(LOAD_COMPLEXITY, (int) complexitySelector.getSelectedItem().value)
                .putInt(ROUTINE, (int) routineSelector.getSelectedItem().value)
                .putBoolean(MODE_GENERATED_PROGRAM, true)
                .commit();


        //String s = dm.getProgramDataManager().readByTable(DBProgramHelper.TABLE_TEMPLATES);
        // dm.getProgramDataManager().createUserTemplate();
        Bundle bundle = new Bundle();
        bundle.putBoolean(MODE_GENERATED_PROGRAM, true);
        String routine = "";
        switch ((int) routineSelector.getSelectedItem().value) {
            case 0:
                routine = "fbw";
                break;
            case 1:
                routine = "ab";
                break;
            case 2:
                routine = "abc";
                break;
            case 3:
                routine = "abcde";
                break;

        }
        bundle.putString(ROUTINE, routine );
        onCreateProgramListener.createProgramUI(CreateFragment.newInstance(onCreateProgramListener, bundle));
//        onCreateProgramListener.createProgramUI(new WorkoutFragment());

        // switchFragment(new CreateFragment(), bundle);
    }

    public void setOnCreateProgramListener(OnCreateProgramListener onCreateProgramListener) {
        this.onCreateProgramListener = onCreateProgramListener;
    }
}
