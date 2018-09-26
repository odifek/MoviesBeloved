package com.techbeloved.moviesbeloved.data.models;

public interface Review {
    String getId();
    int getMovieId();
    String getAuthor();
    String getContent();
    String getUrl();

    void setAuthor(String author);
    void setContent(String content);
    void setUrl(String url);
}
