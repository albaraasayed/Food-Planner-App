package com.example.foodplannerapp.data.remote.remote_datasource_interface;

import com.example.foodplannerapp.model.CategoryResponse;
import com.example.foodplannerapp.model.CountryResponse;
import com.example.foodplannerapp.model.IngredientResponse;
import com.example.foodplannerapp.model.MealResponse;
import io.reactivex.rxjava3.core.Single;

public interface MealRemoteDataSource {
    Single<MealResponse> getRandomMeal();
    Single<CategoryResponse> getCategories();
    Single<CountryResponse> getCountries();
    Single<MealResponse> searchMeals(String query);
    Single<MealResponse> filterByCategory(String category);
    Single<MealResponse> filterByArea(String area);
    Single<IngredientResponse> getIngredients();
    Single<MealResponse> filterByIngredient(String ingredient);

    // NEW
    Single<MealResponse> getMealDetails(String id);
}