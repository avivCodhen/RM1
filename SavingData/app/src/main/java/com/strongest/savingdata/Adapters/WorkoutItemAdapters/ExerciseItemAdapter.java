package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.AModels.WorkoutItemAdapter;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.strongest.savingdata.AModels.AlgorithmLayout.PLObject.*;

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
    public boolean notifyInserted(int start,int count, ItemAdapter adapter) {

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
    public ExerciseProfile onDuplicate(ExerciseProfile clone) {
        return new ExerciseProfile(clone);
    }

    @Override
    public int addingDuplicateTo(ExerciseProfile parent) {
        return 1+parent.getExerciseProfiles().size();
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
        parent.getExerciseProfiles().add(ep);
        ep.setParent(parent);
        ep.isParent = false;
        return ep;
    }

    @Override
    public int onAddingChildToGroup(ExerciseProfile parent, ExerciseProfile child) {

        return parent.getExerciseProfiles().size();
    }

    @Override
    public boolean notifyChild(int position, ItemAdapter adapter) {
        adapter.adapterNotifyItemInserted(position);
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
