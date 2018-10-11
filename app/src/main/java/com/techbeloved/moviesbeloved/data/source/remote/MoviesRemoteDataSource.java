package com.techbeloved.moviesbeloved.data.source.remote;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;

import java.util.List;

import androidx.annotation.NonNull;

import javax.inject.Singleton;


public class MoviesRemoteDataSource {

//    private static MoviesRemoteDataSource INSTANCE;
//
//    private MovieRemoteDao mMovieRemoteDao;
//
//    public static MoviesRemoteDataSource getInstance(MovieRemoteDao movieRemoteDao) {
//        if (INSTANCE == null) {
//            INSTANCE = new MoviesRemoteDataSource(movieRemoteDao);
//        }
//        return INSTANCE;
//    }
//
//    // Prevent instantiation
//    private MoviesRemoteDataSource(MovieRemoteDao movieRemoteDao) {
//        mMovieRemoteDao = movieRemoteDao;
//    }
//
//    @Override
//    public void getMovies(MovieFilterType filterType, @NonNull final LoadMoviesCallback callback) {
//        // The first page is requested
//        int page = 1;
//        mMovieRemoteDao.getMovies(filterType, page, new LoadMoviesCallback() {
//            @Override
//            public void onMoviesLoaded(List<MovieEntity> movies) {
//                callback.onMoviesLoaded(movies);
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                callback.onDataNotAvailable();
//            }
//        });
//    }
//
//    @Override
//    public void getMovies(MovieFilterType filterType, int page, @NonNull final LoadMoviesCallback callback) {
//        mMovieRemoteDao.getMovies(filterType, page, new LoadMoviesCallback() {
//            @Override
//            public void onMoviesLoaded(List<MovieEntity> movies) {
//                callback.onMoviesLoaded(movies);
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                callback.onDataNotAvailable();
//            }
//        });
//    }
//
//    @Override
//    public void getMovie(int movieId, @NonNull final GetMovieCallback callback) {
//        mMovieRemoteDao.getMovie(movieId, new GetMovieCallback() {
//            @Override
//            public void onMovieLoaded(MovieEntity movie) {
//                callback.onMovieLoaded(movie);
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                callback.onDataNotAvailable();
//            }
//        });
//    }
//
//    @Override
//    public void saveMovie(@NonNull MovieEntity movie) {
//        // Not used in remote server
//    }
//
//    @Override
//    public void deleteAllMovies() {
//        // Not applicable
//    }
//
//    @Override
//    public void deleteMovie(int movieId) {
//        // Not applicable
//    }
//
//    @Override
//    public void getReviews(int movieId, @NonNull final LoadReviewsCallback callback) {
//        mMovieRemoteDao.getReviews(movieId, 1, new LoadReviewsCallback() {
//            @Override
//            public void onReviewsLoaded(List<ReviewEntity> reviews) {
//                callback.onReviewsLoaded(reviews);
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                callback.onDataNotAvailable();
//            }
//        });
//    }
//
//    @Override
//    public void getReviews(int movieId, int page, @NonNull final LoadReviewsCallback callback) {
//        mMovieRemoteDao.getReviews(movieId, page, new LoadReviewsCallback() {
//            @Override
//            public void onReviewsLoaded(List<ReviewEntity> reviews) {
//                callback.onReviewsLoaded(reviews);
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                callback.onDataNotAvailable();
//            }
//        });
//    }
//
//    @Override
//    public void saveReview(@NonNull ReviewEntity review) {
//        // Not yet implemented
//    }
//
//    @Override
//    public void deleteReviews(int movieId) {
//        // Not applicable
//    }
//
//    @Override
//    public void getVideos(int movieId, @NonNull final LoadVideosCallback callback) {
//        mMovieRemoteDao.getVideos(movieId, new LoadVideosCallback() {
//            @Override
//            public void onVideosLoaded(List<VideoEntity> videos) {
//                callback.onVideosLoaded(videos);
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                callback.onDataNotAvailable();
//            }
//        });
//    }
//
//    @Override
//    public void saveVideo(@NonNull VideoEntity video) {
//        // Not applicable
//    }
//
//    @Override
//    public void deleteVideos(int movieId) {
//        // Not applicable
//    }
}
