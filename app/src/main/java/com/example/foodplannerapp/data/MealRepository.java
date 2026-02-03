package com.example.foodplannerapp.data;

import com.example.foodplannerapp.model.CategoryResponse;
import com.example.foodplannerapp.model.CountryResponse;
import com.example.foodplannerapp.model.MealResponse;
import com.example.foodplannerapp.network.MealService;

import io.reactivex.rxjava3.core.Single;

public class MealRepository {

    private static MealRepository instance = null;
    private final MealService mealService;

    private MealRepository(MealService mealService) {
        this.mealService = mealService;
    }

    public static MealRepository getInstance(MealService mealService) {
        if (instance == null) {
            instance = new MealRepository(mealService);
        }
        return instance;
    }

    public Single<MealResponse> getRandomMeal() {
        return mealService.getRandomMeal();
    }

    public Single<CategoryResponse> getCategories() {
        return mealService.getCategories();
    }

    public Single<CountryResponse> getCountries() {
        return mealService.getCountries();
    }

    public Single<MealResponse> searchMeals(String query) {
        return mealService.searchMeals(query);
    }

    public Single<MealResponse> filterByCategory(String category) {
        return mealService.filterByCategory(category);
    }
    public Single<MealResponse> filterByArea(String area) {
        return mealService.filterByArea(area);
    }
}