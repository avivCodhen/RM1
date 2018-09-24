package com.strongest.savingdata.AViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.Database.LogData;

import java.util.ArrayList;

public class SelectedLogDataViewModel extends ViewModel {

    private MutableLiveData<ArrayList<LogData.LogDataSets>> sets = new MutableLiveData<>();
    private String date;

    public SelectedLogDataViewModel(){

    }

    public ArrayList<LogData.LogDataSets> getSets(){
        return sets.getValue();
    }

    public void setSets(ArrayList<LogData.LogDataSets> sets){
        this.sets.setValue(sets);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
