package com.techbeloved.moviesbeloved.movies;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;
import com.techbeloved.moviesbeloved.data.source.MoviesRepository;

import java.util.List;

import androidx.annotation.NonNull;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({ MoviesFragment}), retrieves the data and updates the
 * UI as required.
 */
public class MoviesPresenter implements MoviesContract.Presenter {

    private final MoviesRepository mMoviesRepository;

    private final MoviesContract.View mMoviesView;

    // The default filter is most popular movies
    private MovieFilterType mCurrentFiltering = MovieFilterType.POPULAR;

    public MoviesPresenter(@NonNull MoviesRepository moviesRepository, @NonNull MoviesContract.View moviesView) {
        mMoviesRepository = checkNotNull(moviesRepository, "moviesRepository cannot be null!");
        mMoviesView = checkNotNull(moviesView, "moviesView cannot be null!");

        mMoviesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMovies();
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadMovies() {
        mMoviesView.setLoadingIndicator(true);
        mMoviesRepository.getMovies(mCurrentFiltering, new MoviesDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<MovieEntity> movies) {
                if (!mMoviesView.isActive()) {
                    return;
                }
                mMoviesView.setLoadingIndicator(false);
                mMoviesView.showMovies(movies);
            }

            @Override
            public void onDataNotAvailable() {
                mMoviesView.setLoadingIndicator(false);
                mMoviesView.showNoMovies();
            }
        });
    }

    /**
     * This is used to get more movies from next page
     *
     * @param page is the page number
     */
    @Override
    public void loadMoreMovies(int page) {
        mMoviesRepository.getMovies(mCurrentFiltering, page, new MoviesDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<MovieEntity> movies) {
                if (!mMoviesView.isActive()) {
                    return;
                }
                mMoviesView.setLoadingIndicator(false);
                mMoviesView.showMoreMovies(movies);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void openMovieDetails(int requestedMovieId) {
        mMoviesView.showMovieDetails(requestedMovieId);
    }


    @Override
    public void setFiltering(MovieFilterType filtering) {
        mCurrentFiltering = filtering;
    }

    @Override
    public MovieFilterType getFiltering() {
        return mCurrentFiltering;
    }
}
