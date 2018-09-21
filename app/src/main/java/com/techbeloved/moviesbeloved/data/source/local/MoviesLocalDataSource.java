package com.techbeloved.moviesbeloved.data.source.local;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;
import com.techbeloved.moviesbeloved.utils.AppExecutors;

import java.util.List;

import androidx.annotation.NonNull;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
public class MoviesLocalDataSource implements MoviesDataSource {

    private static volatile MoviesLocalDataSource INSTANCE;

    private MoviesDao mMoviesDao;

    private AppExecutors mAppExecutors;

    private MoviesLocalDataSource(@NonNull AppExecutors appExecutors,
                                  @NonNull MoviesDao moviesDao) {
        mAppExecutors = appExecutors;
        mMoviesDao = moviesDao;
    }

    public static MoviesLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                    @NonNull MoviesDao moviesDao) {
        if (INSTANCE == null) {
            synchronized (MoviesLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MoviesLocalDataSource(appExecutors, moviesDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getMovies(MovieFilterType filterType, @NonNull final LoadMoviesCallback callback) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<MovieEntity> movies = mMoviesDao.getMovies();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (movies.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onMoviesLoaded(movies);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getMovies(MovieFilterType filterType, int page, @NonNull LoadMoviesCallback callback) {
        // Not applicable in local data source
        callback.onDataNotAvailable();
    }

    @Override
    public void getMovie(final int movieId, @NonNull final GetMovieCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final MovieEntity movie = mMoviesDao.getMovieById(movieId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (movie != null) {
                            callback.onMovieLoaded(movie);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveMovie(@NonNull final MovieEntity movie) {
        checkNotNull(movie);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mMoviesDao.insertMovie(movie);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllMovies() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mMoviesDao.deleteMovies();
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteMovie(final int movieId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mMoviesDao.deleteMovieById(movieId);
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    static void clearInstance() {
        INSTANCE = null;
    }
}
