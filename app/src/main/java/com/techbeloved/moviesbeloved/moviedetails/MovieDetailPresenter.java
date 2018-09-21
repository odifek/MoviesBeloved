package com.techbeloved.moviesbeloved.moviedetails;

import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;

import androidx.annotation.NonNull;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private final MoviesRepository mMoviesRepository;

    private final MovieDetailContract.View mMovieDetailView;

    private int mMovieId;

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
                mMovieDetailView.setLoadingIndicator(false);
                showMovie(movie);
            }

            @Override
            public void onDataNotAvailable() {
                // TODO: 9/21/18 may be some error and data could not be loaded. consider what to do
            }
        });
    }

    private void showMovie(MovieEntity movie) {
        mMovieDetailView.showMovieDetail(movie);
//        mMovieDetailView.
    }

    @Override
    public void markFavorite() {

    }

}
