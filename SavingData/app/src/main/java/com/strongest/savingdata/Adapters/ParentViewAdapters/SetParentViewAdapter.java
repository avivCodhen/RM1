package com.strongest.savingdata.Adapters.ParentViewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Controllers.UISetsClickHandler;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.R;
import com.strongest.savingdata.ViewHolders.IntraSetViewHolder;

import java.util.ArrayList;

public class SetParentViewAdapter extends ParentView.Adapter<IntraSetViewHolder> {

    private PLObject.SetsPLObject setsPLObject;
    private UISetsClickHandler uiSetsClickHandler;
    private ArrayList<PLObject.SetsPLObject> list;
    private MyExpandableAdapter.MyExpandableViewHolder parentViewHolder;
    private ArrayList<PLObject.ExerciseProfile> supersets;
    private LongClickMenuView longClickMenuView;

    public SetParentViewAdapter(ArrayList<PLObject.ExerciseProfile> supersets, PLObject.SetsPLObject setsPLObject, UISetsClickHandler uiSetsClickHandler,
                                MyExpandableAdapter.MyExpandableViewHolder parentViewHolder, LongClickMenuView longClickMenuView) {
        this.setsPLObject = setsPLObject;
        this.supersets = supersets;
        this.uiSetsClickHandler = uiSetsClickHandler;
        this.parentViewHolder = parentViewHolder;
        this.longClickMenuView = longClickMenuView;
        list = new ArrayList<>();
        list.addAll(setsPLObject.superSets);
        list.addAll(setsPLObject.intraSets);
    }

    @Override
    public IntraSetViewHolder onCreateChildViewHolder(ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View v = inflater.inflate(R.layout.recycler_view_intra_set, container, false);
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

        if (!exerciseSet.hasSomething()) {
            intraSetViewHolder.editContainer.setVisibility(View.VISIBLE);
            intraSetViewHolder.dataLayout.setVisibility(View.INVISIBLE);
        } else {
            intraSetViewHolder.dataLayout.setVisibility(View.VISIBLE);
            intraSetViewHolder.editContainer.setVisibility(View.GONE);
        }
        if (list.get(position).type == WorkoutLayoutTypes.SuperSetIntraSet) {
            intraSetViewHolder.supersetTv.setVisibility(View.VISIBLE);
            //PLObject.ExerciseProfile superset = setsPLObject.parent.exerciseProfiles.get(position);
            intraSetViewHolder.deleteIV.setVisibility(View.INVISIBLE);

            if(supersets.get(position).getExercise() != null){
                set.setExerciseName(supersets.get(position).getExercise().getName());
                intraSetViewHolder.supersetTv.setText("Superset of " +set.getExerciseName());
            }else{
                intraSetViewHolder.supersetTv.setText("No exercise picked for this superset");
            }
            intraSetViewHolder.editContainerTV.setText("Tap to edit Superset");
/*
            if(superset.getExercise()!= null){

                }else{
                    intraSetViewHolder.supersetTv.setText("Superset");
                }
*/

        } else {
            intraSetViewHolder.editContainerTV.setText("Tap to edit Dropset");
            intraSetViewHolder.intraSetTag.setImageResource(R.drawable.child_green);
            intraSetViewHolder.deleteIV.setVisibility(View.VISIBLE);
            intraSetViewHolder.deleteIV.setOnClickListener((delete) -> {
                uiSetsClickHandler.onRemoveIntraSet(setsPLObject, setsPLObject.intraSets.indexOf(set));
            });
        }

        if (position != (list.size() - 1)) {
            intraSetViewHolder.rest.setText(exerciseSet.getRest());
        } else {
            intraSetViewHolder.rest.setText("");
        }

        intraSetViewHolder.reps.setText(exerciseSet.getRep());
        intraSetViewHolder.weight.setText(exerciseSet.getWeight() + "kg");

        intraSetViewHolder.card.setOnClickListener((itemView) -> {

            if(longClickMenuView != null &&longClickMenuView.isOn()){
                uiSetsClickHandler.onSetsLongClick(setsPLObject, parentViewHolder);

            }else{
                uiSetsClickHandler.onSetsClick(parentViewHolder, set);

            }

        });

        intraSetViewHolder.card.setOnLongClickListener((longClicked) -> {
            uiSetsClickHandler.onSetsLongClick(setsPLObject, parentViewHolder);
            return true;
        });
    }


}