package com.strongest.savingdata.AModels.programModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ProgramRepository implements ProgramDAO {

    private final ProgramDAO programDAO;

    private MutableLiveData programByKey = new MutableLiveData();

    @Inject
    public ProgramRepository(ProgramDAO programDAO) {

        this.programDAO = programDAO;
    }


    @Override
    public LiveData<List<Program>> getAllPrograms() {
        return programDAO.getAllPrograms();
    }

    @Override
    public LiveData<Program> getProgramByName(String dbName) {
        return programDAO.getProgramByName(dbName);
    }

    @Override
    public LiveData<Program> getProgramByKey(String key) {
        return programDAO.getProgramByKey(key);
    }

    @Override
    public Single<Program> getCurrentProgram(String key) {
        return programDAO.getCurrentProgram(key);
    }

    @Override
    public void updateProgram(Program program) {
        Completable.fromRunnable(() -> {
            programDAO.updateProgram(program);

        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void nukeTable() {
        programDAO.nukeTable();
    }

    @Override
    public void insertProgram(Program program) {
        Completable.fromRunnable(()->{
            programDAO.insertProgram(program);
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void deleteList(Program program) {
        programDAO.deleteList(program);
    }
}
