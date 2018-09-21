package com.techbeloved.moviesbeloved.data.source.local;

import android.content.Context;

import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {MovieEntity.class}, version = 1)
@TypeConverters({DateConverter.class, ListConverter.class})
public abstract class FavoriteDatabase extends RoomDatabase {
    private static FavoriteDatabase INSTANCE;

    public abstract MoviesDao moviesDao();

    private static final Object sLock = new Object();

    public static FavoriteDatabase getInstance(Context context) {
        if (INSTANCE == null)
        synchronized (FavoriteDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteDatabase.class, "Movies.db")
                        .build();
            }
        }
        return INSTANCE;
    }
}
