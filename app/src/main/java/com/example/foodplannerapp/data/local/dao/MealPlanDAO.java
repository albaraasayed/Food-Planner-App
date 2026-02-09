package com.example.foodplannerapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplannerapp.model.MealPlan;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable; // Changed from Flowable

@Dao
public interface MealPlanDAO {
    @Query("SELECT * FROM meal_plan_table")
    Observable<List<MealPlan>> getAllPlannedMeals();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMealPlan(MealPlan mealPlan);

    @Delete
    Completable deleteMealPlan(MealPlan mealPlan);
}