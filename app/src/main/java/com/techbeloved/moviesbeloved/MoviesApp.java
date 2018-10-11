package com.techbeloved.moviesbeloved;

import androidx.annotation.VisibleForTesting;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import com.techbeloved.moviesbeloved.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import timber.log.Timber;

import javax.inject.Inject;

public class MoviesApp extends DaggerApplication {
    @Inject
    MoviesRepository moviesRepository;
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    @VisibleForTesting
    public MoviesRepository getMoviesRepository() {
        return moviesRepository;
    }
}
