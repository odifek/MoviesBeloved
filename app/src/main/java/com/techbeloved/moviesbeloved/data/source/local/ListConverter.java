package com.techbeloved.moviesbeloved.data.source.local;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

public class ListConverter {
    @TypeConverter
    public static List<String> toStringList(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(jsonString, type);
    }

    @TypeConverter
    public static String toJsonString(List<String> stringList) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.toJson(stringList, type);
    }
}
