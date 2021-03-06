package com.techbeloved.moviesbeloved.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;

@Database(entities = {MovieEntity.class, ReviewEntity.class, VideoEntity.class}, version = 4, exportSchema = false)
@TypeConverters({DateConverter.class, ListConverter.class})
public abstract class FavoriteDatabase extends RoomDatabase {
    private static FavoriteDatabase INSTANCE;

    public abstract MoviesDao moviesDao();

    public abstract ReviewsDao reviewsDao();

    public abstract VideosDao videosDao();
}
