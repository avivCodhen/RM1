package com.strongest.savingdata.AViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AService.ProgramService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ProgramViewModel extends ViewModel {

    private final ProgramService programService;

    private LiveData<Program> program;
    private MutableLiveData<Program> newProgram = new MutableLiveData<>();
    private MediatorLiveData<Program> mediatorLiveData;
    private MutableLiveData<ArrayList<Program>> allPrograms = new MutableLiveData<>();


    @Inject
    public ProgramViewModel(ProgramService programService) {
        this.programService = programService;
        mediatorLiveData = new MediatorLiveData<>();

        program = programService.provideProgram();

    }

    public void fetchAllPrograms() {
        programService.fetchAllPrograms(allPrograms);
    }

    public LiveData<Program> getProgram() {
        return program;
    }


    public LiveData<Program> getNewProgram() {
        return newProgram;
    }

    public void setNewProgram() {
        newProgram.setValue(programService.getNewProgram());

    }

    public MediatorLiveData<Program> getMediatorLiveData() {
        return mediatorLiveData;
    }

    public void setProgram(LiveData<Program> program) {
        this.program = program;
    }

    public LiveData<ArrayList<Program>> getAllPrograms() {
        return allPrograms;
    }

    public void switchProgram(Program p) {
        programService.insertProgram(p);
        newProgram.setValue(p);

    }
}
