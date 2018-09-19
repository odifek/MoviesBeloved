package com.techbeloved.moviesbeloved;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.techbeloved.moviesbeloved.models.Movie;
import com.techbeloved.moviesbeloved.moviedetails.MovieDetailActivity;
import com.techbeloved.moviesbeloved.movies.MovieAdapter;
import com.techbeloved.moviesbeloved.movies.MovieClickCallback;
import com.techbeloved.moviesbeloved.utils.MovieUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.techbeloved.moviesbeloved.utils.Constants.*;

public class MainActivity extends AppCompatActivity {

    private MovieAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        RecyclerView mRecyclerView = findViewById(R.id.movie_list);

        mMoviesAdapter = new MovieAdapter(new MovieClickCallback() {
            @Override
            public void onClick(Movie movie) {
                // TODO: handle click events here

                Intent movieIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
                movieIntent.putExtra(MOVIE_ID, movie.getId());
                startActivity(movieIntent);
            }
        });
        mRecyclerView.setAdapter(mMoviesAdapter);

        getPopularMovies(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.popularity_item
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPopularMovies(int page) {
        Uri.Builder builder = Uri.parse(TMDB_API_BASE_URL).buildUpon();
        builder.appendPath(MOVIE_PATH_SEG)
                .appendPath(POPULAR_PATH_SEG)
                .appendQueryParameter(API_KEY_QUERY_PARAM, TMDB_API_KEY)
                .appendQueryParameter(PAGE_QUERY_PARAM, String.valueOf(page));
        String requestUrl = builder.build().toString();

        // Process with volley
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Movie> movieList = new ArrayList<>();
                try {
                    JSONArray resultsArray = response.getJSONArray("results");
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject jsonMovie = resultsArray.getJSONObject(i);
                        Movie movieInfo = MovieUtils.createMovieModel(jsonMovie);
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
