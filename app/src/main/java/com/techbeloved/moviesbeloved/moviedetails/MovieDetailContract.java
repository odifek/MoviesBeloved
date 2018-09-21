package com.techbeloved.moviesbeloved.moviedetails;

import com.techbeloved.moviesbeloved.BasePresenter;
import com.techbeloved.moviesbeloved.BaseView;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.movies.MoviesContract;

public interface MovieDetailContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showMovieFavorited();

        void showMovieUnfavorited();

        void showMovieDetail(MovieEntity movie);

        // Whether view is active
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void markFavorite();
    }
}
