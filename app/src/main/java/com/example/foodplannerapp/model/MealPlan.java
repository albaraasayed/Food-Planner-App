package com.example.foodplannerapp.model;

import androidx.room.Entity;
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
    private String date;

    public MealPlan(String mealId, String mealName, String mealThumb, String mealArea, String date) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealThumb = mealThumb;
        this.mealArea = mealArea;
        this.date = date;
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

    public String getMealName() {
        return mealName;
    }

    public String getMealThumb() {
        return mealThumb;
    }

    public String getMealArea() {
        return mealArea;
    }

    public String getDate() {
        return date;
    }
}