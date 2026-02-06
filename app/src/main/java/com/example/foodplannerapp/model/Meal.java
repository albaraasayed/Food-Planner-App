package com.example.foodplannerapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "meals_table")
public class Meal implements Serializable {

    @PrimaryKey
    @NonNull
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

    // Default Constructor (Required for Room)
    public Meal() {
    }

    // ... (Keep your existing Getters and Setters) ...
    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }
}