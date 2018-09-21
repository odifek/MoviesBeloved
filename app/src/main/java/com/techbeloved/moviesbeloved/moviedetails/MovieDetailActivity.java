package com.techbeloved.moviesbeloved.moviedetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.techbeloved.moviesbeloved.Injection;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import static com.techbeloved.moviesbeloved.utils.Constants.MOVIE_ID_EXTRA;
import static com.techbeloved.moviesbeloved.utils.MovieUtils.getYearFromDate;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private MovieDetailContract.Presenter mPresenter;

    private  boolean mIsActive;

    private int mCurrentMovieId;

    private CollapsingToolbarLayout mCollapsingToolbar;
    private TextView mSynopsisText;
    private ImageView mBackdropImage;
    private ImageView mPosterImage;
    private TextView mYearText;
    private TextView mRatingText;
    private TextView mTitleText;
    private RatingBar mRatingBar;
    private TextView mGenreText;

    private ContentLoadingProgressBar mProgressBar;
    private ConstraintLayout mContentLayoutView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent movieIntent = getIntent();
        if (movieIntent.hasExtra(MOVIE_ID_EXTRA)) {
            mCurrentMovieId = movieIntent.getIntExtra(MOVIE_ID_EXTRA, 0);
        } else {
            finish();
            return;
        }

        // Create Presenter. Presenter is set in the setPresenter method
        new MovieDetailPresenter(mCurrentMovieId,
                Injection.provideMoviesRepository(getApplicationContext()),
                this
        );

        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsActive = true;
        mPresenter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsActive = false;
    }

    private void setupViews() {
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
        mTitleText = findViewById(R.id.original_title_text);
        mBackdropImage = findViewById(R.id.backdrop);
        mPosterImage = findViewById(R.id.iv_movie_poster_anchor);
        mRatingText = findViewById(R.id.user_rating);
        mSynopsisText = findViewById(R.id.synopsis_text);
        mRatingBar = findViewById(R.id.ratingBar);
        mYearText = findViewById(R.id.release_year_text);
        mGenreText = findViewById(R.id.genre_text);
        mRatingBar.setMax(5);
        mRatingBar.setStepSize(0.5f);

        mProgressBar = findViewById(R.id.loading_progressbar);
        mContentLayoutView = findViewById(R.id.movie_detail_view);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBar.show();
        } else {
            mProgressBar.hide();
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMovieFavorited() {

    }

    @Override
    public void showMovieUnfavorited() {

    }

    @Override
    public void showMovieDetail(MovieEntity movie) {
        mContentLayoutView.setVisibility(View.VISIBLE);
        displayMovieInfo(movie);
    }

    @Override
    public boolean isActive() {
        return mIsActive;
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void displayMovieInfo(Movie movieInfo) {
        mTitleText.setText(movieInfo.getTitle());
        mSynopsisText.setText(movieInfo.getSynopsis());
        mCollapsingToolbar.setTitle(movieInfo.getTitle());

        String year = getYearFromDate(movieInfo.getReleaseDate());
        mYearText.setText(year);

        mRatingText.setText(String.valueOf(movieInfo.getUserRating()));
        mRatingBar.setRating(movieInfo.getUserRating() / 2);

        if (!movieInfo.getGenres().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0, j = movieInfo.getGenres().size(); i < j; i++) {
                builder.append(movieInfo.getGenres().get(i));
                if (i != j - 1) builder.append(" | ");
            }
            mGenreText.setText(builder.toString());
        }

        if (!TextUtils.isEmpty(movieInfo.getBackdropUrl())) {
            Glide.with(mBackdropImage.getContext())
                    .load(movieInfo.getBackdropUrl())
                    .into(mBackdropImage);
        } else {
            Glide.with(mBackdropImage.getContext())
                    .load(R.drawable.dancers)
                    .into(mBackdropImage);
        }

        if (!TextUtils.isEmpty(movieInfo.getPosterUrl())) {
            Glide.with(mPosterImage.getContext())
                    .load(movieInfo.getPosterUrl())
                    .into(mPosterImage);
        } else {
            Glide.with(mPosterImage.getContext())
                    .load(R.drawable.dancers)
                    .into(mPosterImage);
        }
    }
}
