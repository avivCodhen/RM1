package com.strongest.savingdata.AViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AService.ProgramService;

import java.util.ArrayList;

import javax.inject.Inject;

public class MyProgramsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Program>> programList = new MutableLiveData<>();
    private ProgramService programService;
    private Program currentProgram;

    @Inject
    public MyProgramsViewModel(ProgramService programService) {

        this.programService = programService;
    }

    public void setProgramList(MutableLiveData<ArrayList<Program>> programList) {
        this.programList = programList;
    }

    public LiveData<ArrayList<Program>> getProgramList() {
        return programList;
    }

    public void fetchProgramList(String tag){
        programService.fetchAllProgramsByTag(programList, tag);
    }

    public void setCurrentProgram(Program currentProgram) {
        this.currentProgram = currentProgram;
    }

    public Program getCurrentProgram() {
        return currentProgram;
    }


}
