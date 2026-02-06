package com.example.foodplannerapp.data.remote.remote_datasource_implementation;

import com.example.foodplannerapp.data.remote.remote_datasource_interface.MealRemoteDataSource;
import com.example.foodplannerapp.data.remote.service.MealService;
import com.example.foodplannerapp.model.CategoryResponse;
import com.example.foodplannerapp.model.CountryResponse;
import com.example.foodplannerapp.model.IngredientResponse;
import com.example.foodplannerapp.model.MealResponse;
import io.reactivex.rxjava3.core.Single;

public class MealRemoteDataSourceImpl implements MealRemoteDataSource {

    private static MealRemoteDataSourceImpl instance = null;
    private final MealService mealService;

    private MealRemoteDataSourceImpl(MealService mealService) {
        this.mealService = mealService;
    }

    public static MealRemoteDataSourceImpl getInstance(MealService mealService) {
        if (instance == null) {
            instance = new MealRemoteDataSourceImpl(mealService);
        }
        return instance;
    }

    @Override
    public Single<MealResponse> getRandomMeal() {
        return mealService.getRandomMeal();
    }

    @Override
    public Single<CategoryResponse> getCategories() {
        return mealService.getCategories();
    }

    @Override
    public Single<CountryResponse> getCountries() {
        return mealService.getCountries();
    }

    @Override
    public Single<MealResponse> searchMeals(String query) {
        return mealService.searchMeals(query);
    }

    @Override
    public Single<MealResponse> filterByCategory(String category) {
        return mealService.filterByCategory(category);
    }

    @Override
    public Single<MealResponse> filterByArea(String area) {
        return mealService.filterByArea(area);
    }

    @Override
    public Single<IngredientResponse> getIngredients() {
        return mealService.getIngredients();
    }

    @Override
    public Single<MealResponse> filterByIngredient(String ingredient) {
        return mealService.filterByIngredient(ingredient);
    }

    @Override
    public Single<MealResponse> getMealDetails(String id) {
        return mealService.getMealDetails(id);
    }
}