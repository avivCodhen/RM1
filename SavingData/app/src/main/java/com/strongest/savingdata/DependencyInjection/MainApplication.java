package com.strongest.savingdata.DependencyInjection;

import android.app.Application;

import com.strongest.savingdata.Activities.OnDoneListener;
import com.strongest.savingdata.dagger.AppComponent;
import com.strongest.savingdata.dagger.AppModule;



/**
 * Created by Cohen on 12/19/2017.
 */


//@Component(modules = { CreateProgramModule.class, })
public class MainApplication extends Application{

    MainAppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerMainAppComponent.builder()
                .createProgramModule(new CreateProgramModule(this)).build();
        appComponent.inject(this);
       // appComponent = DaggerAppComponent.builder().appModule(new CreateProgramModule(this)).build();
    }

    public MainAppComponent getAppComponent() {
        return appComponent;
    }
}
