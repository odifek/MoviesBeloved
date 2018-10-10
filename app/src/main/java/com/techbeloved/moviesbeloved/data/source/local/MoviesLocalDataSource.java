package com.techbeloved.moviesbeloved.data.source.local;

import androidx.lifecycle.LiveData;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Flowable;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
public class MoviesLocalDataSource implements MoviesDataSource {

    private static volatile MoviesLocalDataSource INSTANCE;

    private final FavoriteDatabase mDatabase;


    private MoviesLocalDataSource(final FavoriteDatabase database) {
        mDatabase = database;
    }

    public static MoviesLocalDataSource getInstance(final FavoriteDatabase database) {
        if (INSTANCE == null) {
            synchronized (MoviesLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MoviesLocalDataSource(database);
                }
            }
        }
        return INSTANCE;
    }

//    private MoviesDao mMoviesDao;
//
//    private ReviewsDao mReviewsDao;
//
//    private VideosDao mVideosDao;
//
//    private AppExecutors mAppExecutors;
//
//    private MoviesLocalDataSource(@NonNull AppExecutors appExecutors,
//                                  @NonNull MoviesDao moviesDao,
//                                  @NonNull ReviewsDao reviewsDao,
//                                  @NonNull VideosDao videosDao) {
//        mAppExecutors = appExecutors;
//        mMoviesDao = moviesDao;
//        mReviewsDao = reviewsDao;
//        mVideosDao = videosDao;
//    }
//
//    public static MoviesLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
//                                                    @NonNull MoviesDao moviesDao,
//                                                    @NonNull ReviewsDao reviewsDao,
//                                                    @NonNull VideosDao videosDao) {
//        if (INSTANCE == null) {
//            synchronized (MoviesLocalDataSource.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = new MoviesLocalDataSource(appExecutors, moviesDao, reviewsDao, videosDao);
//                }
//            }
//        }
//        return INSTANCE;
//    }
//
//    @Override
//    public void getMovies(MovieFilterType filterType, @NonNull final LoadMoviesCallback callback) {
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                final List<MovieEntity> movies = mMoviesDao.getMovies();
//                mAppExecutors.mainThread().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (movies.isEmpty()) {
//                            callback.onDataNotAvailable();
//                        } else {
//                            callback.onMoviesLoaded(movies);
//                        }
//                    }
//                });
//            }
//        };
//
//        mAppExecutors.diskIO().execute(runnable);
//    }
//
//    @Override
//    public void getMovies(MovieFilterType filterType, int page, @NonNull LoadMoviesCallback callback) {
//        // Not applicable in local data source
//        callback.onDataNotAvailable();
//    }
//
//    @Override
//    public void getMovie(final int movieId, @NonNull final GetMovieCallback callback) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                final MovieEntity movie = mMoviesDao.getMovieById(movieId);
//
//                mAppExecutors.mainThread().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (movie != null) {
//                            callback.onMovieLoaded(movie);
//                        } else {
//                            callback.onDataNotAvailable();
//                        }
//                    }
//                });
//            }
//        };
//
//        mAppExecutors.diskIO().execute(runnable);
//    }
//
//    @Override
//    public void saveMovie(@NonNull final MovieEntity movie) {
//        checkNotNull(movie);
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                mMoviesDao.insertMovie(movie);
//            }
//        };
//        mAppExecutors.diskIO().execute(runnable);
//    }
//
//    @Override
//    public void deleteAllMovies() {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                mMoviesDao.deleteMovies();
//            }
//        };
//        mAppExecutors.diskIO().execute(runnable);
//    }
//
//    @Override
//    public void deleteMovie(final int movieId) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                mMoviesDao.deleteMovieById(movieId);
//            }
//        };
//
//        mAppExecutors.diskIO().execute(runnable);
//    }
//
//    @Override
//    public void getReviews(final int movieId, @NonNull final LoadReviewsCallback callback) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                final List<ReviewEntity> reviews = mReviewsDao.getReviews(movieId);
//                mAppExecutors.mainThread().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (reviews != null) {
//                            callback.onReviewsLoaded(reviews);
//                        } else {
//                            callback.onDataNotAvailable();
//                        }
//                    }
//                });
//            }
//        };
//        mAppExecutors.diskIO().execute(runnable);
//    }
//
//    @Override
//    public void getReviews(int movieId, int page, @NonNull LoadReviewsCallback callback) {
//        // paginated content not applicable in local database. Or maybe later on
//    }
//
//    @Override
//    public void saveReview(@NonNull final ReviewEntity review) {
//        checkNotNull(review);
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                mReviewsDao.insertReview(review);
//            }
//        };
//        mAppExecutors.diskIO().execute(runnable);
//    }
//
//    @Override
//    public void deleteReviews(final int movieId) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                mReviewsDao.deleteReviews(movieId);
//            }
//        };
//
//        mAppExecutors.diskIO().execute(runnable);
//    }
//
//    @Override
//    public void getVideos(final int movieId, @NonNull final LoadVideosCallback callback) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                final List<VideoEntity> videos = mVideosDao.getVideos(movieId);
//                mAppExecutors.mainThread().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (videos != null) {
//                            callback.onVideosLoaded(videos);
//                        } else {
//                            callback.onDataNotAvailable();
//                        }
//                    }
//                });
//            }
//        };
//        mAppExecutors.diskIO().execute(runnable);
//    }
//
//    @Override
//    public void saveVideo(@NonNull final VideoEntity video) {
//        checkNotNull(video);
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                mVideosDao.insertVideo(video);
//            }
//        };
//        mAppExecutors.diskIO().execute(runnable);
//    }
//
//    @Override
//    public void deleteVideos(final int movieId) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                mVideosDao.deleteVideos(movieId);
//            }
//        };
//        mAppExecutors.diskIO().execute(runnable);
//    }

    static void clearInstance() {
        INSTANCE = null;
    }

    @Override
    public Flowable<List<MovieEntity>> getMovies(MovieFilterType filterType) {
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
