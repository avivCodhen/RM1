package com.strongest.savingdata.AModels.programModel;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface ProgramDAO {

    @Query("SELECT * FROM program")
    LiveData<List<Program>> getAllPrograms();

    @Query("SELECT * FROM program WHERE dbName = :dbName")
    LiveData<Program> getProgramByName(String dbName);

    @Query("SELECT * FROM program WHERE `key` = :key")
    LiveData<Program> getProgramByKey(String key);


    @Query("DELETE FROM program")
    void nukeTable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProgram(Program program);

    @Delete
    void deleteList(Program program);


}
