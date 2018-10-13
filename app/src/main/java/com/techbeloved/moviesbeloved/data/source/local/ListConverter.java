package com.techbeloved.moviesbeloved.data.source.local;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techbeloved.moviesbeloved.data.models.Genre;

import java.lang.reflect.Type;
import java.util.List;

public class ListConverter {
    @TypeConverter
    public static List<Genre> toGenreList(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Genre>>() {
        }.getType();
        return gson.fromJson(jsonString, type);
    }

    @TypeConverter
    public static String toJsonString(List<Genre> genreList) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Genre>>() {
        }.getType();
        return gson.toJson(genreList, type);
    }
}
