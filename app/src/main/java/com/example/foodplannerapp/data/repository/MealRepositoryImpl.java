package com.example.foodplannerapp.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodplannerapp.data.local.local_datasource_interface.MealLocalDataSource;
import com.example.foodplannerapp.data.remote.remote_datasource_interface.MealRemoteDataSource;
import com.example.foodplannerapp.model.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable; // Use Observable
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealRepositoryImpl implements MealRepository {

    private static MealRepositoryImpl instance = null;
    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;
    private final FirebaseAuth mAuth;
    private final DatabaseReference mDatabase;
    private String currentSyncUid = null;

    private MealRepositoryImpl(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mAuth.getCurrentUser() != null) {
            syncFromFirebase(mAuth.getCurrentUser().getUid());
        }
    }

    public static MealRepositoryImpl getInstance(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
        if (instance == null) {
            instance = new MealRepositoryImpl(remoteDataSource, localDataSource);
        }
        return instance;
    }

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
        return localDataSource.insertFavorite(meal)
                .doOnComplete(() -> {
                    if (mAuth.getCurrentUser() != null) {
                        String uid = mAuth.getCurrentUser().getUid();
                        mDatabase.child("users").child(uid).child("favorites")
                                .child(meal.getId()).setValue(meal);
                    }
                });
    }

    @Override
    public Completable removeFromFavorites(Meal meal) {
        return localDataSource.deleteFavorite(meal)
                .doOnComplete(() -> {
                    if (mAuth.getCurrentUser() != null) {
                        String uid = mAuth.getCurrentUser().getUid();
                        mDatabase.child("users").child(uid).child("favorites")
                                .child(meal.getId()).removeValue();
                    }
                });
    }

    @Override
    public Observable<List<Meal>> getStoredFavorites() {
        return localDataSource.getFavorites();
    }

    @Override
    public Completable addMealToPlan(MealPlan mealPlan) {
        if (mealPlan.getId() == null || !mealPlan.getId().contains("_")) {
            mealPlan.setId(mealPlan.getDay() + "_" + mealPlan.getMealId());
        }
        return localDataSource.insertMealPlan(mealPlan)
                .doOnComplete(() -> {
                    if (mAuth.getCurrentUser() != null) {
                        String uid = mAuth.getCurrentUser().getUid();
                        mDatabase.child("users").child(uid).child("plans")
                                .child(mealPlan.getId()).setValue(mealPlan);
                    }
                });
    }

    @Override
    public Completable removeMealFromPlan(MealPlan mealPlan) {
        return localDataSource.deleteMealPlan(mealPlan)
                .doOnComplete(() -> {
                    if (mAuth.getCurrentUser() != null) {
                        String uid = mAuth.getCurrentUser().getUid();
                        mDatabase.child("users").child(uid).child("plans")
                                .child(mealPlan.getId()).removeValue();
                    }
                });
    }

    @Override
    public Observable<List<MealPlan>> getPlan() {
        return localDataSource.getAllPlans();
    }

    @Override
    public void syncFromFirebase(String uid) {
        if (uid == null || uid.equals(currentSyncUid)) return;
        currentSyncUid = uid;

        // 1. Favorites Listener
        mDatabase.child("users").child(uid).child("favorites").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Meal remoteMeal = snapshot.getValue(Meal.class);
                if (remoteMeal != null) {
                    localDataSource.insertFavorite(remoteMeal)
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Meal meal = snapshot.getValue(Meal.class);
                if (meal != null) {
                    localDataSource.deleteFavorite(meal)
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        mDatabase.child("users").child(uid).child("plans").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MealPlan remotePlan = snapshot.getValue(MealPlan.class);
                if (remotePlan != null) {
                    localDataSource.insertMealPlan(remotePlan)
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                MealPlan plan = snapshot.getValue(MealPlan.class);
                if (plan != null) {
                    localDataSource.deleteMealPlan(plan)
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}