package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import com.strongest.savingdata.AModels.workoutModel.PLObject;

import java.util.ArrayList;

public interface WorkoutItemAdapter<T extends PLObject> {

    interface ItemAdapter {
        boolean adapterNotifyDataSetChanged();

        boolean adapterNotifyItemChanged(int positionChanged);

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

    T onDuplicate(int positionOfClone, T clone);

    int addingDuplicateTo(T parent);

    boolean notifyDuplicate(int position, ItemAdapter adapter);

    T onChild(T parent);

    int onAddingChildToGroup(T parent, T child);

    boolean notifyChild(int position, ItemAdapter adapter);

    T replace(T toReplace);

    boolean notifyReplaced(int positionReplaced, ItemAdapter adapter);
}