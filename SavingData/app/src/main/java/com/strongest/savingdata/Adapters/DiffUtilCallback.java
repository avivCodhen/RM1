package com.strongest.savingdata.Adapters;

import android.support.v7.util.DiffUtil;

import com.strongest.savingdata.AModels.workoutModel.PLObject;

import java.util.ArrayList;

public class DiffUtilCallback extends DiffUtil.Callback {

    private ArrayList<PLObject> oldList;
    private ArrayList<PLObject> newList;

    public DiffUtilCallback(ArrayList<PLObject> oldList, ArrayList<PLObject> newList){

        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).hashCode() == newList.get(newItemPosition).hashCode();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
