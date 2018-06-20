package com.strongest.savingdata.AViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;

import java.util.ArrayList;

public class SingleWorkoutViewModel extends ViewModel {

    private MutableLiveData<ArrayList<PLObject>>exArray = new MutableLiveData<>();

    public MutableLiveData<ArrayList<PLObject>> getExArray(){
        return exArray;
    }

    public SingleWorkoutViewModel(ArrayList<PLObject> exArray){
        this.exArray.setValue(exArray);
    }


}
