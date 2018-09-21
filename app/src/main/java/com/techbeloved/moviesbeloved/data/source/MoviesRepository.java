package com.techbeloved.moviesbeloved.data.source;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Concrete implementation to load movies from the data sources into a cache
 */
public class MoviesRepository implements MoviesDataSource {

    private static MoviesRepository INSTANCE = null;

    private final MoviesDataSource mMoviesRemoteDataSource;

    private final MoviesDataSource mMoviesLocalDataSource;

    Map<Integer, MovieEntity> mCachedMovies;

    boolean mCacheIsDirty = false;

    // prevent direct instantiation
    private MoviesRepository(@NonNull MoviesDataSource moviesRemoteDataSource,
                             @NonNull MoviesDataSource moviesLocalDataSource) {
        mMoviesRemoteDataSource = moviesRemoteDataSource;
        mMoviesLocalDataSource = moviesLocalDataSource;
    }

    public static MoviesRepository getInstance(MoviesDataSource moviesRemoteDataSource,
                                               MoviesDataSource moviesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository(moviesRemoteDataSource, moviesLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets Movies local data source or remote data source, depending on whether favorites are requested or not
     * By default only favorites are stored locally
     * @param callback sends signal when data is ready
     */
    @Override
    public void getMovies(MovieFilterType filterType, @NonNull final LoadMoviesCallback callback) {

        // Determine if favorites or others that can be fetched from remote data source
        switch (filterType) {
            // Only favorites are saved locally
            case FAVORITES:
                mMoviesLocalDataSource.getMovies(filterType, new LoadMoviesCallback() {
                    @Override
                    public void onMoviesLoaded(List<MovieEntity> movies) {
                        callback.onMoviesLoaded(movies);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
                break;
            case POPULAR:
            case TOP_RATED:
                mMoviesRemoteDataSource.getMovies(filterType, new LoadMoviesCallback() {
                    @Override
                    public void onMoviesLoaded(List<MovieEntity> movies) {
                        callback.onMoviesLoaded(movies);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
                break;
            default:
                callback.onDataNotAvailable();
        }
    }

    @Override
    public void getMovies(MovieFilterType filterType, int page, @NonNull final LoadMoviesCallback callback) {
        // Given page number, results must come from remote sources
        // However, we must be double sure
        switch (filterType) {
            case POPULAR:
            case TOP_RATED:
                mMoviesRemoteDataSource.getMovies(filterType, page, new LoadMoviesCallback() {
                    @Override
                    public void onMoviesLoaded(List<MovieEntity> movies) {
                        callback.onMoviesLoaded(movies);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
                break;
            default:
                callback.onDataNotAvailable();
        }
    }

    @Override
    public void getMovie(final int movieId, @NonNull final GetMovieCallback callback) {

        // First check whether the requested movie is stored locally as a favorite. If not, then request from remote
        MovieEntity cachedMovie = getMovieWithId(movieId);

        // Respond immediately with cache if available
        if (cachedMovie != null) {
            callback.onMovieLoaded(cachedMovie);
            return;
        }

        // Load from server/persisted if needed

        mMoviesLocalDataSource.getMovie(movieId, new GetMovieCallback() {
            @Override
            public void onMovieLoaded(MovieEntity movie) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedMovies == null) {
                    mCachedMovies = new LinkedHashMap<>();
                }
                mCachedMovies.put(movie.getId(), movie);
                callback.onMovieLoaded(movie);
            }

            // Then it must be loaded from remote sources
            @Override
            public void onDataNotAvailable() {
                mMoviesRemoteDataSource.getMovie(movieId, new GetMovieCallback() {
                    @Override
                    public void onMovieLoaded(MovieEntity movie) {
                        // DO in memory cache update to keep the app UI up to date
                        if (mCachedMovies == null) {
                            mCachedMovies = new LinkedHashMap<>();
                        }
                        mCachedMovies.put(movieId, movie);
                        callback.onMovieLoaded(movie);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    /**
     * Saves movie in a local data source which represents favorites
     * @param movie is the movie to be saved
     */
    @Override
    public void saveMovie(@NonNull MovieEntity movie) {

        mMoviesLocalDataSource.saveMovie(movie);
        // Do in memory cache update to keep the app UI up to date
        if (mCachedMovies == null) {
            mCachedMovies = new LinkedHashMap<>();
        }
        mCachedMovies.put(movie.getId(), movie);
    }

    /**
     * Deletes all local movies which are basically favorites
     */
    @Override
    public void deleteAllMovies() {
        mMoviesLocalDataSource.deleteAllMovies();

        if (mCachedMovies == null) {
            mCachedMovies = new LinkedHashMap<>();
        }
        mCachedMovies.clear();
    }

    @Override
    public void deleteMovie(int movieId) {
        mMoviesLocalDataSource.deleteMovie(movieId);
        if (mCachedMovies != null) mCachedMovies.remove(movieId);
    }

    private MovieEntity getMovieWithId(int movieId) {
        if (mCachedMovies == null || mCachedMovies.isEmpty()) {
            return null;
        } else {
            return mCachedMovies.get(movieId);
        }
    }
}
