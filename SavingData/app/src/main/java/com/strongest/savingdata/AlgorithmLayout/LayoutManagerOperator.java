package com.strongest.savingdata.AlgorithmLayout;

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

    private MyExpandableAdapter adapter;
    private ArrayList<PLObject> layout;
    private LayoutManager.LayoutManagerHelper helper;

    public LayoutManagerOperator(MyExpandableAdapter adapter, ArrayList<PLObject> layout, LayoutManager.LayoutManagerHelper helper) {
        this.adapter = adapter;

        this.layout = layout;
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
                    setsPLObject
            );
            newSetsPLObject.getIntraSets().add(newIntra);
            block.add(newIntra);
        }

        //copies the intrasets from the supersets, if any
        for (int i = 0; i < setsPLObject.getParent().getExerciseProfiles().size(); i++) {
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

        return block;
    }

    public void deleteSuperset(PLObject.ExerciseProfile ep, OnUpdateLayoutStatsListener layoutUpdateCallback, OnWorkoutViewInterfaceClicksListener interfaceCallback) {

        int exercisePos = helper.findPLObjectPosition(ep, layout);

        ep.getParent().getExerciseProfiles().remove(ep);
        LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(DELETE_EXERCISE);
        updateComponents.setPlObject(ep);
        updateComponents.setRemovePosition(exercisePos);
        layoutUpdateCallback.updateLayout(updateComponents);

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

    }

    public void deleteExercise(PLObject.ExerciseProfile ep, OnUpdateLayoutStatsListener layoutUpdateCallback, OnWorkoutViewInterfaceClicksListener interfaceCallback) {
        int length = 1;
        int exercisePos = helper.findPLObjectPosition(ep, layout);
        if (ep.isExpand()) {
            interfaceCallback.collapseExercise(exercisePos);
        }
        layout.remove(exercisePos);
        LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(DELETE_EXERCISE);
        updateComponents.setPlObject(ep);
        updateComponents.setRemovePosition(exercisePos);
        //exArray = layoutUpdateCallback.updateLayout(updateComponents);
        //adapter.setExArray(exArray);
        adapter.notifyItemRangeRemoved(exercisePos, ep.getExerciseProfiles().size() + 1);
    }
}
