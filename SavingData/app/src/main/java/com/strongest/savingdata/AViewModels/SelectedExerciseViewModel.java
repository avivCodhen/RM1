package com.strongest.savingdata.AViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;

import java.util.ArrayList;

public class SelectedExerciseViewModel extends ViewModel {

    private MutableLiveData<PLObject.ExerciseProfile> selectedExercise = new MutableLiveData<>();
    private MutableLiveData<Workout> parentWorkout= new MutableLiveData<>();
    private MutableLiveData<Workout> exerciseAsList = new MutableLiveData<>();
    private MutableLiveData<PLObject.ExerciseProfile> modifiedExerciseProfile = new MutableLiveData<>();
    private int selectedExercisePosition;

    public SelectedExerciseViewModel(){

    }

    public MutableLiveData<PLObject.ExerciseProfile> getModifiedExerciseProfile() {
        return modifiedExerciseProfile;
    }

    public void modifyNewExerciseProfile(PLObject.ExerciseProfile ep){
        this.modifiedExerciseProfile.setValue(ep);
    }
    public LiveData<PLObject.ExerciseProfile> getSelectedExercise() {
        return selectedExercise;
    }

    public MutableLiveData<Workout> getExerciseAsList() {
        return exerciseAsList;
    }

    public void select(PLObject.ExerciseProfile selectedExercise) {
        this.selectedExercise.setValue(selectedExercise);
    }

    public void setExpandedExerciseList(Workout w){
        this.exerciseAsList.setValue(w);
    }

    public void setSelectedExercise(PLObject.ExerciseProfile ep) {
        this.selectedExercise.setValue(ep);
    }

    public void setSelectedExercisePosition(int selectedExercisePosition) {
        this.selectedExercisePosition = selectedExercisePosition;
    }

    public int getSelectedExercisePosition() {
        return selectedExercisePosition;
    }

    public Workout getParentWorkout() {
        return parentWorkout.getValue();
    }

    public void setParentWorkout(Workout parentWorkout) {
        this.parentWorkout.setValue(parentWorkout);
    }
}
