package com.strongest.savingdata.AViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AService.ProgramService;
import com.strongest.savingdata.AService.WorkoutsService;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Managers.DataManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class WorkoutsViewModel extends ViewModel {


    private WorkoutsService.CMD cmd;
    private final WorkoutsService workoutsService;
    @Inject
    public WorkoutsModel workoutsModel;
    private final CompositeDisposable compositeDisposable;
    /*private MutableLiveData<Program> program = new MutableLiveData<>();
    private MutableLiveData<List<Program>> programs = new MutableLiveData<>();*/
    private MutableLiveData<ArrayList<Workout>> workoutsList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Beans>> allExercisesList = new MutableLiveData<>();

    public boolean safeToSave;

    public MutableLiveData<ArrayList<Workout>> getWorkoutsList() {
        return workoutsList;
    }

    public MutableLiveData<ArrayList<Beans>> getAllExercisesList() {
        return allExercisesList;
    }

    //TODO: needs to be changed from the LISOV principle
    public DataManager getDataManager() {
        return workoutsModel.getWorkoutsService().getDataManager();
    }

    @Inject
    public WorkoutsViewModel(WorkoutsService workoutsService, WorkoutsModel workoutsModel, CompositeDisposable compositeDisposable) {
        this.workoutsService = workoutsService;
        this.workoutsModel = workoutsModel;
        this.compositeDisposable = compositeDisposable;
    }

    public void initWorkouts() {
        if(cmd == null){
            cmd = WorkoutsService.CMD.INIT;
        }
        workoutsService.provideWorktoutsList(workoutsList, cmd);
        cmd = null;
    }


    public void saveLayoutToDataBase() {
        workoutsService.saveLayoutToDataBase(true, workoutsList.getValue(), null);
    }



    public WorkoutsModel getWorkoutsModel() {
        return workoutsModel;
    }


    public void setNewWorkout() {
        workoutsService.provideWorktoutsList(workoutsList, WorkoutsService.CMD.NEW);
    }

    public void setCmd(WorkoutsService.CMD cmd) {
        this.cmd = cmd;
    }

    public WorkoutsService.CMD getCmd() {
        return cmd;
    }
}
