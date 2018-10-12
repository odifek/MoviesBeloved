package com.techbeloved.moviesbeloved.data.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Entity(tableName = "movies")
public class MovieEntity implements Movie {

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    @SerializedName("id")
    private int id;
    @Nullable
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "synopsis")
    @SerializedName("overview")
    private String synopsis;

    @ColumnInfo(name = "poster_url")
    private String posterUrl;

    @Ignore
    @SerializedName("poster_path")
    private String posterPath;

    @ColumnInfo(name = "backdrop_url")
    @SerializedName("backdrop_url")
    private String backdropUrl;

    @ColumnInfo(name = "genres")
    private List<String> genres;

    @ColumnInfo(name = "average_rating")
    @SerializedName("vote_average")
    private float userRating;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private Date releaseDate;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    @ColumnInfo(name = "runtime")
    private int runtime;

    public MovieEntity() {

    }

    @Ignore
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
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    @Override
    public void setSynopsis(@Nullable String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public void setPosterUrl(@Nullable  String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public void setBackdropUrl(@Nullable String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    @Override
    public void setGenres(@Nullable List<String> genres) {
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
    public void setReleaseDate(@Nullable Date releaseDate) {
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
    public int getRuntime() {
        return runtime;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @NonNull
    @Override
    public String toString() {
        return "Movie title: " + title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieEntity that = (MovieEntity) o;

        if (getId() != that.getId()) return false;
        if (getRuntime() != that.getRuntime()) return false;
        if (!getTitle().equals(that.getTitle())) return false;
        if (getSynopsis() != null ? !getSynopsis().equals(that.getSynopsis()) : that.getSynopsis() != null)
            return false;
        if (getPosterPath() != null ? !getPosterPath().equals(that.getPosterPath()) : that.getPosterPath() != null)
            return false;
        return getReleaseDate() != null ? getReleaseDate().equals(that.getReleaseDate()) : that.getReleaseDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + (getSynopsis() != null ? getSynopsis().hashCode() : 0);
        result = 31 * result + (getPosterPath() != null ? getPosterPath().hashCode() : 0);
        result = 31 * result + (getReleaseDate() != null ? getReleaseDate().hashCode() : 0);
        result = 31 * result + getRuntime();
        return result;
    }
}
