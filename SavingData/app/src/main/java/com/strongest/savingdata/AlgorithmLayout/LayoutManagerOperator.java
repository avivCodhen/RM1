package com.strongest.savingdata.AlgorithmLayout;

import android.content.Context;
import android.widget.Toast;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.MyViews.WorkoutView.OnUpdateLayoutStatsListener;
import com.strongest.savingdata.MyViews.WorkoutView.OnWorkoutViewInterfaceClicksListener;

import java.util.ArrayList;

import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.DELETE_EXERCISE;

/**
 * Created by Cohen on 3/29/2018.
 */

public class LayoutManagerOperator {

    private Context context;
    private MyExpandableAdapter adapter;
    private LayoutManager.LayoutManagerHelper helper;

    public LayoutManagerOperator(Context context, MyExpandableAdapter adapter, LayoutManager.LayoutManagerHelper helper) {
        this.context = context;
        this.adapter = adapter;

        this.helper = helper;
    }

    public PLObject.SetsPLObject injectNewSet(PLObject.SetsPLObject setsPLObject) {
        PLObject.SetsPLObject newSetsPLObject = new PLObject.SetsPLObject(
                setsPLObject.getParent(),
                new ExerciseSet(setsPLObject.getExerciseSet()));

        newSetsPLObject.setInnerType(WorkoutLayoutTypes.SetsPLObject);
        int childPosition = helper.findSetPosition(setsPLObject);
        setsPLObject.getParent().getSets().add(childPosition + 1, newSetsPLObject);

        return newSetsPLObject;
    }

    public PLObject.IntraSetPLObject injectNewIntraSet(PLObject.SetsPLObject setsPLObject) {
        PLObject.IntraSetPLObject intraSetPLObject = new PLObject.IntraSetPLObject(
                setsPLObject.getParent(),
                new ExerciseSet(),
                WorkoutLayoutTypes.IntraSetNormal,
                setsPLObject
        );
        setsPLObject.getIntraSets().add(intraSetPLObject);
        return intraSetPLObject;
    }

    public ArrayList<PLObject> injectCopySet(PLObject.SetsPLObject setsPLObject) {
        ArrayList<PLObject> block = new ArrayList<>();
        int childPosition = helper.findSetPosition(setsPLObject);
        PLObject.SetsPLObject newSetsPLObject = injectNewSet(setsPLObject);
        block.add(newSetsPLObject);
        //copy the intrasets of this specific set (if it has any)
        for (int i = 0; i < setsPLObject.getIntraSets().size(); i++) {
            PLObject.IntraSetPLObject newIntra = new PLObject.IntraSetPLObject(
                    setsPLObject.getParent(),
                    new ExerciseSet(setsPLObject.getIntraSets().get(i).getExerciseSet()),
                    WorkoutLayoutTypes.IntraSetNormal,
                    newSetsPLObject
            );
            newSetsPLObject.getIntraSets().add(newIntra);
            block.add(newIntra);
        }

        //copies the intrasets from the supersets, if any
        for (int i = 0; i < setsPLObject.getParent().getExerciseProfiles().size(); i++) {
            if(childPosition < setsPLObject.getParent().getExerciseProfiles().get(i).getIntraSets().size()){
                block.add(setsPLObject.getParent().getExerciseProfiles().get(i).getIntraSets().get(childPosition));
            }else{

                PLObject.IntraSetPLObject newIntra = new PLObject.IntraSetPLObject(
                        setsPLObject.getParent().getExerciseProfiles().get(i),
                        new ExerciseSet(setsPLObject.getParent().getExerciseProfiles().get(i).getIntraSets().get(childPosition).getExerciseSet()),
                        WorkoutLayoutTypes.SuperSetIntraSet,
                        setsPLObject
                );

                //creates a new intra set for each superset
                setsPLObject.getParent().getExerciseProfiles().get(i).getIntraSets().add(childPosition + 1, newIntra);
                block.add(newIntra);
            }
        }

        return block;
    }

    /*public void deleteSuperset(PLObject.ExerciseProfile ep, OnUpdateLayoutStatsListener layoutUpdateCallback, OnWorkoutViewInterfaceClicksListener interfaceCallback) {

        int exercisePos = helper.findPLObjectPosition(ep, layout);

        ep.getParent().getExerciseProfiles().remove(ep);
        LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(DELETE_EXERCISE);
        updateComponents.setPlObject(ep);
        updateComponents.setRemovePosition(exercisePos);
       layout = layoutUpdateCallback.updateLayout(updateComponents);
        adapter.setExArray(layout);
        adapter.notifyItemRemoved(exercisePos);
        if (ep.isExpand()) {

            for (int i = exercisePos; i < layout.size(); i++) {
                for (int j = 0; j < ep.getParent().getSets().size(); j++) {
                    if (layout.get(i).type == WorkoutLayoutTypes.IntraSet && ep.getIntraSets().get(j) == layout.get(i)) {
                        layout.remove(i);
                        adapter.notifyItemRemoved(i);
                    }
                }
            }
        }
        int start = 0;
        int end = 0;
        for (int i = exercisePos; i < layout.size(); i++) {
            if (layout.get(i).getType() == WorkoutLayoutTypes.SetsPLObject) {
                break;
            } else {
                start++;
            }
        }

        //adapter.notifyItemRangeRemoved(position+start, ep.getParent().getSets().size()*2);

        return;

    }*/

/*
    public void deleteExercise(PLObject.ExerciseProfile ep, OnUpdateLayoutStatsListener layoutUpdateCallback, OnWorkoutViewInterfaceClicksListener interfaceCallback) {
        int length = 1;
        int exercisePos = helper.findPLObjectPosition(ep, layout);
        if (ep.isExpand()) {
            interfaceCallback.collapseExercise(exercisePos);
        }
        LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(DELETE_EXERCISE);
        updateComponents.setPlObject(ep);
        updateComponents.setRemovePosition(exercisePos);
        layout= layoutUpdateCallback.updateLayout(updateComponents);
        adapter.setExArray(layout);
        adapter.notifyItemRemoved(exercisePos);
        //adapter.notifyItemRangeRemoved(exercisePos, ep.getExerciseProfiles().size() + 1);
    }
*/

    public void deleteSet(PLObject.SetsPLObject setsPLObject, ArrayList<PLObject> layout){
        int position = helper.findPLObjectPosition(setsPLObject, layout);
        if (setsPLObject.getParent().getSets().size() == 1) {
            Toast.makeText(context, "You cannot delete the only set.", Toast.LENGTH_SHORT).show();
            return;
        }
            int count = 0;
        int childPosition = helper.findSetPosition(setsPLObject);
            setsPLObject.getParent().getSets().remove(childPosition);
            for (int i = 0; i < setsPLObject.getParent().getExerciseProfiles().size(); i++) {
                count++;
                setsPLObject.getParent().getExerciseProfiles().get(i).getIntraSets().remove(childPosition);
                layout.remove(position);
            }
            for (int i = 0; i < setsPLObject.getIntraSets().size() + 1; i++) {
                layout.remove(position);
                count++;
            }
            int end = 0;
            for (int i = position; i < layout.size(); i++) {
                if (layout.get(i) instanceof PLObject.ExerciseProfile) {
                    break;
                } else {
                    end++;
                }
            }
            adapter.notifyItemRangeRemoved(position, count);
            adapter.notifyItemRangeChanged(position, end);
            adapter.notifyItemChanged(helper.findPLObjectPosition(setsPLObject.getParent(), layout));
            return;
        }


        public void deleteIntraSet(PLObject.IntraSetPLObject intraSetPLObject, ArrayList<PLObject> layout){
            intraSetPLObject.getParentSet().getIntraSets().remove(intraSetPLObject);
            int pos = helper.findPLObjectPosition(intraSetPLObject, layout);
            layout.remove(pos);
        }
}
