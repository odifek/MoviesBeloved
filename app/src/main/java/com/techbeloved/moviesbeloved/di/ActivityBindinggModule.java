package com.techbeloved.moviesbeloved.di;

import com.techbeloved.moviesbeloved.movies.MoviesActivity;
import com.techbeloved.moviesbeloved.movies.MoviesFragment;
import com.techbeloved.moviesbeloved.movies.MoviesModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindinggModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = MoviesModule.class)
    abstract MoviesActivity moviesActivity();
}
