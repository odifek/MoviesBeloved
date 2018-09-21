package com.techbeloved.moviesbeloved.data.source;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.Movie;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Main entry point for accessing movies data
 */
public interface MoviesDataSource {
    interface LoadMoviesCallback {
        void  onMoviesLoaded(List<Movie> movies);

        void onDataNotAvailable();
    }

    interface GetMovieCallback {
        void onMovieLoaded(Movie movie);

        void onDataNotAvailable();
    }

    /**
     * Gets movies given filter type and page number. Used to query remote sources which are usually paginated
     * @param filterType is the filter type, either of popular, top_rated, or favorites
     * @param callback is used to return the results
     */
    void getMovies(MovieFilterType filterType, @NonNull LoadMoviesCallback callback);

    /**
     * Gets movies given filter type and page number. Used to query remote sources which are usually paginated
     * @param filterType is the filter type, either of popular, top_rated, or favorites
     * @param page is the page number
     * @param callback is used to return the results
     */
    void getMovies(MovieFilterType filterType, int page, @NonNull LoadMoviesCallback callback);

    void getMovie(int movieId, @NonNull GetMovieCallback callback);

    void saveMovie(@NonNull Movie movie);

    void deleteAllMovies();

    void deleteMovie(int movieId);
}
