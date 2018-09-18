package com.techbeloved.moviesbeloved.models;

import java.util.Date;

public interface Movie {
    String getTitle();
    String getPosterUrl();
    String getSynopsis();
    String getUserRating();
    Date getReleaseDate();
}
