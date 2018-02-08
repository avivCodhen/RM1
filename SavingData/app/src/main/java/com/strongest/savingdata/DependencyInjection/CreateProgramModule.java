package com.strongest.savingdata.DependencyInjection;

import android.content.Context;

import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.ArtificialInteligence.ArtificialIntelligence;
import com.strongest.savingdata.BaseWorkout.Programmer;
import com.strongest.savingdata.Database.Managers.DataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Cohen on 12/20/2017.
 */

@Module
public class CreateProgramModule {

    private MainApplication application;

    public CreateProgramModule(MainApplication application) {
        this.application = application;
    }


    @Provides
    @Singleton
    public MainApplication provideContext() {
        return application;
    }


    @Provides
    @Singleton
    public Context getContext() {
        return application;
    }

    @Provides
    @Singleton
    public DataManager getDataManager(Context context) {
        return new DataManager(context);
    }


    @Provides
    @Singleton
    public LayoutManager getProgramLayoutManager(Context context, DataManager dataManager){
        int workoutOrder = dataManager.getPrefs().getInt(DataManager.WORKOUT_ORDER, -1);
        LayoutManager plm = new LayoutManager(context, dataManager);
        plm.readLayoutFromDataBase(workoutOrder);
        return plm;
    }

    @Provides
    @Singleton
    public Programmer getProgrammer(Context context, DataManager dataManager) {
        return new Programmer(context, dataManager, true);
    }



    @Provides
    @Singleton
    public ArtificialIntelligence getAI(Context context, Programmer programmer) {
        return new ArtificialIntelligence(context, programmer);
    }
}
