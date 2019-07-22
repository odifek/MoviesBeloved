package com.techbeloved.moviesbeloved.movies;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;

import java.util.List;

import timber.log.Timber;

/**
 * A {@link PageKeyedDataSource} that takes care of loading movies from online source page by page
 */
public final class MoviesPagingDataSource extends PageKeyedDataSource<Integer, MovieEntity> {

    private MoviesRepository repository;

    private MovieFilterType filterType;


    private MoviesPagingDataSource(MoviesRepository repository, MovieFilterType filterType) {
        this.repository = repository;
        this.filterType = filterType;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, MovieEntity> callback) {
        try {
            List<MovieEntity> movies = repository.getMovies(filterType, 1).blockingFirst();
            callback.onResult(movies, null, 2);
        } catch (Throwable throwable) {
            Timber.w(throwable);
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MovieEntity> callback) {
        Timber.i("Load before, %s", params.key);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MovieEntity> callback) {
        try {
            List<MovieEntity> movies = repository.getMovies(filterType, params.key).blockingFirst();
            callback.onResult(movies, params.key + 1);
        } catch (Throwable throwable) {
            Timber.w(throwable);
        }
    }

    public static class MoviesPagingFactory extends PageKeyedDataSource.Factory<Integer, MovieEntity> {

        private MovieFilterType filterType;
        private MoviesRepository repository;

        MoviesPagingFactory(MovieFilterType filterType, MoviesRepository repository) {
            this.filterType = filterType;
            this.repository = repository;
        }

        @NonNull
        @Override
        public DataSource<Integer, MovieEntity> create() {
            return new MoviesPagingDataSource(repository, filterType);
        }
    }
}
