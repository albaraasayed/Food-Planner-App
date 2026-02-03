package com.example.foodplannerapp.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("idIngredient")
    private String id;

    @SerializedName("strIngredient")
    private String name;

    @SerializedName("strDescription")
    private String description;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    // Helper to get Ingredient Image URL
    public String getThumbnail() {
        return "https://www.themealdb.com/images/ingredients/" + name + "-Small.png";
    }
}