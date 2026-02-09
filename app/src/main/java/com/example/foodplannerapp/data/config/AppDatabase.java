package com.example.foodplannerapp.data.config;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodplannerapp.data.local.dao.MealDAO;
import com.example.foodplannerapp.data.local.dao.MealPlanDAO;
import com.example.foodplannerapp.model.Meal;
import com.example.foodplannerapp.model.MealPlan;

@Database(entities = {Meal.class, MealPlan.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance = null;
    public abstract MealDAO mealDAO();
    public abstract MealPlanDAO mealPlanDAO();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "food_planner_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}