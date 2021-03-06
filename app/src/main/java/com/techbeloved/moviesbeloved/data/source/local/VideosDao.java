package com.techbeloved.moviesbeloved.data.source.local;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface VideosDao {

    @Query("SELECT * FROM videos WHERE movie_id = :movieId")
    Flowable<List<VideoEntity>> getVideos(int movieId);

    @Query("SELECT * FROM videos WHERE video_id = :videoId")
    LiveData<VideoEntity> getVideoById(String videoId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideo(VideoEntity video);

    @Update
    int updateVideo(VideoEntity video);

    @Query("DELETE FROM videos WHERE video_id = :videoId")
    int deleteVideoById(String videoId);

    @Query("DELETE FROM videos WHERE movie_id = :movieId")
    void deleteVideos(int movieId);

    @Query("DELETE FROM videos")
    void deleteAllVideos();
}
