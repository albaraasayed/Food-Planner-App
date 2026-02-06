package com.example.foodplannerapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable; // Important: Implement Serializable to pass objects between fragments

public class Meal implements Serializable {

    @SerializedName("idMeal")
    private String id;

    @SerializedName("strMeal")
    private String name;

    @SerializedName("strArea")
    private String area;

    @SerializedName("strCategory")
    private String category;

    @SerializedName("strInstructions")
    private String instructions;

    @SerializedName("strMealThumb")
    private String thumbUrl;

    @SerializedName("strYoutube")
    private String youtubeUrl;


    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }
}