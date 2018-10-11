package com.techbeloved.moviesbeloved.movies;

import android.content.Context;
import com.techbeloved.moviesbeloved.common.domain.LoadFavoriteMoviesUseCase;
import com.techbeloved.moviesbeloved.common.domain.LoadMoviesUseCase;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import com.techbeloved.moviesbeloved.di.ActivityScope;
import com.techbeloved.moviesbeloved.di.FragmentScope;
import com.techbeloved.moviesbeloved.rx.SchedulersFacade;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

import javax.inject.Singleton;

@Module
public abstract class MoviesModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract MoviesFragment moviesFragment();

//    @ActivityScope
//    @Binds
//    abstract MoviesViewModelFactory providesMoviesViewModelFactory(MoviesViewModelFactory factory);

    @Binds
    abstract LoadMoviesUseCase providesFavoriteMoviesUseCase(LoadFavoriteMoviesUseCase loadFavoriteMoviesUseCase);

    @Singleton
    @Provides
    static SchedulersFacade providesSchedulerFacade() {
        return new SchedulersFacade();
    }
}
