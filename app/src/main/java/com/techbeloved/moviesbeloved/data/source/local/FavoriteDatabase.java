package com.techbeloved.moviesbeloved.data.source.local;

import android.content.Context;

import com.techbeloved.moviesbeloved.data.models.Movie;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Movie.class}, version = 1)
@TypeConverters({DateConverter.class, ListConverter.class})
public abstract class FavoriteDatabase extends RoomDatabase {
    private static FavoriteDatabase INSTANCE;

    public abstract MovieDao movieDao();

    private static final Object sLock = new Object();

    public static FavoriteDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteDatabase.class, "Movies.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}
