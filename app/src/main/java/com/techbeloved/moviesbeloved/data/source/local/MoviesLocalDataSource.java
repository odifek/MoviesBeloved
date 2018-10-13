package com.techbeloved.moviesbeloved.data.source.local;

import androidx.annotation.NonNull;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;
import com.techbeloved.moviesbeloved.utils.AppExecutors;
import io.reactivex.Flowable;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
@Singleton
public class MoviesLocalDataSource implements MoviesDataSource {

    private final FavoriteDatabase mDatabase;

    private AppExecutors mAppExecutors;


    @Inject
    public MoviesLocalDataSource(final FavoriteDatabase database, AppExecutors appExecutors) {
        mDatabase = database;
        mAppExecutors = appExecutors;
    }

    @Override
    public Flowable<List<MovieEntity>> getMovies(MovieFilterType filterType, int page) {
        return mDatabase.moviesDao().getMovies();
    }

    @Override
    public Single<MovieEntity> getMovie(int movieId) {
        return mDatabase.moviesDao().getMovieById(movieId);
    }

    @Override
    public Flowable<List<ReviewEntity>> getReviews(int movieId) {
        return mDatabase.reviewsDao().getReviews(movieId);
    }

    @Override
    public Flowable<List<VideoEntity>> getVideos(int movieId) {
        return mDatabase.videosDao().getVideos(movieId);
    }

    @Override
    public void saveMovie(@NonNull MovieEntity movie) {
        checkNotNull(movie);
        Runnable runnable = () -> mDatabase.moviesDao().insertMovie(movie);
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllMovies() {
        Runnable runnable = () -> mDatabase.moviesDao().deleteMovies();
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteMovie(int movieId) {
        Runnable runnable = () -> mDatabase.moviesDao().deleteMovieById(movieId);
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveReview(@NonNull ReviewEntity review) {
        Runnable runnable = () -> mDatabase.reviewsDao().insertReview(review);
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteReviews(int movieId) {
        Runnable runnable = () -> mDatabase.reviewsDao().deleteReviews(movieId);
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveVideo(@NonNull VideoEntity video) {
        Runnable runnable = () -> mDatabase.videosDao().insertVideo(video);
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteVideos(int movieId) {
        Runnable runnable = () -> mDatabase.videosDao().deleteVideos(movieId);
        mAppExecutors.diskIO().execute(runnable);
    }
}
