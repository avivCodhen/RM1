package com.strongest.savingdata.DependencyInjection;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;

import com.strongest.savingdata.AModels.programModel.ProgramDAO;
import com.strongest.savingdata.AModels.programModel.ProgramDataBase;
import com.strongest.savingdata.AModels.programModel.ProgramRepository;
import com.strongest.savingdata.AService.ProgramService;
import com.strongest.savingdata.AService.UserService;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    private final ProgramDataBase programDataBase;

    public RoomModule(MainApplication mainApplication) {
        this.programDataBase = Room.databaseBuilder(
                mainApplication,
                ProgramDataBase.class,
                "programs.db"
        ).build();
    }

    @Provides
    public ProgramDAO getProgramDAO() {
        return programDataBase.getProgramDAO();
    }

    @Provides
    public ProgramRepository getProgramRepository(ProgramDAO programDAO) {
        return new ProgramRepository(programDAO);
    }

    @Provides
    public ProgramService getProgramService(UserService userService,
                                            ProgramRepository programRepository,
                                            SharedPreferences sharedPreferences,
                                            SharedPreferences.Editor editor) {
        return new ProgramService(userService,programRepository,sharedPreferences,editor);
    }
}
