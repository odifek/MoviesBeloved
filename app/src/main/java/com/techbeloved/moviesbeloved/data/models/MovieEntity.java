package com.techbeloved.moviesbeloved.data.models;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class MovieEntity implements Movie {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movie_id")
    private int id;
    @Nullable
    @ColumnInfo
    private String title;
    @ColumnInfo
    private String synopsis;

    @ColumnInfo(name = "poster_url")
    private String posterUrl;

    @ColumnInfo(name = "backdrop_url")
    private String backdropUrl;

    @ColumnInfo(name = "genres")
    private List<String> genres;

    @ColumnInfo(name = "average_rating")
    private float userRating;

    @ColumnInfo(name = "release_date")
    private Date releaseDate;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    public MovieEntity() {

    }

    public MovieEntity(int id, String title) {
        this.id = id;
        this.title = title;
        // Not favorite by default;
        this.isFavorite = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(@Nullable String title) {
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
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    @Override
    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
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
    public List<String> getGenres() {
        return this.genres;
    }

    @Override
    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public String toString() {
        return "Movie title: " + title;
    }
}
