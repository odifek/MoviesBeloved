package com.techbeloved.moviesbeloved.data.models;

import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "videos", foreignKeys =
    @ForeignKey(entity = MovieEntity.class, parentColumns = "movie_id",
            childColumns = "movie_id", onDelete = CASCADE))
public class VideoEntity implements Video {

    @PrimaryKey
    @ColumnInfo(name = "video_id")
    private String id;
    @ColumnInfo(name = "movie_id")
    private int  movieId;
    private String key;
    private String name;
    private String site;
    private String type;

    @Ignore
    public VideoEntity(int movieId, String id) {
        this.movieId = movieId;
        this.id = id;
    }

    public VideoEntity(int movieId, String id, String key, String name, String site, String type) {
        this.movieId = movieId;
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getMovieId() {
        return this.movieId;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSite() {
        return this.site;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
