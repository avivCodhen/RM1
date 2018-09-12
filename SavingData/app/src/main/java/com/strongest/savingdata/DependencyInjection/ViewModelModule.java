package com.strongest.savingdata.DependencyInjection;

import android.arch.lifecycle.ViewModel;

import com.strongest.savingdata.AViewModels.MyProgramsViewModel;
import com.strongest.savingdata.AViewModels.ProgramViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WorkoutsViewModel.class)
    abstract ViewModel bindWorkoutsViewModel(WorkoutsViewModel workoutsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProgramViewModel.class)
    abstract ViewModel bindProgramViewModel(ProgramViewModel programViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyProgramsViewModel.class)
    abstract ViewModel bindMyProgramsViewModel(MyProgramsViewModel programViewModel);

}
