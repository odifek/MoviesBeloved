package com.techbeloved.moviesbeloved;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.moviedetails.MovieDetailActivity;
import com.techbeloved.moviesbeloved.movies.MovieAdapter;
import com.techbeloved.moviesbeloved.movies.MovieClickCallback;
import com.techbeloved.moviesbeloved.utils.EndlessScrollListener;
import com.techbeloved.moviesbeloved.utils.MovieUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.techbeloved.moviesbeloved.utils.Constants.*;

public class MainActivity extends AppCompatActivity {

    private MovieAdapter mMoviesAdapter;

    RecyclerView.OnScrollListener mOnScrollListener;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        mRecyclerView = findViewById(R.id.movie_list);

        mMoviesAdapter = new MovieAdapter(new MovieClickCallback() {
            @Override
            public void onClick(Movie movie) {
                // COMPLETED: handle click events here

                Intent movieIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
                movieIntent.putExtra(MOVIE_ID_EXTRA, movie.getId());
                startActivity(movieIntent);
            }
        });
        mRecyclerView.setAdapter(mMoviesAdapter);

        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        mOnScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getPopularMovies(getString(R.string.popular), page);
            }
        };
        mRecyclerView.setOnScrollListener(mOnScrollListener);

        // Sets the filtering using the value found in preferences
        int filterBy = getIntPreference(getApplicationContext(), getString(R.string.filter_by_pref_key));
        if (filterBy == getResources().getInteger(R.integer.popularity_filter)) {
            getPopularMovies(getString(R.string.popular), 1);
        } else if (filterBy == getResources().getInteger(R.integer.user_rating_filter)) {
            getPopularMovies(getString(R.string.top_rated), 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mOnScrollListener != null && mRecyclerView != null) {
//            mRecyclerView.removeOnScrollListener(mOnScrollListener);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_fragment_menu, menu);

        // Initialise menu items
        int filterPref = getIntPreference(getApplicationContext(), getString(R.string.filter_by_pref_key));
        if (filterPref == getResources().getInteger(R.integer.popularity_filter)) {
            menu.findItem(R.id.popularity_filter_menu).setChecked(true);
        } else if (filterPref == getResources().getInteger(R.integer.user_rating_filter)) {
            menu.findItem(R.id.top_rated_filter_menu).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Handles toggling of check state and saving the preference
            case R.id.popularity_filter_menu:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                saveIntPreference(getApplicationContext(),
                        getString(R.string.filter_by_pref_key),
                        getResources().getInteger(R.integer.popularity_filter));
                mMoviesAdapter.clear();
                getPopularMovies(getString(R.string.popular), 1);

                return true;
            case R.id.top_rated_filter_menu:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                saveIntPreference(getApplicationContext(),
                        getString(R.string.filter_by_pref_key),
                        getResources().getInteger(R.integer.user_rating_filter));
                mMoviesAdapter.clear();
                getPopularMovies(getString(R.string.top_rated), 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int getIntPreference(Context context, String pref_key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(pref_key, getResources().getInteger(R.integer.popularity_filter));
    }

    private void saveIntPreference(Context context, String pref_key,  int pref_value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putInt(pref_key, pref_value)
                .apply();
    }

    private void getPopularMovies(String sortCriteria, int page) {
        Uri.Builder builder = Uri.parse(TMDB_API_BASE_URL).buildUpon();
        builder.appendPath(MOVIE_PATH_SEG)
                .appendPath(sortCriteria)
                .appendQueryParameter(API_KEY_QUERY_PARAM, TMDB_API_KEY)
                .appendQueryParameter(PAGE_QUERY_PARAM, String.valueOf(page));
        String requestUrl = builder.build().toString();

        // Process with volley
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<MovieEntity> movieList = new ArrayList<>();
                try {
                    JSONArray resultsArray = response.getJSONArray("results");
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject jsonMovie = resultsArray.getJSONObject(i);
                        MovieEntity movieInfo = MovieUtils.createMovieModel(jsonMovie);
                        if (movieInfo != null) {
                            movieList.add(movieInfo);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update list
                mMoviesAdapter.setMovieList(movieList);
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
