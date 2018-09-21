package com.techbeloved.moviesbeloved.data.source.remote;

import com.techbeloved.moviesbeloved.MovieFilterType;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.source.MoviesDataSource;

import java.util.List;

import androidx.annotation.NonNull;


public class MoviesRemoteDataSource implements MoviesDataSource {

    private static MoviesRemoteDataSource INSTANCE;

    private MovieRemoteDao mMovieRemoteDao;

    public static MoviesRemoteDataSource getInstance(MovieRemoteDao movieRemoteDao) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRemoteDataSource(movieRemoteDao);
        }
        return INSTANCE;
    }

    // Prevent instantiation
    private MoviesRemoteDataSource(MovieRemoteDao movieRemoteDao) {
        mMovieRemoteDao = movieRemoteDao;
    }

    @Override
    public void getMovies(MovieFilterType filterType, @NonNull final LoadMoviesCallback callback) {
        // The first page is requested
        int page = 1;
        mMovieRemoteDao.getMovies(filterType, page, new LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<MovieEntity> movies) {
                callback.onMoviesLoaded(movies);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getMovies(MovieFilterType filterType, int page, @NonNull final LoadMoviesCallback callback) {
        mMovieRemoteDao.getMovies(filterType, page, new LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<MovieEntity> movies) {
                callback.onMoviesLoaded(movies);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getMovie(int movieId, @NonNull final GetMovieCallback callback) {
        mMovieRemoteDao.getMovie(movieId, new GetMovieCallback() {
            @Override
            public void onMovieLoaded(MovieEntity movie) {
                callback.onMovieLoaded(movie);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveMovie(@NonNull MovieEntity movie) {
        // Not used in remote server
    }

    @Override
    public void deleteAllMovies() {
        // Not applicable
    }

    @Override
    public void deleteMovie(int movieId) {
        // Not applicable
    }

//    private void getPopularMovies(String sortCriteria, int page) {
//        Uri.Builder builder = Uri.parse(TMDB_API_BASE_URL).buildUpon();
//        builder.appendPath(MOVIE_PATH_SEG)
//                .appendPath(sortCriteria)
//                .appendQueryParameter(API_KEY_QUERY_PARAM, TMDB_API_KEY)
//                .appendQueryParameter(PAGE_QUERY_PARAM, String.valueOf(page));
//        String requestUrl = builder.build().toString();
//
//        // Process with volley
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                List<Movie> movieList = new ArrayList<>();
//                try {
//                    JSONArray resultsArray = response.getJSONArray("results");
//                    for (int i = 0; i < resultsArray.length(); i++) {
//                        JSONObject jsonMovie = resultsArray.getJSONObject(i);
//                        Movie movieInfo = MovieUtils.createMovieModel(jsonMovie);
//                        if (movieInfo != null) {
//                            movieList.add(movieInfo);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                // Update list
//                mMoviesAdapter.setMovieList(movieList);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        // Add the request to the RequestQueue
//        queue.add(jsonObjectRequest);
//    }
}
