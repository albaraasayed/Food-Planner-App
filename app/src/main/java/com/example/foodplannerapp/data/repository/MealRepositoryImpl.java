package com.example.foodplannerapp.data.repository;

import com.example.foodplannerapp.data.local.local_datasource_interface.MealLocalDataSource;
import com.example.foodplannerapp.data.remote.remote_datasource_interface.MealRemoteDataSource;
import com.example.foodplannerapp.model.CategoryResponse;
import com.example.foodplannerapp.model.CountryResponse;
import com.example.foodplannerapp.model.IngredientResponse;
import com.example.foodplannerapp.model.Meal;
import com.example.foodplannerapp.model.MealPlan;
import com.example.foodplannerapp.model.MealResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class MealRepositoryImpl implements MealRepository {

    private static MealRepositoryImpl instance = null;
    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;

    // Firebase References
    private final FirebaseAuth mAuth;
    private final DatabaseReference mDatabase;

    private MealRepositoryImpl(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static MealRepositoryImpl getInstance(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
        if (instance == null) {
            instance = new MealRepositoryImpl(remoteDataSource, localDataSource);
        }
        return instance;
    }

    // --- Remote Data Methods (Retrofit) ---
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

    // --- Favorites Logic (Room + Firebase) ---
    @Override
    public Completable addToFavorites(Meal meal) {
        return localDataSource.insertFavorite(meal)
                .doOnComplete(() -> {
                    // If Online & Logged In: Save to Firebase
                    if (mAuth.getCurrentUser() != null) {
                        String uid = mAuth.getCurrentUser().getUid();
                        mDatabase.child("users").child(uid).child("favorites").child(meal.getId()).setValue(meal);
                    }
                });
    }

    @Override
    public Completable removeFromFavorites(Meal meal) {
        return localDataSource.deleteFavorite(meal)
                .doOnComplete(() -> {
                    // If Online & Logged In: Remove from Firebase
                    if (mAuth.getCurrentUser() != null) {
                        String uid = mAuth.getCurrentUser().getUid();
                        mDatabase.child("users").child(uid).child("favorites").child(meal.getId()).removeValue();
                    }
                });
    }

    @Override
    public Single<List<Meal>> getStoredFavorites() {
        // Always get from Room (Offline Support)
        return localDataSource.getFavorites();
    }

    // --- Planner Logic (Room + Firebase) ---
    @Override
    public Completable addMealToPlan(MealPlan mealPlan) {
        return localDataSource.insertMealPlan(mealPlan)
                .doOnComplete(() -> {
                    if (mAuth.getCurrentUser() != null) {
                        String uid = mAuth.getCurrentUser().getUid();
                        // Generate a unique key for the plan based on day and meal ID
                        String key = mealPlan.getDay() + "_" + mealPlan.getMealId();
                        mDatabase.child("users").child(uid).child("plans").child(key).setValue(mealPlan);
                    }
                });
    }

    @Override
    public Completable removeMealFromPlan(MealPlan mealPlan) {
        return localDataSource.deleteMealPlan(mealPlan)
                .doOnComplete(() -> {
                    if (mAuth.getCurrentUser() != null) {
                        String uid = mAuth.getCurrentUser().getUid();
                        String key = mealPlan.getDay() + "_" + mealPlan.getMealId();
                        mDatabase.child("users").child(uid).child("plans").child(key).removeValue();
                    }
                });
    }

    @Override
    public Single<List<MealPlan>> getPlan() {
        // Always get from Room (Offline Support)
        return localDataSource.getAllPlans();
    }
}