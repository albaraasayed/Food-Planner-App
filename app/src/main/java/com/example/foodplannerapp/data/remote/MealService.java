package com.example.foodplannerapp.data.remote;

import com.example.foodplannerapp.model.CategoryResponse;
import com.example.foodplannerapp.model.CountryResponse;
import com.example.foodplannerapp.model.IngredientResponse;
import com.example.foodplannerapp.model.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {

    // --- EXISTING ---
    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("categories.php")
    Single<CategoryResponse> getCategories();

    @GET("list.php?a=list")
    Single<CountryResponse> getCountries();

    // --- NEW: ADD THESE FOR SEARCH ---

    // Search by Name: https://www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
    @GET("search.php")
    Single<MealResponse> searchMeals(@Query("s") String query);

    // Filter by Category: https://www.themealdb.com/api/json/v1/1/filter.php?c=Seafood
    @GET("filter.php")
    Single<MealResponse> filterByCategory(@Query("c") String category);

    @GET("filter.php")
    Single<MealResponse> filterByArea(@Query("a") String area);

    @GET("list.php?i=list")
    Single<IngredientResponse> getIngredients();

    @GET("filter.php")
    Single<MealResponse> filterByIngredient(@Query("i") String ingredient);
    @GET("lookup.php")
    Single<MealResponse> getMealDetails(@Query("i") String id);
}