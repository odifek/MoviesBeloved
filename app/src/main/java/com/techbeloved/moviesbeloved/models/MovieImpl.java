package com.techbeloved.moviesbeloved.models;

import java.util.Date;

public class MovieImpl implements Movie {

    private int id;
    private String title;
    private String synopsis;
    private String posterUrl;
    private String backdropUrl;
    private String[] genres;
    private float userRating;
    private Date releaseDate;

    public MovieImpl() {

    }

    public MovieImpl(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    @Override
    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    @Override
    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    @Override
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getPosterUrl() {
        return this.posterUrl;
    }

    @Override
    public String getBackdropUrl() {
        return this.backdropUrl;
    }

    @Override
    public String getSynopsis() {
        return this.synopsis;
    }

    @Override
    public float getUserRating() {
        return this.userRating;
    }

    @Override
    public Date getReleaseDate() {
        return this.releaseDate;
    }

    @Override
    public String[] getGenres() {
        return this.genres;
    }


}
