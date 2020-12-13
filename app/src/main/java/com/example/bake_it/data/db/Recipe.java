package com.example.bake_it.data.db;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.bake_it.models.Ingredient;
import com.example.bake_it.models.Step;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "recipes")
public class Recipe {

    @SerializedName("steps")
    @Expose
    @Embedded
    @Ignore
    private final List<Step> steps = null;
    @SerializedName("ingredients")
    @Expose
    @TypeConverters(IngredientTypeConverters.class)
    public List<Ingredient> ingredients = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private Integer id;
    @SerializedName("servings")
    @Expose
    @Ignore
    private Integer servings;
    @SerializedName("image")
    @Expose
    @Ignore
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }


}


