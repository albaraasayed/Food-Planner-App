package com.example.foodplannerapp.data.repository;

import com.example.foodplannerapp.model.CategoryResponse;
import com.example.foodplannerapp.model.CountryResponse;
import com.example.foodplannerapp.model.IngredientResponse;
import com.example.foodplannerapp.model.Meal;
import com.example.foodplannerapp.model.MealPlan;
import com.example.foodplannerapp.model.MealResponse;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface MealRepository {

    Single<MealResponse> getRandomMeal();
    Single<CategoryResponse> getCategories();
    Single<CountryResponse> getCountries();
    Single<MealResponse> searchMeals(String query);
    Single<MealResponse> filterByCategory(String category);
    Single<MealResponse> filterByArea(String area);
    Single<IngredientResponse> getIngredients();
    Single<MealResponse> filterByIngredient(String ingredient);
    Single<MealResponse> getMealDetails(String id);


    Completable addToFavorites(Meal meal);
    Completable removeFromFavorites(Meal meal);
    Single<List<Meal>> getStoredFavorites();


    Completable addMealToPlan(MealPlan mealPlan);
    Completable removeMealFromPlan(MealPlan mealPlan);
    Single<List<MealPlan>> getPlan();

    void syncFromFirebase(String uid);
}