package com.strongest.savingdata.AViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.strongest.savingdata.AModels.workoutModel.PLObject;

import java.util.ArrayList;

public class SelectedLogDataViewModel extends ViewModel {

    private MutableLiveData<ArrayList<PLObject>> sets = new MutableLiveData<>();

    public SelectedLogDataViewModel(){

    }

    public ArrayList<PLObject> getSets(){
        return sets.getValue();
    }

    public void setSets(ArrayList<PLObject> sets){
        this.sets.setValue(sets);
    }
}
