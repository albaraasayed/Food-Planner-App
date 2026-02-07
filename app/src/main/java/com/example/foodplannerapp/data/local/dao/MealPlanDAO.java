package com.example.foodplannerapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.foodplannerapp.model.MealPlan;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealPlanDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMealPlan(MealPlan mealPlan);

    @Delete
    Completable deleteMealPlan(MealPlan mealPlan);

    @Query("SELECT * FROM meal_plan_table")
    Single<List<MealPlan>> getAllPlannedMeals();

    @Query("SELECT * FROM meal_plan_table WHERE date = :date")
    Single<List<MealPlan>> getMealsForDate(String date);
}