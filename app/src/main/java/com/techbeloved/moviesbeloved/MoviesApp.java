package com.techbeloved.moviesbeloved;

import android.app.Application;
import android.os.StrictMode;

import androidx.annotation.VisibleForTesting;

import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import com.techbeloved.moviesbeloved.di.AppComponent;
import com.techbeloved.moviesbeloved.di.DaggerAppComponent;

import timber.log.Timber;

import javax.inject.Inject;

public class MoviesApp extends Application {
    @Inject
    MoviesRepository moviesRepository;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        appComponent = DaggerAppComponent.builder().application(this).build();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @VisibleForTesting
    public MoviesRepository getMoviesRepository() {
        return moviesRepository;
    }
}
