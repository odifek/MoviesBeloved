package com.techbeloved.moviesbeloved.data.source.remote;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.*;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;
import com.techbeloved.moviesbeloved.utils.MovieUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.techbeloved.moviesbeloved.utils.Constants.*;

public class TMDBMovies implements MovieRemoteDao {

    private static TMDBMovies INSTANCE;


    private MoviesRequestQueue mRequestQueue;

    /**
     * We are going to use injection to construct A RemoteDataSource
     * We are going to do something like this
     *  public static MoviesRepository provideMoviesRepository(@NonNull Context context) {
     *      FavoriteDatabase database = FavoriteDatabase.getInstance(context);
     *      TMDBMovies mDBMovies = TMDBMovies.getInstance(MoviesRequestQueue.getInstance(context))
     *      return MoviesRepository.getInstance(MoviesRemoteDataSource.getInstance(mDBMovies),
     *                              MoviesLocalDataSource.getInstance(new AppExecutors(), database.taskDao()));
     *  }
     * @param requestQueue is passed by injection
     * @return singleton instance
     */
    public static TMDBMovies getInstance(MoviesRequestQueue requestQueue) {
        if (INSTANCE == null) {
            INSTANCE = new TMDBMovies(requestQueue);
        }
        return INSTANCE;
    }

    private TMDBMovies(MoviesRequestQueue requestQueue) {
        mRequestQueue = requestQueue;
    }

    @Override
    public void getMovies(MovieFilterType filterType, int page, final MoviesDataSource.LoadMoviesCallback callback) {
        String filter = "";
        switch (filterType) {
            case POPULAR:
                filter = FILTER_POPULAR_PATH;
                break;
            case TOP_RATED:
                filter = FILTER_TOP_RATED_PATH;
                break;
            default:
                filter = FILTER_POPULAR_PATH;
        }

        // Build the Uri
        Uri.Builder builder = Uri.parse(TMDB_API_BASE_URL).buildUpon();
        builder.appendPath(MOVIE_PATH_SEG)
                .appendPath(filter)
                .appendQueryParameter(API_KEY_QUERY_PARAM, TMDB_API_KEY)
                .appendQueryParameter(PAGE_QUERY_PARAM, String.valueOf(page));
        String requestUrl = builder.build().toString();

        // Process with volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<MovieEntity> movieList = new ArrayList<>();
                try {
                    JSONArray resultsArray = response.getJSONArray(RESULTS_JSON_ARRAY_KEY);
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
                callback.onMoviesLoaded(movieList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onDataNotAvailable();
            }
        });

        // Add the request to the RequestQueue
        mRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void getMovie(int movieId, final MoviesDataSource.GetMovieCallback callback) {

        Uri.Builder builder = Uri.parse(TMDB_API_BASE_URL).buildUpon();
        builder.appendPath(MOVIE_PATH_SEG)
                .appendPath(String.valueOf(movieId))
                .appendQueryParameter(API_KEY_QUERY_PARAM, TMDB_API_KEY);
        String requestUrl = builder.build().toString();

        // Process with volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MovieEntity movieInfo = MovieUtils.createMovieModel(response);
                // reply with the movie info
                if (movieInfo != null) {
                    callback.onMovieLoaded(movieInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue
        mRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Takes care of querying the api for the reviews of a given movie.
     * @param movieId is the id of the movie we want to see the reviews
     * @param page is the number for the particular page
     * @param callback returns the result to whoever cares to listen
     */
    @Override
    public void getReviews(final int movieId, int page, final MoviesDataSource.LoadReviewsCallback callback) {
        Uri.Builder builder = Uri.parse(TMDB_API_BASE_URL).buildUpon();
        builder.appendPath(MOVIE_PATH_SEG)
                .appendPath(String.valueOf(movieId))
                .appendPath(REVIEWS_PATH_SEGMENT)
                .appendQueryParameter(API_KEY_QUERY_PARAM, TMDB_API_KEY)
                .appendQueryParameter(PAGE_QUERY_PARAM, String.valueOf(page));
        String requestUrl = builder.build().toString();

        // Process with volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<ReviewEntity> reviewList = new ArrayList<>();
                try {
                    JSONArray resultsArray = response.getJSONArray(RESULTS_JSON_ARRAY_KEY);
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject jsonReview = resultsArray.getJSONObject(i);
                        ReviewEntity review = MovieUtils.createReviewModel(movieId, jsonReview);
                        if (review != null) {
                            reviewList.add(review);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update list
                callback.onReviewsLoaded(reviewList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onDataNotAvailable();
            }
        });

        // Add the request to the RequestQueue
        mRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Takes care of querying the api for video information available for the particular movie
     * @param movieId is the movie id
     * @param callback returns the result
     */
    @Override
    public void getVideos(final int movieId, final MoviesDataSource.LoadVideosCallback callback) {
        Uri.Builder builder = Uri.parse(TMDB_API_BASE_URL).buildUpon();
        builder.appendPath(MOVIE_PATH_SEG)
                .appendPath(String.valueOf(movieId))
                .appendPath(VIDEOS_PATH_SEGMENT)
                .appendQueryParameter(API_KEY_QUERY_PARAM, TMDB_API_KEY);
        String requestUrl = builder.build().toString();

        // Process with volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<VideoEntity> videoList = new ArrayList<>();
                try {
                    JSONArray resultsArray = response.getJSONArray(RESULTS_JSON_ARRAY_KEY);
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject jsonVideo = resultsArray.getJSONObject(i);
                        VideoEntity video = MovieUtils.createVideoModel(movieId, jsonVideo);
                        if (video != null) {
                            videoList.add(video);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update list
                callback.onVideosLoaded(videoList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onDataNotAvailable();
            }
        });

        // Add the request to the RequestQueue
        mRequestQueue.addToRequestQueue(jsonObjectRequest);
    }
}
