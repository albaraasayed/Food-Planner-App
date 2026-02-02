package com.example.foodplannerapp.data;

import com.example.foodplannerapp.model.CategoryResponse;
import com.example.foodplannerapp.model.CountryResponse;
import com.example.foodplannerapp.model.MealResponse;
import com.example.foodplannerapp.network.MealService;
import com.example.foodplannerapp.network.RetrofitClient;

import io.reactivex.rxjava3.core.Single;

public class MealRepository {

    private static MealRepository instance = null;
    private final MealService mealService;

    // Private constructor for Singleton
    private MealRepository(MealService mealService) {
        this.mealService = mealService;
    }

    public static MealRepository getInstance(MealService mealService) {
        if (instance == null) {
            instance = new MealRepository(mealService);
        }
        return instance;
    }

    // --- Remote Data Calls ---

    public Single<MealResponse> getRandomMeal() {
        return mealService.getRandomMeal();
    }

    public Single<CategoryResponse> getCategories() {
        return mealService.getCategories();
    }

    public Single<CountryResponse> getCountries() {
        // Note: The API endpoint for countries is typically "list.php?a=list"
        // Ensure your Retrofit interface has the correct @GET path
        return mealService.getCountries();
    }
}