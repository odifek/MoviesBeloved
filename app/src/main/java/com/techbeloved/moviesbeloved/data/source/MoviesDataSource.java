package com.techbeloved.moviesbeloved.data.source;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;
import io.reactivex.Flowable;

import java.util.List;

/**
 * Main entry point for accessing movies data
 */
public interface MoviesDataSource {
    // Use LiveData onwards
    Flowable<List<MovieEntity>> getMovies(MovieFilterType filterType, int page);

    LiveData<MovieEntity> getMovie(int movieId);

    LiveData<List<ReviewEntity>> getReviews(int movieId);

    LiveData<List<VideoEntity>> getVideos(int movieId);

    void saveMovie(@NonNull MovieEntity movie);

    void deleteAllMovies();

    void deleteMovie(int movieId);
    void saveReview(@NonNull ReviewEntity review);


    /**
     * Delete all reviews associated with a movie in the database
     *
     * @param movieId is the id of the movie
     */
    void deleteReviews(int movieId);
    void saveVideo(@NonNull VideoEntity video);

    void deleteVideos(int movieId);
}
