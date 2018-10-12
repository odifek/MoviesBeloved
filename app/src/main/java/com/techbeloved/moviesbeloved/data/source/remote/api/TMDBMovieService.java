package com.techbeloved.moviesbeloved.data.source.remote.api;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBMovieService {
    @GET("/3/movie/popular")
    Flowable<MoviesWrapper> getPopularMovies(@Query("page") int page);
}
