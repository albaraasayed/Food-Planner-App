package com.example.foodplannerapp.network;

import com.example.foodplannerapp.model.CategoryResponse;
import com.example.foodplannerapp.model.CountryResponse;
import com.example.foodplannerapp.model.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {

    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("search.php")
    Single<MealResponse> searchMeal(@Query("s") String mealName);

    @GET("categories.php")
    Single<CategoryResponse> getCategories();

    @GET("filter.php")
    Single<MealResponse> getMealsByCountry(@Query("a") String country);

    @GET("list.php?a=list")
    Single<CountryResponse> getCountries();
}