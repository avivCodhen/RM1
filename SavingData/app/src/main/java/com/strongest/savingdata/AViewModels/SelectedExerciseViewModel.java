package com.strongest.savingdata.AViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;

public class SelectedExerciseViewModel extends ViewModel {

    private MutableLiveData<PLObject.ExerciseProfile> selectedExercise = new MutableLiveData<>();

    public SelectedExerciseViewModel(){

    }

    public LiveData<PLObject.ExerciseProfile> getSelectedExercise() {
        return selectedExercise;
    }

    public void select(PLObject.ExerciseProfile selectedExercise) {
        this.selectedExercise.setValue(selectedExercise);
    }
}
