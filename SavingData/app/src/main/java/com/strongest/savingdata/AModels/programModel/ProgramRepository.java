package com.strongest.savingdata.AModels.programModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.strongest.savingdata.Controllers.CallBacks;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ProgramRepository {

    private final ProgramDAO programDAO;

    private MutableLiveData programByKey = new MutableLiveData();

    @Inject
    public ProgramRepository(ProgramDAO programDAO) {

        this.programDAO = programDAO;
    }


    public LiveData<List<Program>> getAllPrograms() {
        return programDAO.getAllPrograms();
    }

    public LiveData<Program> getProgramByName(String dbName) {
        return programDAO.getProgramByName(dbName);
    }

    public LiveData<Program> getProgramByKey(String key) {
        return programDAO.getProgramByKey(key);
    }

    public Single<Program> getCurrentProgram(String key) {
        return programDAO.getCurrentProgram(key);
    }

    public void updateProgram(Program program) {
        Completable.fromRunnable(() -> {
            programDAO.updateProgram(program);

        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void nukeTable() {
        programDAO.nukeTable();
    }

    public void insertProgram(Program program, CallBacks.OnFinish onFinish) {
        Completable.fromRunnable(() -> programDAO.insertProgram(program))
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                    if (onFinish != null)
                        onFinish.onFinish(1);
                });
    }

    public void deleteList(Program program) {
        programDAO.deleteList(program);
    }
}
