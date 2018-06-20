package com.strongest.savingdata.AViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class WorkoutsViewModelFactory implements   ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels;

    @Inject
    Context context;

    @Inject
    public WorkoutsViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>>viewModels){

        this.viewModels = viewModels;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try{
            return (T) viewModels.get(modelClass).get();
        }catch (Exception e ){
            throw new IllegalArgumentException(e);
        }

    }
}
