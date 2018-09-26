package com.techbeloved.moviesbeloved.moviedetails;

import com.techbeloved.moviesbeloved.BasePresenter;
import com.techbeloved.moviesbeloved.BaseView;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.data.models.Video;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;

import java.util.List;

public interface MovieDetailContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showMovieFavorited();

        void showMovieUnfavorited();

        void showMovieDetail(MovieEntity movie);

        void showReviews(List<ReviewEntity> reviews);

        void showVideos(List<VideoEntity> videos);

        // Whether view is active
        boolean isActive();

        void openPlayer(String youtubeKey);
    }

    interface Presenter extends BasePresenter {
        void toggleFavorite();

        void playVideo(Video video);
    }
}
