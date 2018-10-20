package com.techbeloved.moviesbeloved.data.source.remote;

import android.net.Uri;
import androidx.annotation.NonNull;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;
import com.techbeloved.moviesbeloved.data.source.remote.api.MoviesWrapper;
import com.techbeloved.moviesbeloved.data.source.remote.api.ReviewsWrapper;
import com.techbeloved.moviesbeloved.data.source.remote.api.TMDBMovieService;
import com.techbeloved.moviesbeloved.data.source.remote.api.VideosWrapper;
import com.techbeloved.moviesbeloved.utils.Constants;
import io.reactivex.Flowable;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Singleton
public class MoviesRemoteDataSource implements MoviesDataSource {

    private TMDBMovieService movieService;

    private String maxRealeaseDate;


    @Inject
    public MoviesRemoteDataSource(@NonNull TMDBMovieService movieService) {
        this.movieService = movieService;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        maxRealeaseDate = dateFormat.format(now);
    }

    @Override
    public Flowable<List<MovieEntity>> getMovies(MovieFilterType filterType, int page) {
        switch (filterType) {
            case POPULAR:
                return movieService.getPopularMovies(page)
                        .map(MoviesWrapper::getMovieList)
                        .map(movieEntities -> {
                            for (int i = 0; i < movieEntities.size(); i++) {
                                // Modify the object in place
                                movieEntities.set(i, addPosterUrl(movieEntities.get(i)));
                            }
                            return movieEntities;
                        });
            case TOP_RATED:
                return movieService.getHighestRatedMovies(page)
                        .map(MoviesWrapper::getMovieList)
                        .map(movieEntities -> {
                            for (int i = 0; i < movieEntities.size(); i++) {
                                // Modify the object in place
                                movieEntities.set(i, addPosterUrl(movieEntities.get(i)));
                            }
                            return movieEntities;
                        });
            case LATEST:
                return movieService.getLatestMovies(maxRealeaseDate, page)
                        .map(MoviesWrapper::getMovieList)
                        .map(movieEntities -> {
                            for (int i = 0; i < movieEntities.size(); i++) {
                                // Modify the object in place
                                movieEntities.set(i, addPosterUrl(movieEntities.get(i)));
                            }
                            return movieEntities;
                        });
        }
        return null;
    }

    private MovieEntity addPosterUrl(MovieEntity movieEntity) {
        String baseImageUrl = Constants.TMDB_IMAGE_BASE_URL;
        Uri.Builder builder = Uri.parse(baseImageUrl).buildUpon();
        builder.appendEncodedPath(Constants.DEFAULT_POSTER_SIZE);
        if (!movieEntity.getPosterPath().isEmpty()) {
            builder.appendEncodedPath(movieEntity.getPosterPath());
            movieEntity.setPosterUrl(builder.toString());
        }

        // Append backdrop url also
        if (null != movieEntity.getBackdropUrl()) {
            Uri.Builder backdropBuilder = Uri.parse(baseImageUrl).buildUpon();
            backdropBuilder.appendEncodedPath(Constants.DEFAULT_BACKDROP_SIZE);
            backdropBuilder.appendEncodedPath(movieEntity.getBackdropUrl());
            movieEntity.setBackdropUrl(backdropBuilder.toString());
        }

        return movieEntity;
    }

    @Override
    public Single<MovieEntity> getMovie(int movieId) {
        return movieService.getMovie(movieId)
                .map(this::addPosterUrl);
    }

    @Override
    public Flowable<List<ReviewEntity>> getReviews(int movieId) {
        return movieService.getReviews(movieId)
                .map(ReviewsWrapper::getReviewList);
    }

    @Override
    public Flowable<List<VideoEntity>> getVideos(int movieId) {
        return movieService.getVideos(movieId)
                .map(VideosWrapper::getVideoList);
    }

    @Override
    public void saveMovie(@NonNull MovieEntity movie) {
        // We don't save movie to remote
    }

    @Override
    public void deleteAllMovies() {
        // not applicable
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
