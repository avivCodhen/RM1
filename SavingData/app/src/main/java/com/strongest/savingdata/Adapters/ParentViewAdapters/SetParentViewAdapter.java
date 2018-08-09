package com.strongest.savingdata.Adapters.ParentViewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Controllers.UISetsClickHandler;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.R;
import com.strongest.savingdata.ViewHolders.IntraSetViewHolder;

import java.util.ArrayList;

public class SetParentViewAdapter extends ParentView.Adapter<IntraSetViewHolder> {

    private PLObject.SetsPLObject setsPLObject;
    private UISetsClickHandler uiSetsClickHandler;
    private ArrayList<PLObject.SetsPLObject> list;
    private MyExpandableAdapter.MyExpandableViewHolder parentViewHolder;

    public SetParentViewAdapter(PLObject.SetsPLObject setsPLObject, UISetsClickHandler uiSetsClickHandler,
                                MyExpandableAdapter.MyExpandableViewHolder parentViewHolder){
        this.setsPLObject = setsPLObject;
        this.uiSetsClickHandler = uiSetsClickHandler;
        this.parentViewHolder = parentViewHolder;
        list = new ArrayList<>();
        list.addAll(setsPLObject.superSets);
        list.addAll(setsPLObject.intraSets);
    }

    @Override
    public IntraSetViewHolder onCreateChildViewHolder(ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View v = inflater.inflate(R.layout.recycler_view_intra_set,container, false);
         return new IntraSetViewHolder(v);
    }

    @Override
    public int getCount() {
        return setsPLObject.intraSets.size() + setsPLObject.superSets.size();
    }

    @Override
    public void onBindViewHolder(IntraSetViewHolder intraSetViewHolder, int position) {
        PLObject.SetsPLObject set = list.get(position);
        ExerciseSet exerciseSet = set.getExerciseSet();
        if (list.get(position).type == WorkoutLayoutTypes.SuperSetIntraSet){
                intraSetViewHolder.supersetTv.setVisibility(View.VISIBLE);
                PLObject.ExerciseProfile superset = setsPLObject.parent.getExerciseProfiles().get(position);
                intraSetViewHolder.deleteIV.setVisibility(View.INVISIBLE);
                if(superset.getExercise()!= null){

                    intraSetViewHolder.supersetTv.setText("Superset of "+superset.getExercise().getName());
                }else{
                    intraSetViewHolder.supersetTv.setText("Superset");
                }

            }else{
            intraSetViewHolder.intraSetTag.setImageResource(R.drawable.child_green);
                intraSetViewHolder.deleteIV.setVisibility(View.VISIBLE);
                intraSetViewHolder.deleteIV.setOnClickListener((delete)->{
                    uiSetsClickHandler.onRemoveIntraSet(setsPLObject, setsPLObject.intraSets.indexOf(set));
                });
            }

        if(position != (list.size()-1)){
            intraSetViewHolder.rest.setText(exerciseSet.getRest());
        }else{
            intraSetViewHolder.rest.setText("");
        }

        intraSetViewHolder.reps.setText(exerciseSet.getRep());
        intraSetViewHolder.weight.setText(exerciseSet.getWeight() + "kg");

        intraSetViewHolder.mainLayout.setOnClickListener((itemView)->{
            uiSetsClickHandler.onSetsClick(parentViewHolder, setsPLObject);
        });

        intraSetViewHolder.mainLayout.setOnLongClickListener((longClicked)->{
            uiSetsClickHandler.onSetsLongClick(setsPLObject,intraSetViewHolder);
            return true;
        });

    }

}