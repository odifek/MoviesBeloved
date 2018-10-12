package com.techbeloved.moviesbeloved.data.source.local;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;
import io.reactivex.Flowable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Concrete implementation of a data source as a db.
 */
@Singleton
public class MoviesLocalDataSource implements MoviesDataSource {

    private final FavoriteDatabase mDatabase;


    @Inject
    public MoviesLocalDataSource(final FavoriteDatabase database) {
        mDatabase = database;
    }

    @Override
    public Flowable<List<MovieEntity>> getMovies(MovieFilterType filterType, int page) {
        return mDatabase.moviesDao().getMovies();
    }

    @Override
    public LiveData<MovieEntity> getMovie(int movieId) {
        return mDatabase.moviesDao().getMovieById(movieId);
    }

    @Override
    public LiveData<List<ReviewEntity>> getReviews(int movieId) {
        return null;
    }

    @Override
    public LiveData<List<VideoEntity>> getVideos(int movieId) {
        return null;
    }

    @Override
    public void saveMovie(@NonNull MovieEntity movie) {

    }

    @Override
    public void deleteAllMovies() {

    }

    @Override
    public void deleteMovie(int movieId) {

    }

    @Override
    public void saveReview(@NonNull ReviewEntity review) {

    }

    @Override
    public void deleteReviews(int movieId) {

    }

    @Override
    public void saveVideo(@NonNull VideoEntity video) {

    }

    @Override
    public void deleteVideos(int movieId) {

    }
}
