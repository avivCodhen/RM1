package com.strongest.savingdata.Adapters.ParentViewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
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

        if(!exerciseSet.hasSomething()){
            intraSetViewHolder.editContainer.setVisibility(View.VISIBLE);
            intraSetViewHolder.editContainer.setOnClickListener((clicked)->{

            });
        }else{
            intraSetViewHolder.editContainer.setVisibility(View.GONE);
        }
        if (list.get(position).type == WorkoutLayoutTypes.SuperSetIntraSet){
                intraSetViewHolder.supersetTv.setVisibility(View.VISIBLE);
                //PLObject.ExerciseProfile superset = setsPLObject.parent.exerciseProfiles.get(position);
                intraSetViewHolder.deleteIV.setVisibility(View.INVISIBLE);

            intraSetViewHolder.supersetTv.setText("Superset of Exercise A");

/*
            if(superset.getExercise()!= null){

                }else{
                    intraSetViewHolder.supersetTv.setText("Superset");
                }
*/

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

        intraSetViewHolder.card.setOnClickListener((itemView)->{
            uiSetsClickHandler.onSetsClick(parentViewHolder, setsPLObject);
        });

        intraSetViewHolder.card.setOnLongClickListener((longClicked)->{
            uiSetsClickHandler.onSetsLongClick(setsPLObject,parentViewHolder);
            return true;
        });
    }



}