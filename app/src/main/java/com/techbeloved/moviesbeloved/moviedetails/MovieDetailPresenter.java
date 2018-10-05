package com.techbeloved.moviesbeloved.moviedetails;

import com.techbeloved.moviesbeloved.data.models.*;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;

import androidx.annotation.NonNull;

import java.util.List;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private final MoviesRepository mMoviesRepository;

    private final MovieDetailContract.View mMovieDetailView;

    private int mMovieId;
    private MovieEntity mCurrentMovie;

    public MovieDetailPresenter(int movieId,
                                @NonNull MoviesRepository moviesRepository,
                                @NonNull MovieDetailContract.View movieDetailView) {
        mMovieId = movieId;
        mMoviesRepository = checkNotNull(moviesRepository);
        mMovieDetailView = checkNotNull(movieDetailView);

        mMovieDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openMovie();
    }

    private void openMovie() {
        mMovieDetailView.setLoadingIndicator(true);
        mMoviesRepository.getMovie(mMovieId, new MoviesDataSource.GetMovieCallback() {
            @Override
            public void onMovieLoaded(MovieEntity movie) {
                if (!mMovieDetailView.isActive()) {
                    return;
                }
                // Hold the details of the current movie in place
                mCurrentMovie = movie;
                mMovieDetailView.setLoadingIndicator(false);
                showMovie(movie);
            }

            @Override
            public void onDataNotAvailable() {
                // TODO: 9/21/18 may be some error and data could not be loaded. consider what to do
            }
        });

        // Get the reviews
        mMoviesRepository.getReviews(mMovieId, new MoviesDataSource.LoadReviewsCallback() {
            @Override
            public void onReviewsLoaded(List<ReviewEntity> reviews) {
                showReviews(reviews);
            }

            @Override
            public void onDataNotAvailable() {
                // TODO: 9/26/18 Show no reviews found
            }
        });

        // Get Videos
        mMoviesRepository.getVideos(mMovieId, new MoviesDataSource.LoadVideosCallback() {
            @Override
            public void onVideosLoaded(List<VideoEntity> videos) {
                mMovieDetailView.showVideos(videos);
            }

            @Override
            public void onDataNotAvailable() {
                // TODO: 9/26/18 Show no videos message
            }
        });
    }

    private void showMovie(MovieEntity movie) {
        mMovieDetailView.showMovieDetail(movie);
//        mMovieDetailView.
    }

    @Override
    public void toggleFavorite() {
        MovieEntity currentMovie = getCurrentMovie();
        if (currentMovie != null) {
            if (currentMovie.isFavorite()) {
                // Remove from favorites
                currentMovie.setFavorite(false);
                mMoviesRepository.deleteMovie(currentMovie.getId());
                mMovieDetailView.showMovieUnfavorited();
            } else {
                // Save as a favorite
                currentMovie.setFavorite(true);
                mMoviesRepository.saveMovie(currentMovie);
                mMovieDetailView.showMovieFavorited();
            }
        }
    }

    @Override
    public void playVideo(Video video) {
        mMovieDetailView.openPlayer(video.getKey());
    }

    public MovieEntity getCurrentMovie() {
        return mCurrentMovie;
    }

    public void setCurrentMovie(MovieEntity currentMovie) {
        this.mCurrentMovie = currentMovie;
    }

    private void showReviews(List<ReviewEntity> reviews){
        mMovieDetailView.showReviews(reviews);
    }

}
