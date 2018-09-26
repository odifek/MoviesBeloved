package com.techbeloved.moviesbeloved.data.source;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import java.util.List;

import androidx.annotation.NonNull;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;

/**
 * Main entry point for accessing movies data
 */
public interface MoviesDataSource {
    interface LoadMoviesCallback {
        void  onMoviesLoaded(List<MovieEntity> movies);

        void onDataNotAvailable();
    }

    interface GetMovieCallback {
        void onMovieLoaded(MovieEntity movie);

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

    void saveMovie(@NonNull MovieEntity movie);

    void deleteAllMovies();

    void deleteMovie(int movieId);


    /**
     * Callback for loading reviews for the movie
     */
    interface LoadReviewsCallback {
        void onReviewsLoaded(List<ReviewEntity> reviews);

        void onDataNotAvailable();
    }


    /**
     * Get all reviews for a particular video. Use this without page number to query local database
     * @param callback is used to return the results
     * @param movieId is the id of the movie you need the reviews
     */
    void getReviews(int movieId, @NonNull LoadReviewsCallback callback);

    /**
     * Get all reviews for a particular video. Use this to query paginated online source
     * @param movieId is the id fo rthe movie
     * @param page is the page number
     * @param callback returns the results after successful query
     */
    void getReviews(int movieId, int page, @NonNull LoadReviewsCallback callback);

    void saveReview(@NonNull ReviewEntity review);

    /**
     * Delete all reviews associated with a movie in the database
     * @param movieId is the id of the movie
     */
    void deleteReviews(int movieId);


    /**
     * Callback and methods for loading video information for the movie
     */
    interface LoadVideosCallback {
        void onVideosLoaded(List<VideoEntity> videos);

        void onDataNotAvailable();
    }

    void getVideos(int movieId, @NonNull LoadVideosCallback callback);

    void saveVideo(@NonNull VideoEntity video);

    void deleteVideos(int movieId);
}
