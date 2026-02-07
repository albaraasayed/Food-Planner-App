package com.example.foodplannerapp.data.config;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Make sure your imports match your package structure
import com.example.foodplannerapp.data.local.dao.MealDAO;     // Updated package based on your file
import com.example.foodplannerapp.data.local.dao.MealPlanDAO; // Updated package based on your file
import com.example.foodplannerapp.model.Meal;
import com.example.foodplannerapp.model.MealPlan;

// 1. Add MealPlan.class
// 2. Change version to 2
@Database(entities = {Meal.class, MealPlan.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance = null;

    // 3. Keep only ONE definition of each DAO
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