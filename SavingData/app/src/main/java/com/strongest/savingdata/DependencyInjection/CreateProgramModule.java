package com.strongest.savingdata.DependencyInjection;

import android.content.Context;
import android.content.SharedPreferences;

import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AService.WorkoutsService;
import com.strongest.savingdata.Database.Managers.DataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

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
    static WorkoutsService getWorkoutsService(DataManager dataManager,
                                              SharedPreferences sharedPreferences,
                                              SharedPreferences.Editor editor) {
        return new WorkoutsService(dataManager, sharedPreferences, editor);
    }


    @Provides
    @Singleton
    static WorkoutsModel getWorkoutsModel(WorkoutsService workoutsService) {
        return new WorkoutsModel(workoutsService);
    }

    @Provides
    static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("AppSharedPreferences", Context.MODE_PRIVATE);
    }

    @Provides
    static SharedPreferences.Editor getSharedPreferencesEditor(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit();
    }

    @Provides
    static CompositeDisposable getCompositeDisposable() {
        return new CompositeDisposable();
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
