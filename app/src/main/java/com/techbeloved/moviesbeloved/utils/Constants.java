package com.techbeloved.moviesbeloved.utils;

public class Constants {
    public static final String MOVIE_ID = "movie_id";
    public static final String TMDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String TMDB_API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String DEFAULT_POSTER_SIZE = "w185";
    public static final String DEFAULT_BACKDROP_SIZE = "w780";
    public static final String TMDB_API_KEY = "b8eb7211b6e1882884bdf92f1d94961a";
    public static final String POPULAR_PATH_SEG = "popular";
    public static final String PAGE_QUERY_PARAM = "page";
    public static final String API_KEY_QUERY_PARAM = "api_key";
    public static final String MOVIE_PATH_SEG = "movie";

    public enum SortBy {
        POPULARITY,
        USER_RATING
    }
}
