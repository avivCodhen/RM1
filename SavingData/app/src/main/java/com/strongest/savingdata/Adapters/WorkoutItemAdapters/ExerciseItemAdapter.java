package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
import com.strongest.savingdata.Utils.MyUtils;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.strongest.savingdata.AModels.workoutModel.PLObject.*;

public class ExerciseItemAdapter implements WorkoutItemAdapter<ExerciseProfile> {


    public ExerciseItemAdapter() {

    }

    @Override
    public ExerciseProfile insert() {
        ExerciseProfile ep = new ExerciseProfile();
        ep.setInnerType(WorkoutLayoutTypes.ExerciseProfile);
        ep.isParent = true;
        return ep;
    }

    @Nullable
    @Override
    public ArrayList<ExerciseProfile> onInsert(int pos, ArrayList<ExerciseProfile> itemsHolder) {
        return itemsHolder;
    }

    @Override
    public boolean notifyInserted(int start, int count, ItemAdapter adapter) {

        adapter.adapterNotifyItemRangeInserted(start, count);
        return true;
    }

    @Override
    public void remove(int position, ExerciseProfile removedItem) {

    }


    @Override
    public boolean notifyRemoved(int from, int count, ItemAdapter adapter) {
        adapter.adapterNotifyItemRemoved(from);
        return true;
    }


    @Override
    public boolean onDestroy() {
        return false;
    }

    @Override
    public ExerciseProfile onDuplicate(int position, ExerciseProfile clone) {
        try {
            return MyUtils.clone(clone);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("object cannot be cloned");

    }


    @Override
    public int addingDuplicateTo(ExerciseProfile parent) {
        return 1 + parent.exerciseProfiles.size();
    }

    @Override
    public boolean notifyDuplicate(int position, ItemAdapter adapter) {
        adapter.adapterNotifyItemInserted(position);
        return true;
    }

    @Override
    public ExerciseProfile onChild(ExerciseProfile parent) {
        ExerciseProfile ep = new ExerciseProfile();
        ep.type = WorkoutLayoutTypes.IntraExerciseProfile;
        parent.exerciseProfiles.add(ep);
        //ep.setParent(parent);
        for (int i = 0; i < parent.getSets().size(); i++) {
            PLObject.SetsPLObject intraSet = new PLObject.SetsPLObject();
            intraSet.innerType = WorkoutLayoutTypes.SuperSetIntraSet;
            intraSet.type = WorkoutLayoutTypes.SuperSetIntraSet;
            parent.getSets().get(i).superSets.add(intraSet);
        }
        ep.isParent = false;
        return ep;
    }

    @Override
    public int onAddingChildToGroup(ExerciseProfile parent, ExerciseProfile child) {

        return parent.exerciseProfiles.size();
    }

    @Override
    public boolean notifyChild(int position, ItemAdapter adapter) {
        adapter.adapterNotifyItemChanged(position);
        return true;
    }

    @Override
    public ExerciseProfile replace(ExerciseProfile toReplace) {
        return toReplace;
    }

    @Override
    public boolean notifyReplaced(int positionReplaced, ItemAdapter adapter) {
        adapter.adapterNotifyItemChanged(positionReplaced);
        return true;
    }
}
