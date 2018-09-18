package com.techbeloved.moviesbeloved;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.techbeloved.moviesbeloved.models.Movie;
import com.techbeloved.moviesbeloved.models.MovieImpl;
import com.techbeloved.moviesbeloved.movies.MovieAdapter;
import com.techbeloved.moviesbeloved.movies.MovieClickCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TMDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String TMDB_API_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String DEFAULT_POSTER_SIZE = "w185";
    private static final String TMDB_API_KEY = "b8eb7211b6e1882884bdf92f1d94961a";
    private static final String POPULAR_PATH_SEG = "popular";
    private static final String PAGE_QUERY_PARAM = "page";
    private static final String API_KEY_QUERY_PARAM = "api_key";
    private static final String MOVIE_PATH_SEG = "movie";

    private MovieAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.movie_list);

        mMoviesAdapter = new MovieAdapter(new MovieClickCallback() {
            @Override
            public void onClick(Movie movie) {
                // TODO: handle click events here
            }
        });
        mRecyclerView.setAdapter(mMoviesAdapter);

        getPopularMovies(1);
    }

    private Movie createMovieModel(JSONObject jsonMovie) {

        try {
            String title = jsonMovie.getString("title");
            int id = jsonMovie.getInt("id");
            float rating = (float) jsonMovie.getDouble("vote_average");
            String posterPath = jsonMovie.getString("poster_path");
            String synopsis = jsonMovie.getString("overview");
            String releaseDateString = jsonMovie.getString("release_date");

            Date releaseDate = new SimpleDateFormat("dd-MM-yyyy",
                    Locale.getDefault()).parse(releaseDateString);
            Movie movieInfo = new MovieImpl(id, title);
            movieInfo.setPosterUrl(buildPosterUrl(posterPath));
            movieInfo.setSynopsis(synopsis);
            movieInfo.setReleaseDate(releaseDate);
            movieInfo.setUserRating(rating);

            return movieInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String buildPosterUrl(String posterPath) {
        Uri.Builder uriBuilder = Uri.parse(TMDB_IMAGE_BASE_URL).buildUpon();
        uriBuilder.appendPath(DEFAULT_POSTER_SIZE)
                .appendEncodedPath(posterPath);
        return uriBuilder.build().toString();
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
                        Movie movieInfo = createMovieModel(jsonMovie);
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
