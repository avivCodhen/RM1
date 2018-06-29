package com.strongest.savingdata.AViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;

public class SelectedSetViewModel extends ViewModel {

    private MutableLiveData<PLObject.SetsPLObject> selectedSet = new MutableLiveData<>();
    private MutableLiveData<ExerciseSet> selectedExerciseSet= new MutableLiveData<>();

    public PLObject.SetsPLObject getSelectedSets(){
        return selectedSet.getValue();
    }

    public MutableLiveData<ExerciseSet> getSelectedExerciseSet() {
        return selectedExerciseSet;
    }

    public void select(PLObject.SetsPLObject setsPLObject){
        this.selectedSet.setValue(setsPLObject);
    }

    public void modifySetSelected(ExerciseSet exerciseSet){
        this.selectedExerciseSet.setValue(exerciseSet);
    }
}
