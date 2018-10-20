package com.techbeloved.moviesbeloved.data.source.remote.api;

import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBMovieService {
    @GET("/3/discover/movie?language=en-US&sort_by=popularity.desc&include_adult=false&include_video=true")
    Flowable<MoviesWrapper> getPopularMovies(@Query("page") int page);

    @GET("/3/discover/movie?language=en-US&sort_by=release_date.desc&include_adult=false&include_video=true")
    Flowable<MoviesWrapper> getLatestMovies(@Query("release_date.lte") String maxReleaseDate, @Query("page") int page);

    @GET("/3/movie/top_rated")
    Flowable<MoviesWrapper> getHighestRatedMovies(@Query("page") int page);

    @GET("/3/movie/{movie_id}")
    Single<MovieEntity> getMovie(@Path("movie_id") int movieId);

    @GET("/3/movie/{movie_id}/reviews")
    Flowable<ReviewsWrapper> getReviews(@Path("movie_id") int movieId);

    @GET("/3/movie/{movie_id}/videos")
    Flowable<VideosWrapper> getVideos(@Path("movie_id") int movieId);
}
