package com.techbeloved.moviesbeloved.data.source.remote;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;
import com.techbeloved.moviesbeloved.data.source.remote.api.MoviesWrapper;
import com.techbeloved.moviesbeloved.data.source.remote.api.TMDBMovieService;
import com.techbeloved.moviesbeloved.utils.Constants;
import io.reactivex.Flowable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class MoviesRemoteDataSource implements MoviesDataSource {

    private TMDBMovieService movieService;

    @Inject
    public MoviesRemoteDataSource(@NonNull TMDBMovieService movieService) {
        this.movieService = movieService;
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
        }
        return null;
    }

    private MovieEntity addPosterUrl(MovieEntity movieEntity) {
        String baseImageUrl = Constants.TMDB_IMAGE_BASE_URL;
        Uri.Builder builder = Uri.parse(baseImageUrl).buildUpon();
        builder.appendEncodedPath(Constants.DEFAULT_POSTER_SIZE);
        builder.appendEncodedPath(movieEntity.getPosterPath());
        movieEntity.setPosterUrl(builder.toString());
        return movieEntity;
    }

    @Override
    public LiveData<MovieEntity> getMovie(int movieId) {
        return null;
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
