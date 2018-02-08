package com.strongest.savingdata.dagger;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Cohen on 9/23/2017.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
}
