package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import com.strongest.savingdata.AModels.workoutModel.PLObject;

import java.util.ArrayList;

public class DividerItemAdapter implements WorkoutItemAdapter<PLObject.BodyText> {
    @Override
    public PLObject.BodyText insert() {
        return new PLObject.BodyText();
    }

    @Override
    public ArrayList<PLObject.BodyText> onInsert(int pos, ArrayList<PLObject.BodyText> itemsHolder) {
        return itemsHolder;
    }

    @Override
    public boolean notifyInserted(int start, int count, ItemAdapter adapter) {
        adapter.adapterNotifyItemInserted(start);
        return false;
    }

    @Override
    public void remove(int position, PLObject.BodyText removedItem) {

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
    public PLObject.BodyText onDuplicate(int position, PLObject.BodyText clone) {
        return null;
    }


    @Override
    public int addingDuplicateTo(PLObject.BodyText parent) {
        return 0;
    }

    @Override
    public boolean notifyDuplicate(int position, ItemAdapter adapter) {
        return false;
    }

    @Override
    public PLObject.BodyText onChild(PLObject.BodyText parent) {
        return null;
    }

    @Override
    public int onAddingChildToGroup(PLObject.BodyText parent, PLObject.BodyText child) {
        return 0;
    }

    @Override
    public boolean notifyChild(int position, ItemAdapter adapter) {
        return false;
    }

    @Override
    public PLObject.BodyText replace(PLObject.BodyText toReplace) {
        return null;
    }

    @Override
    public boolean notifyReplaced(int positionReplaced, ItemAdapter adapter) {
        return false;
    }

}
