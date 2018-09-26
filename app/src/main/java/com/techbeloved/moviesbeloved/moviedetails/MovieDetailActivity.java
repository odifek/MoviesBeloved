package com.techbeloved.moviesbeloved.moviedetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import timber.log.Timber;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.techbeloved.moviesbeloved.Injection;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import java.util.List;

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
    private CoordinatorLayout mMainCoordinatorLayout;

    private FloatingActionButton mFavoriteFab;

    private View.OnClickListener mFavFabOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPresenter.toggleFavorite();
        }
    };

    private RecyclerView mReviewList;
    private ReviewAdapter mReviewAdapter;

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
        mMainCoordinatorLayout = findViewById(R.id.main_coordinator_layout);
        mFavoriteFab = findViewById(R.id.favorite_fab);

        mFavoriteFab.setOnClickListener(mFavFabOnclickListener);

        mReviewList = findViewById(R.id.review_recycler_view);
        mReviewAdapter = new ReviewAdapter();
        mReviewList.setAdapter(mReviewAdapter);
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
        showFavoriteIcon(true);
        Snackbar.make(mMainCoordinatorLayout,
                "Movie added to favorites",
                Snackbar.LENGTH_SHORT
        ).show();
    }

    @Override
    public void showMovieUnfavorited() {
        showFavoriteIcon(false);
        Snackbar.make(mMainCoordinatorLayout,
                "Movie removed from favorites",
                Snackbar.LENGTH_SHORT
        ).show();
    }

    @Override
    public void showMovieDetail(MovieEntity movie) {
        mContentLayoutView.setVisibility(View.VISIBLE);
        displayMovieInfo(movie);
    }

    @Override
    public void showReviews(List<ReviewEntity> reviews) {
        mReviewAdapter.setReviewList(reviews);
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
        // Update favorite icon
        showFavoriteIcon(movieInfo.isFavorite());
    }

    private void showFavoriteIcon(boolean isFavorite) {
        Timber.i("showFavoriteIcon is called");
        if (isFavorite) mFavoriteFab.setImageResource(R.drawable.ic_favorite);
        else mFavoriteFab.setImageResource(R.drawable.ic_unfavorite);
        if (mFavoriteFab.getDrawable() == null) {
            Timber.i("No drawable set!");
        }
    }
}
