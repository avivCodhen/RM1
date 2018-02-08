package com.strongest.savingdata.MyViews.WorkoutView.Choose;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.BeansHolder;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Database.Managers.ExercisesDataManager;
import com.strongest.savingdata.MyViews.MySelector.ChooseSelectorAdapter;
import com.strongest.savingdata.MyViews.MySelector.MySelector;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MUSCLE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_ALL;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_METHODS;
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
    private ArrayList<BeansHolder> beansHolders;
    private BeansHolder beansHolder;
    private int muscle;

    private DataManager dataManager;
    private ArrayList<ArrayList<Beans>> beans_array = new ArrayList<>();
    //  private static final int hundred = 100;
    private ChooseSelectorAdapter adapter;

    private OnFragmentInteractionListener mListener;


    public MandatoryChooseDialogFragment() {

    }


    public static MandatoryChooseDialogFragment newInstance(ChooseData data) {

        MandatoryChooseDialogFragment fragment = new MandatoryChooseDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("beans_holder", data.getBeansHolder());
        args.putString(MUSCLE, data.getMuscle().getMuscle_name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            beansHolders = (ArrayList<BeansHolder>) getArguments().getSerializable("beans_holder");
            muscle = getArguments().getInt(MUSCLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_mandatory_choose_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
        dataManager = new DataManager(getContext());
        intent = new Intent();

        MySelector mySelector = (MySelector) v.findViewById(R.id.dialog_fragment_my_selector);

        MySelector.CheckedHolder[] checkerHolders;
        if (beansHolders.get(0) == null) {
            checkerHolders = null;
        } else {
            beansHolder = beansHolders.get(0);
            checkerHolders = new MySelector.CheckedHolder[]{
                    new MySelector.CheckedHolder(beansHolder.getExercise().getId(),
                            beansHolder.getExercise().getName()),
                    new MySelector.CheckedHolder(beansHolder.getRep().getId(),
                            beansHolder.getRep().getName()),
                    new MySelector.CheckedHolder(beansHolder.getSets().getId(),
                            beansHolder.getSets().getName()),
                    new MySelector.CheckedHolder(beansHolder.getRest().getId(),
                            beansHolder.getRest().getName())
            };
        }
        adapter = new ChooseSelectorAdapter(getFragmentManager(),getContext(), muscle, checkerHolders,
                Exercise,
                Reps,
                Sets,
                Rest
                /*Method*/);
        mySelector.setAdapter(adapter);
        initBeans();


    }

    private void initBeans() {
        ArrayList<Beans> exerciseBean = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByString(TABLE_EXERCISES_ALL, DBExercisesHelper.MUSCLE, muscle);
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

    public BeansHolder getMandatoryBeansHolder() {


        if (beansHolder == null) {
            beansHolder = new BeansHolder();
        }
        MySelector.CheckedHolder[] checkHolder = adapter.getCheckedHolders();

        if (checkHolder != null) {
            int i = 0;
            if (vaildate(checkHolder)) {

                if (checkHolder[i] != null) {
                    beansHolder.setExercise(beans_array.get(i).
                            get(checkHolder[i++].getPosition()));
                    //beansHolder.setLoaded(true);
                }
                if (checkHolder[i] != null)
                    beansHolder.setRep(beans_array.get(i).
                            get(checkHolder[i++].getPosition()));
                if (checkHolder[i] != null) {
                    beansHolder.setSets(beans_array.get(i).
                            get(checkHolder[i++].getPosition()));
                }
                if (checkHolder[i] != null) {
                    beansHolder.setRest(beans_array.get(i).
                            get(checkHolder[i].getPosition()));
                }

            }
        }
        return beansHolder;
    }

    private boolean vaildate(MySelector.CheckedHolder[] checkHolder) {
        /*if (checkHolder[recycler_view_exercises.ordinal()] != null || beansHolder.getExercise() != null) {
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
