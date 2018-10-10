package com.techbeloved.moviesbeloved;

import android.app.Application;

import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import com.techbeloved.moviesbeloved.data.source.local.FavoriteDatabase;
import com.techbeloved.moviesbeloved.data.source.local.MoviesLocalDataSource;
import com.techbeloved.moviesbeloved.utils.AppExecutors;
import timber.log.Timber;

public class MoviesApp extends Application {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppExecutors = new AppExecutors();
        Timber.plant(new Timber.DebugTree());
    }

    public FavoriteDatabase getDatabase() {
        return FavoriteDatabase.getInstance(this);
    }

    public MoviesRepository getRepository() {
        return MoviesRepository.getInstance(
                MoviesLocalDataSource.getInstance(
                        getDatabase()));
    }
}
