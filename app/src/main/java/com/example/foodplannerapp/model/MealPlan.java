package com.example.foodplannerapp.model;

import androidx.room.Entity;
import androidx.room.Ignore; // Import Ignore
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "meal_plan_table")
public class MealPlan implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String mealId;
    private String mealName;
    private String mealThumb;
    private String mealArea;
    private String day; // Changed 'date' to 'day' to match your Fragment code

    // --- FIX 1: Add Empty Constructor (Required for Room & new MealPlan()) ---
    public MealPlan() {
    }

    // --- FIX 2: Add @Ignore to the convenience constructor ---
    @Ignore
    public MealPlan(String mealId, String mealName, String mealThumb, String mealArea, String day) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealThumb = mealThumb;
        this.mealArea = mealArea;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealThumb() {
        return mealThumb;
    }

    public void setMealThumb(String mealThumb) {
        this.mealThumb = mealThumb;
    }

    public String getMealArea() {
        return mealArea;
    }

    public void setMealArea(String mealArea) {
        this.mealArea = mealArea;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}