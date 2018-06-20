package com.strongest.savingdata.AViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;
import com.strongest.savingdata.AService.WorkoutsService;
import com.strongest.savingdata.BaseWorkout.Program;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Database.Program.DBProgramHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import static com.strongest.savingdata.Activities.BaseActivity.TAG;

public class WorkoutsViewModel extends ViewModel {


    private Context context;
    @Inject
    public WorkoutsModel workoutsModel;
    private MutableLiveData<Program> program = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Workout>> workoutsList = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Workout>> getWorkoutsList() {
        return workoutsList;
    }

    public MutableLiveData<Program> getProgram() {
        return program;
    }


    //needs to be changed
    public DataManager getDataManager(){
        return workoutsModel.getWorkoutsService().getDataManager();
    }

    @Inject
    public WorkoutsViewModel(WorkoutsModel workoutsModel) {
        program.setValue(workoutsModel.provideProgram());
        workoutsList.setValue(workoutsModel.provideWorktoutsList(program.getValue().dbName));
    }

    public WorkoutsModel getWorkoutsModel() {
        return workoutsModel;
    }
}
