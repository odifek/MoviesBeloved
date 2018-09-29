package com.techbeloved.moviesbeloved.utils;

import com.techbeloved.moviesbeloved.BuildConfig;

public class Constants {
    public static final String MOVIE_ID_EXTRA = "movie_id";
    public static final String TMDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String TMDB_API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String DEFAULT_POSTER_SIZE = "w185";
    public static final String DEFAULT_BACKDROP_SIZE = "w780";
    public static final String TMDB_API_KEY = BuildConfig.TmdbApiKey;
    public static final String PAGE_QUERY_PARAM = "page";
    public static final String API_KEY_QUERY_PARAM = "api_key";
    public static final String MOVIE_PATH_SEG = "movie";
    public static final String REVIEWS_PATH_SEGMENT = "reviews";
    public static final String VIDEOS_PATH_SEGMENT = "videos";
    public static final int DEFAULT_MOVIES_PAGE = 1;

    public static final String FILTER_POPULAR_PATH = "popular";
    public static final String FILTER_TOP_RATED_PATH = "top_rated";
    public static final String RESULTS_JSON_ARRAY_KEY = "results";
    public static final String YOUTUBE_VIDEO_ID = "video_id";
    public static final String YOUTUBE_DEVELOPER_API_KEY = BuildConfig.YoutubeApiKey;
}
