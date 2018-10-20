package com.techbeloved.moviesbeloved.movies;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.utils.ActivityUtils;
import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

import javax.inject.Inject;

public class MoviesActivity extends DaggerAppCompatActivity {

    @Inject
    Lazy<MoviesFragment> moviesFragmentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MoviesFragment moviesFragment =
                (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (moviesFragment == null) {
            // Create the fragment
            moviesFragment = moviesFragmentProvider.get();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), moviesFragment, R.id.contentFrame
            );
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        outState.putSerializable(CURRENT_FILTERING_KEY, mMoviesPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }
}
