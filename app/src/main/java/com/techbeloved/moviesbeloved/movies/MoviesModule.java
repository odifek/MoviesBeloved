package com.techbeloved.moviesbeloved.movies;

import com.techbeloved.moviesbeloved.rx.SchedulersFacade;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class MoviesModule {

    @Singleton
    @Provides
    static SchedulersFacade providesSchedulerFacade() {
        return new SchedulersFacade();
    }
}
