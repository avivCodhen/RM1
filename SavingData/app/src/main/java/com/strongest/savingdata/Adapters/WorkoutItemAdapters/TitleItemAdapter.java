package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import com.strongest.savingdata.AModels.workoutModel.PLObject;

import java.util.ArrayList;

public class TitleItemAdapter implements WorkoutItemAdapter<PLObject.ExerciseProfile> {
    @Override
    public PLObject.ExerciseProfile insert() {
        return PLObject.ExerciseProfile.getBodyTextInstance();
    }

    @Override
    public ArrayList<PLObject.ExerciseProfile> onInsert(int pos, ArrayList<PLObject.ExerciseProfile> itemsHolder) {
        return itemsHolder;
    }

    @Override
    public boolean notifyInserted(int start, int count, ItemAdapter adapter) {
        adapter.adapterNotifyItemInserted(start);
        return false;
    }

    @Override
    public void remove(int position, PLObject.ExerciseProfile removedItem) {

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
    public PLObject.ExerciseProfile onDuplicate(int position, PLObject.ExerciseProfile clone) {
        return null;
    }


    @Override
    public int addingDuplicateTo(PLObject.ExerciseProfile parent) {
        return 0;
    }

    @Override
    public boolean notifyDuplicate(int position, ItemAdapter adapter) {
        return false;
    }

    @Override
    public PLObject.ExerciseProfile onChild(PLObject.ExerciseProfile parent) {
        return null;
    }

    @Override
    public int onAddingChildToGroup(PLObject.ExerciseProfile parent, PLObject.ExerciseProfile child) {
        return 0;
    }

    @Override
    public boolean notifyChild(int position, ItemAdapter adapter) {
        return false;
    }

    @Override
    public PLObject.ExerciseProfile replace(PLObject.ExerciseProfile toReplace) {
        return null;
    }

    @Override
    public boolean notifyReplaced(int positionReplaced, ItemAdapter adapter) {
        return false;
    }

}
