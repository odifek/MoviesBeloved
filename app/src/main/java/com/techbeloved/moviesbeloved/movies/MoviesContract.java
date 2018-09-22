package com.techbeloved.moviesbeloved.movies;

import com.techbeloved.moviesbeloved.BasePresenter;
import com.techbeloved.moviesbeloved.BaseView;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import java.util.List;

import androidx.annotation.NonNull;

public interface MoviesContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showMovies(List<MovieEntity> movies);

        void showMoreMovies(List<MovieEntity> movies);

        void showLoadingMoviesError();

        void showNoMovies();

        // Whether view is active
        boolean isActive();

        void showMovieDetails(int requestedMovieId);
    }

    interface Presenter extends BasePresenter {
        void result(int requestCode, int resultCode);

        void loadMovies();

        void loadMoreMovies(int page);

        void openMovieDetails(int requestedMovieId);

        void setFiltering(MovieFilterType filtering);

        MovieFilterType getFiltering();

        void setNextPageToLoad(int nextPage);

        int getNextPageToLoad();

        void reloadMovies();
    }
}
