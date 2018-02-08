package com.strongest.savingdata.DependencyInjection;

import dagger.Module;

/**
 * Created by Cohen on 12/21/2017.
 */

@Module
public class AppModule {

    private MainApplication application;

    public AppModule(MainApplication application){
        this.application = application;
    }

    public MainApplication getApplication() {
        return application;
    }
}
