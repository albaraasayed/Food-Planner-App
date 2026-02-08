package com.example.foodplannerapp.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("idIngredient")
    private String id;

    @SerializedName("strIngredient")
    private String name;

    @SerializedName("strDescription")
    private String description;

    @SerializedName("strMeasure")
    private String measure;

    // 1. Add this field to store the custom URL we create in MealDetailsFragment
    private String thumbnail;

    public void setName(String name) {
        this.name = name;
    }

    // 2. Add this Setter
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // 3. Update Getter to return our custom thumbnail if it exists
    public String getThumbnail() {
        if (thumbnail != null && !thumbnail.isEmpty()) {
            return thumbnail;
        }
        // Fallback for API responses where we don't set it manually
        return "https://www.themealdb.com/images/ingredients/" + name + "-Small.png";
    }
}