package com.strongest.savingdata.MyViews.WorkoutView.Choose;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.MyViews.MySelector.ChooseSelectorAdapter;
import com.strongest.savingdata.MyViews.MySelector.MySelector;
import com.strongest.savingdata.MyViews.MySelector.MySelectorOnBeansHolderChange;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MUSCLE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REST;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_SETS;
import static com.strongest.savingdata.MyViews.MySelector.MySelector.SelectorTypes.Exercise;
import static com.strongest.savingdata.MyViews.MySelector.MySelector.SelectorTypes.Reps;
import static com.strongest.savingdata.MyViews.MySelector.MySelector.SelectorTypes.Rest;
import static com.strongest.savingdata.MyViews.MySelector.MySelector.SelectorTypes.Sets;


public class MandatoryChooseDialogFragment extends Fragment {


    private Bundle i;
    private Intent intent;
    private ArrayList<ExerciseSet> sets;
    private ExerciseSet set;
    private Muscle muscle;

    private DataManager dataManager;
    private ArrayList<ArrayList<Beans>> beans_array = new ArrayList<>();
    //  private static final int hundred = 100;
    private ChooseSelectorAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private MySelectorOnBeansHolderChange beansHolderChange;


    public MandatoryChooseDialogFragment() {

    }


    public static MandatoryChooseDialogFragment newInstance(ChooseData data) {

        MandatoryChooseDialogFragment fragment = new MandatoryChooseDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("beans_holder", data.getSets());
        args.putString(MUSCLE, data.getMuscle() != null ? data.getMuscle().getMuscle_name() : null);
        fragment.setArguments(args);
        fragment.setMuscle(data.getMuscle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sets = (ArrayList<ExerciseSet>) getArguments().getSerializable("beans_holder");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_mandatory_choose_dialog, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyJavaAnimator.fadeIn(view);
            }
        }, 300);
        initView(view);
    }

    private void initView(View v) {

        dataManager = new DataManager(getContext());
        intent = new Intent();
        //EventBus.getDefault().post(1);
        MySelector mySelector = (MySelector) v.findViewById(R.id.dialog_fragment_my_selector);

        MySelector.CheckedHolder[] checkerHolders;
        if (sets == null) {
            set = new ExerciseSet();
        } else {
            set = sets.get(0);
        }
        /*checkerHolders = new MySelector.CheckedHolder[]{
                new MySelector.CheckedHolder(set.getExercise()),
                new MySelector.CheckedHolder(set.getSets()),
                new MySelector.CheckedHolder(set.getRep()),
                new MySelector.CheckedHolder(set.getRest())
        };*/

       /* adapter = new ChooseSelectorAdapter(beansHolderChange, getFragmentManager(), getContext(), muscle, checkerHolders,
                Exercise,
                Sets,
                Reps,
                Rest
                *//*Method*//*);
        mySelector.setAdapter(adapter);*/


    }

    private void initBeans() {
        ArrayList<Beans> exerciseBean = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(muscle.getMuscle_name());
        beans_array.add(exerciseBean);
        ArrayList<Beans> repsBean = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(TABLE_REPS);
        beans_array.add(repsBean);
        ArrayList<Beans> setsBean = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(TABLE_SETS);
        beans_array.add(setsBean);
        /*ArrayList<Beans> methodBean = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(TABLE_METHODS);
        beans_array.add(methodBean);*/
        ArrayList<Beans> restBean = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(TABLE_REST);
        beans_array.add(restBean);

    }

    public ExerciseSet getMandatoryBeansHolder() {
 /*       initBeans();
        if (sets == null) {
            set = new ExerciseSet();
        }
        MySelector.CheckedHolder[] checkHolder = adapter.getCheckedHolders();

        if (checkHolder != null) {
            int i = 0;
            if (vaildate(checkHolder)) {

                if (checkHolder[i] != null) {
                    set.setExercise(beans_array.get(i).
                            get(checkHolder[i++].getPosition()));
                    //beansHolder.setLoaded(true);
                }
                if (checkHolder[i] != null)
                    set.setRep(beans_array.get(i).
                            get(checkHolder[i++].getPosition()));
                if (checkHolder[i] != null) {
                    set.setSets(beans_array.get(i).
                            get(checkHolder[i++].getPosition()));
                }
                if (checkHolder[i] != null) {
                    set.setRest(beans_array.get(i).
                            get(checkHolder[i].getPosition()));
                }

            }
        }
        return set;*/
 return null;
    }

    private boolean vaildate(MySelector.CheckedHolder[] checkHolder) {
        /*if (checkHolder[recycler_view_exercises_left_margin.ordinal()] != null || beansHolder.getExercise() != null) {
            if (checkHolder[Reps.ordinal()] != null || beansHolder.getRep() != null) {
                return true;
            }
        }
        return false;*/
        for (int j = 0; j < checkHolder.length; j++) {
            if (checkHolder[j] == null) {
                return false;
            }
        }
        return true;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {

        }
    }

    public void setMuscle(Muscle muscle) {
        this.muscle = muscle;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setBeansHolderChange(MySelectorOnBeansHolderChange beansHolderChange) {
        this.beansHolderChange = beansHolderChange;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
