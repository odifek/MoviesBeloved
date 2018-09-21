package com.techbeloved.moviesbeloved.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.techbeloved.moviesbeloved.Injection;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.utils.ActivityUtils;

public class MoviesActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private MoviesPresenter mMoviesPresenter;

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
            moviesFragment = MoviesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), moviesFragment, R.id.contentFrame
            );

            // Create the presenter
            mMoviesPresenter = new MoviesPresenter(
                    Injection.provideMoviesRepository(getApplicationContext()),
                    moviesFragment
            );

        }
    }
}
