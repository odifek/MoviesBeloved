package com.techbeloved.moviesbeloved.movies;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.PositionalDataSource;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;

import java.util.Collections;
import java.util.List;

import timber.log.Timber;

/**
 * A {@link DataSource} for loading favorites from local db into {@link androidx.paging.PagedList}.
 * Since our recyclerview makes use of paged list, everything that will be sent to the adapter should be paged
 */
public class FavoritesDataSource extends PositionalDataSource<MovieEntity> {

    private MoviesRepository repository;

    private boolean favoritesLoaded;

    public FavoritesDataSource(MoviesRepository repository) {
        this.repository = repository;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<MovieEntity> callback) {
        try {
            List<MovieEntity> movieEntities = repository.getMovies(MovieFilterType.FAVORITES, params.requestedStartPosition).blockingFirst(Collections.emptyList());
            Timber.i("Got movies: %s", movieEntities);
            callback.onResult(movieEntities, params.requestedStartPosition, movieEntities.size());
            favoritesLoaded = true;
        } catch (Throwable throwable) {
            Timber.w(throwable);
        }

    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<MovieEntity> callback) {
        if (!favoritesLoaded) {
            List<MovieEntity> movieEntities = repository.getMovies(MovieFilterType.FAVORITES, params.startPosition).blockingFirst(Collections.emptyList());
            callback.onResult(movieEntities);
            favoritesLoaded = true;
        }
    }

    public static class FavoritesDataSourceFactory extends Factory<Integer, MovieEntity> {

        private MoviesRepository moviesRepository;

        public FavoritesDataSourceFactory(MoviesRepository moviesRepository) {
            this.moviesRepository = moviesRepository;
        }

        @NonNull
        @Override
        public DataSource<Integer, MovieEntity> create() {
            return new FavoritesDataSource(moviesRepository);
        }
    }
}
