package com.techbeloved.moviesbeloved.data.source.remote.api;

import com.google.gson.annotations.SerializedName;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import java.util.List;

public class MoviesWrapper {
    @SerializedName("results")
    private List<MovieEntity> movieList;

    public List<MovieEntity> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<MovieEntity> movieList) {
        this.movieList = movieList;
    }
}
