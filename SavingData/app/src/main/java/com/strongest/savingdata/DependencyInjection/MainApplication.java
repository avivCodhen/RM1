package com.strongest.savingdata.DependencyInjection;

import android.app.Application;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;


/**
 * Created by Cohen on 12/19/2017.
 */


//@Component(modules = { CreateProgramModule.class, })

@ReportsCrashes(
        mailTo = "kaazz931@gmail.com")
public class MainApplication extends Application {

    MainAppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);

        appComponent = DaggerMainAppComponent.builder()
                .createProgramModule(new CreateProgramModule(this))
                .userModule(new UserModule(this))
                .roomModule(new RoomModule(this))
                .build();
        appComponent.inject(this);
        // appComponent = DaggerAppComponent.builder().appModule(new CreateProgramModule(this)).build();
    }

    public MainAppComponent getAppComponent() {
        return appComponent;
    }
}
