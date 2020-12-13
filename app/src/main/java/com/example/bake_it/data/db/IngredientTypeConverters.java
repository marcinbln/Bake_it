package com.example.bake_it.data.db;

import androidx.room.TypeConverter;

import com.example.bake_it.models.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

class IngredientTypeConverters {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<Ingredient> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Ingredient>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Ingredient> someObjects) {
        return gson.toJson(someObjects);
    }


}
