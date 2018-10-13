package com.techbeloved.moviesbeloved.moviedetails;

import androidx.appcompat.app.AppCompatActivity;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

//    private View.OnClickListener mFavFabOnclickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//           toggleFavorite();
//        }
//    };
//
//    private VideoClickCallback mVideoOnclickListener = new VideoClickCallback() {
//        @Override
//        public void onClick(Video video) {
//            Timber.i("video onClick was called!");
//            playVideo(video);
//        }
//    };
//
//    private ReviewAdapter mReviewAdapter;
//    private VideoAdapter mVideoAdapter;
//
//    private ActivityMovieDetailBinding mBinding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mBinding =
//                DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
//
//        Toolbar toolbar = mBinding.toolbar;
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        final Intent movieIntent = getIntent();
//        if (movieIntent.hasExtra(MOVIE_ID_EXTRA)) {
//            mCurrentMovieId = movieIntent.getIntExtra(MOVIE_ID_EXTRA, 0);
//        } else {
//            finish();
//            return;
//        }
//
//        // Create Presenter. Presenter is set in the setPresenter method
//        new MovieDetailPresenter(mCurrentMovieId,
//                Injection.provideMoviesRepository(getApplicationContext()),
//                this
//        );
//
//        setupViews();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mIsActive = true;
//        mPresenter.start();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mIsActive = false;
//    }
//
//    private void setupViews() {
//        mBinding.ratingBar.setMax(5);
//        mBinding.ratingBar.setStepSize(0.5f);
//
//        mBinding.favoriteFab.setOnClickListener(mFavFabOnclickListener);
//
//        mReviewAdapter = new ReviewAdapter();
//        mBinding.reviewRecyclerView.setAdapter(mReviewAdapter);
//
//        mVideoAdapter = new VideoAdapter(mVideoOnclickListener);
//        mBinding.videosRecyclerView.setAdapter(mVideoAdapter);
//
//    }
//
//    @Override
//    public void setLoadingIndicator(boolean active) {
//        mBinding.setIsLoading(active);
//    }
//
//    @Override
//    public void showMovieFavorited() {
//        showFavoriteIcon(true);
//        Snackbar.make(mBinding.mainCoordinatorLayout,
//                "Movie added to favorites",
//                Snackbar.LENGTH_SHORT
//        ).show();
//    }
//
//    @Override
//    public void showMovieUnfavorited() {
//        showFavoriteIcon(false);
//        Snackbar.make(mBinding.mainCoordinatorLayout,
//                "Movie removed from favorites",
//                Snackbar.LENGTH_SHORT
//        ).show();
//    }
//
//    @Override
//    public void showMovieDetail(MovieEntity movie) {
//        displayMovieInfo(movie);
//    }
//
//    @Override
//    public void showReviews(List<ReviewEntity> reviews) {
//        mReviewAdapter.setReviewList(reviews);
//    }
//
//    public void showVideos(List<VideoEntity> videos) {
//        mVideoAdapter.setVideoList(videos);
//    }
//
//    @Override
//    public boolean isActive() {
//        return mIsActive;
//    }
//
//    @Override
//    public void openPlayer(String youtubeKey) {
//        Intent youtubePlayerIntent = new Intent(this, YoutubePlayerActivity.class);
//        youtubePlayerIntent.putExtra(Constants.YOUTUBE_VIDEO_ID, youtubeKey);
//        startActivity(youtubePlayerIntent);
//    }
//
//    @Override
//    public void setPresenter(MovieDetailContract.Presenter presenter) {
//        mPresenter = presenter;
//    }
//
//    private void displayMovieInfo(Movie movieInfo) {
//        mBinding.setMovie(movieInfo);
//    }
//
//    private void showFavoriteIcon(boolean isFavorite) {
//        mBinding.setIsFavorite(isFavorite);
//    }
}
