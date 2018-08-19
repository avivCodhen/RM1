package com.strongest.savingdata.DependencyInjection;

import android.app.Application;

import com.strongest.savingdata.Activities.BaseActivity;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Fragments.BaseFragment;
import com.strongest.savingdata.Fragments.ProgramSettingsFragment;
import com.strongest.savingdata.dagger.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Cohen on 12/19/2017.
 */

@Singleton
@Component(modules = {CreateProgramModule.class,
        ViewModelModule.class,
        UserModule.class,
        RoomModule.class})
public interface MainAppComponent {
    void inject(MainApplication application);

    void inject(BaseActivity baseActivity);

    void inject(ProgramSettingsFragment programSettingsFragment);
    void inject(BaseFragment baseFragment);
}
