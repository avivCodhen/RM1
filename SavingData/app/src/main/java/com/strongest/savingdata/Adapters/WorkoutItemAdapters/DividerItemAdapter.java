package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.WorkoutItemAdapter;

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
        return false;
    }

    @Override
    public boolean onDestroy() {
        return false;
    }

    @Override
    public PLObject.BodyText onDuplicate(PLObject.BodyText clone) {
        return null;
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

}
