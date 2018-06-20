package com.strongest.savingdata.DependencyInjection;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModelValidator;
import com.strongest.savingdata.AService.WorkoutsService;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModelFactory;
import com.strongest.savingdata.Database.Managers.DataManager;

import java.util.ArrayList;

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
    static DataManager getDataManager(Context context) {
        return new DataManager(context);
    }


    @Provides
    @Singleton
    static WorkoutsService getWorkoutsService(DataManager dataManager) {
        return new WorkoutsService(dataManager);
    }


    @Provides
    @Singleton
    static WorkoutsModel getWorkoutsModel(WorkoutsService workoutsService) {
        return new WorkoutsModel(workoutsService);
    }

    /*@Provides
    @Singleton
    public ViewModelProvider.Factory workoutsViewModelFactory(Context context, DataManager dataManager) {
        return new WorkoutsViewModelFactory(context, dataManager);
    }
*/

    /*@Provides
    @Singleton
    public Programmer getProgrammer(Context context, DataManager dataManager) {
        return new Programmer(context, dataManager);
    }*/

}
