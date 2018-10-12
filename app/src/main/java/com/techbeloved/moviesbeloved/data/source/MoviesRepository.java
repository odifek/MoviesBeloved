package com.techbeloved.moviesbeloved.data.source;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;
import io.reactivex.Flowable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation to load movies from the data sources into a cache
 */
@Singleton
public class MoviesRepository implements MoviesDataSource {

    private final MoviesDataSource mMoviesRemoteDataSource;

    private final MoviesDataSource mMoviesLocalDataSource;

    Map<Integer, MovieEntity> mCachedMovies;

    boolean mCacheIsDirty = false;

    // prevent direct instantiation
    @Inject
    public MoviesRepository(@NonNull @Remote MoviesDataSource moviesRemoteDataSource,
                            @NonNull @Local MoviesDataSource moviesLocalDataSource) {
        mMoviesRemoteDataSource = moviesRemoteDataSource;
        mMoviesLocalDataSource = moviesLocalDataSource;
    }


    /**
     * Gets Movies local data source or remote data source, depending on whether favorites are requested or not
     * By default only favorites are stored locally
     *  sends signal when data is ready
     */
    @Override
    public Flowable<List<MovieEntity>> getMovies(MovieFilterType filterType, int page) {

        // Determine if favorites or others that can be fetched from remote data source
        switch (filterType) {
            // Only favorites are saved locally
            case FAVORITES:
                return mMoviesLocalDataSource.getMovies(filterType, page);
            default:
                return mMoviesRemoteDataSource.getMovies(filterType, page);
        }
    }

    @Override
    public LiveData<MovieEntity> getMovie(final int movieId/*, @NonNull final GetMovieCallback callback*/) {

        return mMoviesLocalDataSource.getMovie(movieId);
    }

    @Override
    public LiveData<List<ReviewEntity>> getReviews(int movieId) {
        return null;
    }

    @Override
    public LiveData<List<VideoEntity>> getVideos(int movieId) {
        return null;
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

    @Override
    public void saveReview(@NonNull ReviewEntity review) {
        mMoviesLocalDataSource.saveReview(review);
    }

    @Override
    public void deleteReviews(int movieId) {
        mMoviesLocalDataSource.deleteReviews(movieId);
    }

    @Override
    public void saveVideo(@NonNull VideoEntity video) {
        mMoviesLocalDataSource.saveVideo(video);
    }

    @Override
    public void deleteVideos(int movieId) {
        mMoviesLocalDataSource.deleteVideos(movieId);
    }

    private MovieEntity getMovieWithId(int movieId) {
        if (mCachedMovies == null || mCachedMovies.isEmpty()) {
            return null;
        } else {
            return mCachedMovies.get(movieId);
        }
    }
}
