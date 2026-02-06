package com.example.foodplannerapp.data.local.local_datasource_interface;

import com.example.foodplannerapp.model.Meal;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface MealLocalDataSource {
    Completable insertFavorite(Meal meal);
    Completable deleteFavorite(Meal meal);
    Single<List<Meal>> getFavorites();
}