package com.techbeloved.moviesbeloved.moviedetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.snackbar.Snackbar;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.YoutubePlayerActivity;
import com.techbeloved.moviesbeloved.common.viewmodel.Response;
import com.techbeloved.moviesbeloved.data.models.*;
import com.techbeloved.moviesbeloved.databinding.ActivityMovieDetailBinding;
import com.techbeloved.moviesbeloved.moviedetails.reviews.ReviewAdapter;
import com.techbeloved.moviesbeloved.moviedetails.videos.VideoAdapter;
import com.techbeloved.moviesbeloved.moviedetails.videos.VideoClickCallback;
import com.techbeloved.moviesbeloved.utils.Constants;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.List;

import static com.techbeloved.moviesbeloved.utils.Constants.MOVIE_ID_EXTRA;

public class MovieDetailActivity extends DaggerAppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private View.OnClickListener mFavFabOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//           toggleFavorite();
            mViewModel.toggleFavorite();
        }
    };

    private VideoClickCallback mVideoOnclickListener = new VideoClickCallback() {
        @Override
        public void onClick(Video video) {
            Timber.i("video onClick was called!");
            openPlayer(video.getKey());
        }
    };

    private ReviewAdapter mReviewAdapter;
    private VideoAdapter mVideoAdapter;

    private ActivityMovieDetailBinding mBinding;

    private MovieDetailViewModel mViewModel;

    @Inject
    public MovieDetailViewModelFactory mViewModelFactory;

    //    @Inject
    public int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        Toolbar toolbar = mBinding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent movieIntent = getIntent();
        if (movieIntent.hasExtra(MOVIE_ID_EXTRA)) {
            movieId = movieIntent.getIntExtra(MOVIE_ID_EXTRA, 0);
        } else {
            finish();
            return;
        }

        setupViews();

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MovieDetailViewModel.class);

        // Bind viewmodel
        mBinding.setViewModel(mViewModel);

        mViewModel.setMovieId(movieId);
        mViewModel.start();
//        mViewModel.getMovieResponse().observe(this, this::processMovieResponse);
        mViewModel.getReviewsResponse().observe(this, this::processReviewResponse);
        mViewModel.getVideosResponse().observe(this, this::processVideosResponse);
    }

    private void processMovieResponse(Response<MovieEntity> movieEntityResponse) {
        switch (movieEntityResponse.status) {
            case ERROR:
                return;
            case LOADING:
                setLoadingIndicator(true);
                break;
            case SUCCESS:
                setLoadingIndicator(false);
                showMovieDetail(movieEntityResponse.data);
        }
    }

    private void processReviewResponse(Response<List<ReviewEntity>> response) {
        switch (response.status) {
            case ERROR:
                return;
            case LOADING:
                return;
            case SUCCESS:
                showReviews(response.data);
        }
    }

    private void processVideosResponse(Response<List<VideoEntity>> response) {
        switch (response.status) {
            case ERROR:
                return;
            case LOADING:
                return;
            case SUCCESS:
                showVideos(response.data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setupViews() {
        mBinding.ratingBar.setMax(5);
        mBinding.ratingBar.setStepSize(0.5f);

        mBinding.favoriteFab.setOnClickListener(mFavFabOnclickListener);

        mReviewAdapter = new ReviewAdapter();
        mBinding.reviewRecyclerView.setAdapter(mReviewAdapter);

        mVideoAdapter = new VideoAdapter(mVideoOnclickListener);
        mBinding.videosRecyclerView.setAdapter(mVideoAdapter);

    }

    public void setLoadingIndicator(boolean active) {
        mBinding.setIsLoading(active);
    }


    public void showMovieFavorited() {
        showFavoriteIcon(true);
        Snackbar.make(mBinding.mainCoordinatorLayout,
                "Movie added to favorites",
                Snackbar.LENGTH_SHORT
        ).show();
    }


    public void showMovieUnfavorited() {
        showFavoriteIcon(false);
        Snackbar.make(mBinding.mainCoordinatorLayout,
                "Movie removed from favorites",
                Snackbar.LENGTH_SHORT
        ).show();
    }

    public void showMovieDetail(MovieEntity movie) {
        displayMovieInfo(movie);
    }

    public void showReviews(List<ReviewEntity> reviews) {
        mReviewAdapter.setReviewList(reviews);
    }

    public void showVideos(List<VideoEntity> videos) {
        mVideoAdapter.setVideoList(videos);
    }

    public void openPlayer(String youtubeKey) {
        Intent youtubePlayerIntent = new Intent(this, YoutubePlayerActivity.class);
        youtubePlayerIntent.putExtra(Constants.YOUTUBE_VIDEO_ID, youtubeKey);
        startActivity(youtubePlayerIntent);
    }


    private void displayMovieInfo(Movie movieInfo) {
        mBinding.setMovie(movieInfo);
    }

    private void showFavoriteIcon(boolean isFavorite) {
        mBinding.setIsFavorite(isFavorite);
    }
}
