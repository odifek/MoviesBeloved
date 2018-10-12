package com.techbeloved.moviesbeloved.data.source;

import android.app.Application;
import androidx.room.Room;
import com.techbeloved.moviesbeloved.data.source.local.*;
import com.techbeloved.moviesbeloved.data.source.remote.MoviesRemoteDataSource;
import com.techbeloved.moviesbeloved.utils.AppExecutors;
import com.techbeloved.moviesbeloved.utils.DiskIOThreadExecutor;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.concurrent.Executors;

@Module
public abstract class MoviesRepositoryModule {

    private static final int THREAD_COUNT = 3;

    @Singleton
    @Binds
    @Local
    abstract MoviesDataSource provideMoviesLocalDataSource(MoviesLocalDataSource dataSource);

    @Singleton
    @Binds
    @Remote
    abstract MoviesDataSource provideMoviesRemoteDataSource(MoviesRemoteDataSource dataSource);

    @Singleton
    @Provides
    static FavoriteDatabase provideDb(Application context) {
        return Room.databaseBuilder(context.getApplicationContext(), FavoriteDatabase.class, "Movies.db")
                .build();
    }

    @Singleton
    @Provides
    static MoviesDao provideMoviesDao(FavoriteDatabase db) {
        return db.moviesDao();
    }

    @Singleton
    @Provides
    static ReviewsDao provideReviewsDao(FavoriteDatabase db) {
        return db.reviewsDao();
    }

    @Singleton
    @Provides
    static VideosDao provideVideosDao(FavoriteDatabase db) {
        return db.videosDao();
    }

    @Singleton
    @Provides
    static AppExecutors provideAppExecutors() {
        return new AppExecutors(new DiskIOThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }

}
