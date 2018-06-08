package com.strongest.savingdata.MyViews.WorkoutView.Choose;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.CreateBeansHolderView;
import com.strongest.savingdata.MyViews.MySelector.ChooseSelectorAdapter;
import com.strongest.savingdata.MyViews.MySelector.MySelector;
import com.strongest.savingdata.R;
import com.strongest.savingdata.Unused.BaseDialogFragment;

import java.util.ArrayList;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MUSCLE;

public class OptionalChooseDialogFragment extends BaseDialogFragment {


    private MySelector mMySelector;
    private int mMuscle;
   // private ArrayList<Sets> mSets;
    private ExerciseSet mExerciseSet;
    private ChooseSelectorAdapter adapter;
    //private double[] mValueArray = new double[3901];

    private OnFragmentInteractionListener mListener;
    private DataManager dataManager;

    private CreateBeansHolderView createBeansHolderView;
    public OptionalChooseDialogFragment() {
        // Required empty public constructor
    }


    public static OptionalChooseDialogFragment newInstance(ChooseData data) {
        OptionalChooseDialogFragment fragment = new OptionalChooseDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("beans_holder", data.getSets());
        args.putString(MUSCLE, "chest");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          //  mSets = (ArrayList<Sets>) getArguments().getSerializable("beans_holder");
            //mMuscle = getArguments().getInt(MUSCLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_optional_choose_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
        createBeansHolderView = (CreateBeansHolderView) v.findViewById(R.id.fragment_optional_createbeanview);
        createBeansHolderView.instantiate();
      /*  // mCircularSeekBar = (CircularSeekBar) v.findViewById(R.id.fragment_choose_dialog_circularseekbar);

        mMySelector = (MySelector) v.findViewById(R.id.fragment_optional_choose_myselector);
        MySelector.CheckedHolder[] checkedHolder = null;

       *//* if(mBeansHolder != null && mBeansHolder.isHasmethod()){
            checkedHolder = new MySelector.CheckedHolder[]{
                    new MySelector.CheckedHolder(mBeansHolder.getMethod().getBean())
            };

        }else{
            checkedHolder = null;
        }*//*
        adapter = new ChooseSelectorAdapter(getFragmentManager(), getContext(), mMuscle, checkedHolder,
                Method, Weight);
        if (mBeansHolder != null) {
            adapter.setWeightValue(mBeansHolder.getWeight());
        }
        mMySelector.setAdapter(adapter);
        dataManager = new DataManager(getContext());
*/
    }

    public ArrayList<ExerciseSet> getOptionalBeansHolder() {
        ArrayList<ExerciseSet> list = new ArrayList<>();

        // ArrayList<Beans> methodBean = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(TABLE_METHODS);

       /* if(mBeansHolder == null) {
            mBeansHolder = new BeansHolder();
        }
            MySelector.CheckedHolder[] checkedHolder = adapter.getCheckedHolders();
            if (checkedHolder[0] != null) {
                mBeansHolder.setMethod(methodBean.get(checkedHolder[0].getPosition()));
            }
            mBeansHolder.setWeight(adapter.getWeightKeyboardValue());*/

        return null;
    }

  /*  private void generateValueArray() {
        int arrayIndex = 0;
        double arrayValue = 0;

        while (arrayValue < 10) {
            mValueArray[arrayIndex] = (Math.round(arrayValue * 100.0) / 100.0);
            arrayValue += 0.01;
            arrayIndex++;
        }

        mValueArray[arrayIndex] = (Math.round(arrayValue * 10.0) / 10.0);

        while (arrayValue < 300) {
            mValueArray[arrayIndex] = (Math.round(arrayValue * 10.0) / 10.0);
            arrayValue += 0.1;
            arrayIndex++;
        }
    }*/

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
