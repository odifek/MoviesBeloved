package com.techbeloved.moviesbeloved.data.models;

import java.util.Date;
import java.util.List;

public interface Movie {
    int getId();
    String getTitle();
    String getPosterUrl();
    String getBackdropUrl();
    String getSynopsis();
    float getUserRating();
    Date getReleaseDate();
    List<String> getGenres();
    boolean isFavorite();

    void setPosterUrl(String posterUrl);
    void setBackdropUrl(String backdropUrl);
    void setSynopsis(String synopsis);
    void setUserRating(float userRating);
    void setReleaseDate(Date releaseDate);
    void setGenres(List<String> genres);
    void setFavorite(boolean favorite);
    void setId(int id);
    void setTitle(String title);
}
