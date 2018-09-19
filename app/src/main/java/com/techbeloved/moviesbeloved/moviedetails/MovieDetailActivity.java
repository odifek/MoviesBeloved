package com.techbeloved.moviesbeloved.moviedetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.models.Movie;
import com.techbeloved.moviesbeloved.utils.MovieUtils;

import org.json.JSONObject;

import static com.techbeloved.moviesbeloved.utils.Constants.API_KEY_QUERY_PARAM;
import static com.techbeloved.moviesbeloved.utils.Constants.MOVIE_ID;
import static com.techbeloved.moviesbeloved.utils.Constants.MOVIE_PATH_SEG;
import static com.techbeloved.moviesbeloved.utils.Constants.TMDB_API_BASE_URL;
import static com.techbeloved.moviesbeloved.utils.Constants.TMDB_API_KEY;
import static com.techbeloved.moviesbeloved.utils.MovieUtils.getYearFromDate;

public class MovieDetailActivity extends AppCompatActivity {

    private int mCurrentMovieId;

    private CollapsingToolbarLayout mCollapsingToolbar;
    private TextView mSynopsisText;
    private ImageView mBackdropImage;
    private ImageView mPosterImage;
    private TextView mYearText;
    private TextView mRatingText;
    private TextView mTitleText;
    private RatingBar mRatingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent movieIntent = getIntent();
        if (movieIntent.hasExtra(MOVIE_ID)) {
            mCurrentMovieId = movieIntent.getIntExtra(MOVIE_ID, 0);
        } else {
            finish();
            return;
        }

        setupViews();

        loadMovieDetails(mCurrentMovieId);

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = (appBarLayout.getTotalScrollRange() - (float)Math.abs(verticalOffset))/appBarLayout.getTotalScrollRange();
                if (percentage < 0.3) {
                    mPosterImage.setVisibility(View.GONE);
                } else if (percentage > 0.7){
                    mPosterImage.setVisibility(View.VISIBLE);
                }
            }
        });
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
    }

    private void displayMovieInfo(Movie movieInfo) {
        mTitleText.setText(movieInfo.getTitle());
        mSynopsisText.setText(movieInfo.getSynopsis());
        mCollapsingToolbar.setTitle(movieInfo.getTitle());

        String year = getYearFromDate(movieInfo.getReleaseDate());
        mYearText.setText(year);

        mRatingText.setText(String.valueOf(movieInfo.getUserRating()));

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

    private void loadMovieDetails(int currentMovieId) {
        Uri.Builder builder = Uri.parse(TMDB_API_BASE_URL).buildUpon();
        builder.appendPath(MOVIE_PATH_SEG)
                .appendPath(String.valueOf(currentMovieId))
                .appendQueryParameter(API_KEY_QUERY_PARAM, TMDB_API_KEY);
        String requestUrl = builder.build().toString();

        // Process with volley
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Movie movieInfo = MovieUtils.createMovieModel(response);
                // TODO: 9/19/18 Update the database with it also
                // Display the movie info
                if (movieInfo != null) {
                    displayMovieInfo(movieInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue
        queue.add(jsonObjectRequest);
    }
}
