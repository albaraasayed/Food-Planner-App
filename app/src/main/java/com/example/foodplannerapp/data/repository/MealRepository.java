package com.example.foodplannerapp.data.repository;

import com.example.foodplannerapp.model.CategoryResponse;
import com.example.foodplannerapp.model.CountryResponse;
import com.example.foodplannerapp.model.IngredientResponse;
import com.example.foodplannerapp.model.MealResponse;
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
}