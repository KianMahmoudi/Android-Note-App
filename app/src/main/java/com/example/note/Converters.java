package com.example.note;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String fromList(List<ListNoteItems> items) {
        Gson gson = new Gson();
        return gson.toJson(items);
    }

    @TypeConverter
    public static List<ListNoteItems> toList(String itemsString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ListNoteItems>>() {}.getType();
        return gson.fromJson(itemsString, listType);
    }
}
