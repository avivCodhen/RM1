package com.strongest.savingdata.tabFragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nightonke.boommenu.BoomMenuButton;
import com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;
import com.strongest.savingdata.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

@Deprecated
public class WorkoutsTabFragment extends BaseCreateProgramFragment implements View.OnClickListener {
    private static final String ROUTINE = "routine";
    private static final String POS = "position";
    private static final String WORKOUT = "workout";
    private static final String PROGRAM = "program";
    private static final String TO_ANIMATE = "animate";


    private OnFragmentInteractionListener mListener;
    private int routineInt;
    private LinearLayout layout;
    private int position;
    private boolean edit;
    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private TextView t;
    private BoomMenuButton boom;
    private DataManager dataManager;
    private ArrayList<PLObjects> array;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setSelected(true);
            if (edit) {
            } else {
            }

        }
    };
    private LayoutManager layoutManager;

    public static WorkoutsTabFragment newInstance(int pos, boolean editMode) {
        WorkoutsTabFragment fragment = new WorkoutsTabFragment();
        Bundle args = new Bundle();
        args.putInt(POS, pos);
        args.putBoolean("Test", editMode);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            routineInt = getArguments().getInt(ROUTINE, -1);
            position = getArguments().getInt(POS, -1);
           // edit = getArguments().getBoolean("Test");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_workouts_tab, container, false);
    }

    /* @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         inflater.inflate(R.menu.next_button, menu);
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch(item.getItemId()){
             case R.id.edit_menu:
                 Log.d("aviv", "success");
                 return true;
         }
         return false;
     }*/
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
        dataManager = new DataManager(getContext());
        layoutManager = new LayoutManager(getContext(), dataManager);
        layoutManager.readLayoutFromDataBase(0);
        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_workouts_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        initAdapter();
        EventBus.getDefault().register(this);

    }

    @Subscribe
    public void onEvent(BooleanEventHolder event){
        edit = event.isShowStats();
        adapter.setShowStats(edit);
        adapter.notifyDataSetChanged();
    }

    public void initAdapter() {
       // ArrayList<Integer> selectedPositions = fetchSelectedPos();
      /*  array = layoutManager.getSplitRecyclerWorkouts().get(position);
        adapter = new WorkoutAdapter(
                array,
                array,
                getContext(),
                null,
                null,
                edit);

        recyclerView.setAdapter(adapter);*/


    }

    /*private ArrayList<Integer> fetchSelectedPos() {
        ArrayList<Integer> list = new ArrayList<>();
        int j = 0;
        while (getPrefs().getInt(WORKOUT + position + j, -1) != -1) {
            list.add(getPrefs().getInt(WORKOUT + position + j, -1));
            j++;
        }
        return list;
    }

    private void saveSelectedPos() {
        ArrayList<Integer> list = adapter.getSelectedPoses();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                getPrefsEditor().putInt(WORKOUT + position + i, list.get(i)).commit();
            }
        }
    }*/

   /* private void initSpruce() {
        Animator spruceAnimator = new Spruce.SpruceBuilder(recyclerView)
                .sortWith(new DefaultSort(10))
                .animateWith(DefaultAnimations.shrinkAnimator(recyclerView, 200),
                        ObjectAnimator.ofFloat(recyclerView, "translationX", -recyclerView.getWidth(), 0f).setDuration(200))
                .start();
    }
*/

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
        dataManager.closeDataBases();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        //saveSelectedPos();
        Log.d(TAG, "onStop: ");
        adapter.setSelectedPoses(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
      //  ArrayList<Integer> selectedPositions = fetchSelectedPos();
        //
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach: ");
    }
}
