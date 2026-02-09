package com.example.foodplannerapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "meal_plan_table")
public class MealPlan implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;

    private String mealId;
    private String mealName;
    private String mealThumb;
    private String mealArea;
    private String day;

    public MealPlan() {
    }

    public MealPlan(String day, String mealId, String mealName, String mealThumb, String mealArea) {
        this.day = day;
        this.mealId = mealId;
        this.id = day + "_" + mealId;
        this.mealName = mealName;
        this.mealThumb = mealThumb;
        this.mealArea = mealArea;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
        if (this.day != null) this.id = this.day + "_" + mealId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
        if (this.mealId != null) this.id = day + "_" + this.mealId;
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
}