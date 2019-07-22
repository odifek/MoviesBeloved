package com.techbeloved.moviesbeloved.data.source;

import android.content.Context;

import androidx.room.Room;

import com.techbeloved.moviesbeloved.data.source.local.FavoriteDatabase;
import com.techbeloved.moviesbeloved.data.source.local.MoviesDao;
import com.techbeloved.moviesbeloved.data.source.local.MoviesLocalDataSource;
import com.techbeloved.moviesbeloved.data.source.local.ReviewsDao;
import com.techbeloved.moviesbeloved.data.source.local.VideosDao;
import com.techbeloved.moviesbeloved.data.source.remote.MoviesRemoteDataSource;
import com.techbeloved.moviesbeloved.utils.AppExecutors;
import com.techbeloved.moviesbeloved.utils.DiskIOThreadExecutor;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

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
    static FavoriteDatabase provideDb(Context context) {
        return Room.databaseBuilder(context, FavoriteDatabase.class, "Movies.db")
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
