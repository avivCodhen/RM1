package com.strongest.savingdata.AModels;

import com.strongest.savingdata.AModels.AlgorithmLayout.Lister;
import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;

import java.util.ArrayList;

import javax.annotation.Nullable;

public interface WorkoutItemAdapter<T extends PLObject> {

    interface ItemAdapter {
        boolean adapterNotifyDataSetChanged();

        boolean adapterNotifyItemChanged();

        boolean adapterNotifyItemInserted(int positionTo);

        boolean adapterNotifyItemRemoved(int removeFrom);

        boolean adapterNotifyItemRangeInserted(int start, int count);

        boolean adapterNotifyItemRangeRemoved(int start, int count);
    }

    T insert();

    ArrayList<T> onInsert(int pos, ArrayList<T> itemsHolder);

    boolean notifyInserted(int start, int count, ItemAdapter adapter);

    void remove(int position, T removedItem);

    boolean notifyRemoved(int from, int count, ItemAdapter adapter);

    boolean onDestroy();

    T onDuplicate(T clone);
    boolean notifyDuplicate(int position, ItemAdapter adapter);

    T onChild(T parent);

    int onAddingChildToGroup(T parent, T child);

    boolean notifyChild(int position, ItemAdapter adapter);
}