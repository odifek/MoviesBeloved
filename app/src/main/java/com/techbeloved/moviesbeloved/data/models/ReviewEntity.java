package com.techbeloved.moviesbeloved.data.models;


import androidx.annotation.NonNull;
import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "reviews", indices = @Index(value = {"movie_id", "review_id", "content"}),
        foreignKeys = @ForeignKey(entity = MovieEntity.class, parentColumns = "movie_id", childColumns = "movie_id", onDelete = CASCADE))
public class ReviewEntity implements Review {

    @PrimaryKey
    @ColumnInfo(name = "review_id")
    @NonNull
    private String id;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    private String author;
    private String content;
    private String url;

    @Ignore
    public ReviewEntity(int movieId, @NonNull String id) {
        this.id = id;
        this.movieId = movieId;
    }

    public ReviewEntity(int movieId, @NonNull String id, String author, String content, String url) {
        this.id = id;
        this.movieId = movieId;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getMovieId() {
        return movieId;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }
}
