package com.strongest.savingdata.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.Adapters.SingleChoiceAdapter;
import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.NumberChooseManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.RangeNumberChooseView;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.ChooseDialogFragment;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

import java.util.ArrayList;

/**
 * Created by Cohen on 3/15/2018.
 */

public class SetsChooseSingleFragment extends BaseCreateProgramFragment {

    private RangeNumberChooseView mRangeNumberChoose;
    private RecyclerView mRepRecycler, mRestRecycler;
    private SingleChoiceAdapter mRepAdapter, mRestAdapter;

    private PLObject.ExerciseProfile exerciseProfile;
    private PLObject plObject;
    private PLObject.SetsPLObject setsPLObject;
    private PLObject.IntraSetPLObject intraSetPLObject;
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
        if (getArguments() != null) {
            plObject = (PLObject) getArguments().getSerializable(ChooseDialogFragment.PLOBJECT);
            if(plObject instanceof PLObject.SetsPLObject){
                setsPLObject = (PLObject.SetsPLObject) plObject;
            }else{
                intraSetPLObject = (PLObject.IntraSetPLObject) plObject;
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
        mRangeNumberChoose = (RangeNumberChooseView) v.findViewById(R.id.fragment_sets_choose_rangenumberview);
        NumberChooseManager numberChooseManager = new NumberChooseManager();
        mRangeNumberChoose.setUpWithNumberChooseManager(numberChooseManager);
        RecyclerView.LayoutManager lmRep = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager lmRest = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mRepRecycler = (RecyclerView) v.findViewById(R.id.fragment_sets_choose_recycler_repetitions);
        mRestRecycler = (RecyclerView) v.findViewById(R.id.fragment_sets_choose_recycler_rest);

        ArrayList<String> listRep = new ArrayList<>();
        listRep.add("1-5");
        listRep.add("5");
        listRep.add("20");
        listRep.add("10");
        listRep.add("6-10");

        ArrayList<String> listRest = new ArrayList<>();
        listRest.add("00:35");
        listRest.add("00:35");
        listRest.add("00:40");
        listRest.add("00:15");
        listRest.add("01:35");
        listRest.add("02:30");
        listRest.add("00:55");
        listRest.add("01:00");
        listRest.add("03:00");
        mRepAdapter = new SingleChoiceAdapter(getContext(), listRep);
        mRestAdapter = new SingleChoiceAdapter(getContext(), listRest);
        mRestRecycler.setLayoutManager(lmRep);
        mRepRecycler.setLayoutManager(lmRest);
        mRepRecycler.setAdapter(mRepAdapter);
        mRestRecycler.setAdapter(mRestAdapter);
    }
}
