package com.strongest.savingdata.AModels.programModel;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Program.class},
        version = 1,
        exportSchema = false)
public abstract class ProgramDataBase extends RoomDatabase{

    public abstract ProgramDAO getProgramDAO();
}
