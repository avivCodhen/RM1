package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;

import java.util.ArrayList;

public class WorkoutIListHelper {

    private ArrayList<PLObject> list;

    public static WorkoutIListHelper getHelper(ArrayList<PLObject> list){
        return new WorkoutIListHelper(list);
    }

    private WorkoutIListHelper(ArrayList<PLObject> list){

        this.list = list;
    }

    public ArrayList<PLObject> getAllOftype(PLObject from){
        ArrayList<PLObject> arr = new ArrayList<>();
        for (PLObject p : list){
            if(p.type == from.type){
                arr.add(p);
            }
        }
        return arr;
    }
}
