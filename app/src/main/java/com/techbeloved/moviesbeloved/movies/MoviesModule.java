package com.techbeloved.moviesbeloved.movies;

import com.techbeloved.moviesbeloved.di.FragmentScope;
import com.techbeloved.moviesbeloved.rx.SchedulersFacade;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

import javax.inject.Singleton;

@Module
public abstract class MoviesModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract MoviesFragment moviesFragment();

    @Singleton
    @Provides
    static SchedulersFacade providesSchedulerFacade() {
        return new SchedulersFacade();
    }
}
