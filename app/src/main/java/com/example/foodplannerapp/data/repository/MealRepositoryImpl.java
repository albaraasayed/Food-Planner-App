package com.example.foodplannerapp.data.repository;

import com.example.foodplannerapp.data.local.local_datasource_interface.MealLocalDataSource;
import com.example.foodplannerapp.data.remote.remote_datasource_interface.MealRemoteDataSource;
import com.example.foodplannerapp.model.CategoryResponse;
import com.example.foodplannerapp.model.CountryResponse;
import com.example.foodplannerapp.model.IngredientResponse;
import com.example.foodplannerapp.model.Meal;
import com.example.foodplannerapp.model.MealPlan;
import com.example.foodplannerapp.model.MealResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class MealRepositoryImpl implements MealRepository {

    private static MealRepositoryImpl instance = null;
    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;

    private MealRepositoryImpl(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static MealRepositoryImpl getInstance(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
        if (instance == null) {
            instance = new MealRepositoryImpl(remoteDataSource, localDataSource);
        }
        return instance;
    }

    // --- Remote Data Methods ---
    @Override
    public Single<MealResponse> getRandomMeal() {
        return remoteDataSource.getRandomMeal();
    }

    @Override
    public Single<CategoryResponse> getCategories() {
        return remoteDataSource.getCategories();
    }

    @Override
    public Single<CountryResponse> getCountries() {
        return remoteDataSource.getCountries();
    }

    @Override
    public Single<MealResponse> searchMeals(String query) {
        return remoteDataSource.searchMeals(query);
    }

    @Override
    public Single<MealResponse> filterByCategory(String category) {
        return remoteDataSource.filterByCategory(category);
    }

    @Override
    public Single<MealResponse> filterByArea(String area) {
        return remoteDataSource.filterByArea(area);
    }

    @Override
    public Single<IngredientResponse> getIngredients() {
        return remoteDataSource.getIngredients();
    }

    @Override
    public Single<MealResponse> filterByIngredient(String ingredient) {
        return remoteDataSource.filterByIngredient(ingredient);
    }

    @Override
    public Single<MealResponse> getMealDetails(String id) {
        return remoteDataSource.getMealDetails(id);
    }

    @Override
    public Completable addToFavorites(Meal meal) {
        return localDataSource.insertFavorite(meal);
    }

    @Override
    public Completable removeFromFavorites(Meal meal) {
        return localDataSource.deleteFavorite(meal);
    }

    @Override
    public Single<List<Meal>> getStoredFavorites() {
        return localDataSource.getFavorites();
    }

    @Override
    public Completable addMealToPlan(MealPlan mealPlan) {
        return localDataSource.insertMealPlan(mealPlan);
    }

    @Override
    public Completable removeMealFromPlan(MealPlan mealPlan) {
        return localDataSource.deleteMealPlan(mealPlan);
    }

    @Override
    public Single<List<MealPlan>> getPlan() {
        return localDataSource.getAllPlans();
    }
}