package com.example.foodplannerapp.data.local.local_datasource_interface;

import com.example.foodplannerapp.model.Meal;
import com.example.foodplannerapp.model.MealPlan;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable; // Import Observable
import io.reactivex.rxjava3.core.Single;

public interface MealLocalDataSource {
    Completable insertFavorite(Meal meal);

    Completable deleteFavorite(Meal meal);

    Observable<List<Meal>> getFavorites();

    Completable insertMealPlan(MealPlan mealPlan);

    Completable deleteMealPlan(MealPlan mealPlan);

    Observable<List<MealPlan>> getAllPlans();
}