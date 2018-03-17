package com.strongest.savingdata.tabFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Cohen on 4/22/2017.
 */

public class ManagerFragment extends BaseCreateProgramFragment {

    private RecyclerView recyclerView;

    private ArrayList<Muscle> muscles;
    private ArrayList<PLObjects> layout;
    private ArrayList<Muscle.MuscleStatObject> muscleStatObjects;
    private DataManager dataManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_manager, container, false);
        View v = inflater.inflate(R.layout.fragment_manager, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
    recyclerView = (RecyclerView) v.findViewById(R.id.manager_fragment_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        layout = ((HomeActivity)getActivity()).getProgrammer().getLayoutManager().getLayout();
        dataManager = ((HomeActivity)getActivity()).getDataManager();
        muscles = Muscle.getAllMuscles(dataManager.getMuscleDataManager());
        muscleStatObjects = getMuscleStatObjects();

        countMuscleStatsObject();
        sortList();
        ManagerFragmentRecyclerAdapter adapter = new ManagerFragmentRecyclerAdapter(muscleStatObjects);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void sortList(){
        for (int i = 0; i <muscleStatObjects.size() ; i++) {
            for (int j = 0; j < muscleStatObjects.size(); j++) {
                if(muscleStatObjects.get(i).getTimes() > muscleStatObjects.get(j).getTimes() ){
                    Collections.swap(muscleStatObjects, i, j);
                }
            }
        }
    }

    private void countMuscleStatsObject() {
        /*for (PLObjects plobj : layout){
            if(plobj.getType() == WorkoutLayoutTypes.ExerciseView) {
                if (((PLObjects.ExerciseProfile) plobj).getmSets() != null) {
                    if (((PLObjects.ExerciseProfile) plobj).getmSets().getExercise() != null)
                        countMuscle(((PLObjects.ExerciseProfile) plobj).getmSets().getExercise().getMuscles());
                }
            }
        }*/
    }

    public ArrayList<Muscle.MuscleStatObject> getMuscleStatObjects(){
        ArrayList<Muscle.MuscleStatObject> arr = new ArrayList<>();
        for (Muscle m : muscles){
            arr.add(new Muscle.MuscleStatObject(m));
        }
        return arr;
    }

    public void countMuscle(String muscles){
        for (Muscle.MuscleStatObject msobj : muscleStatObjects ){
            if(muscles.contains(msobj.getM().getMuscle_name())){
                msobj.incrementTimes();
            }
        }
    }

    private class ManagerFragmentRecyclerAdapter extends RecyclerView.Adapter<ManagerFragmentRecyclerAdapter.ViewHolder>{

        private ArrayList<Muscle.MuscleStatObject> muscleStatObjects;

        public ManagerFragmentRecyclerAdapter(ArrayList<Muscle.MuscleStatObject >muscleStatObjects){

            this.muscleStatObjects = muscleStatObjects;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.recycler_view_muscles_stats, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Muscle.MuscleStatObject msobj = muscleStatObjects.get(position);
            if(msobj.getMui() != null){
                holder.icon.setImageResource( msobj.getMui().getImage());

            }
            holder.title.setText(msobj.getM().getMuscle_display());
            holder.progressBar.setMax(muscleStatObjects.size()/2);
            holder.progressBar.setProgress(msobj.getTimes());

        }

        @Override
        public int getItemCount() {
            return muscleStatObjects.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private ImageView icon;
            private TextView title;
            private ProgressBar progressBar;
            public ViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.reycler_view_muscle_icon_iv);
                title = (TextView) itemView.findViewById(R.id.reycler_view_muscle_name_tv);
                progressBar = (ProgressBar) itemView.findViewById(R.id.recycler_view_muscle_pb);
            }
        }
    }
}
