package com.example.foodplannerapp.ui.favorites.presenter;

import android.util.Log;

import com.example.foodplannerapp.data.repository.MealRepositoryImpl;
import com.example.foodplannerapp.model.Meal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritesPresenter implements FavoritesContract.Presenter {
    private FavoritesContract.View view;
    private MealRepositoryImpl repository;

    public FavoritesPresenter(FavoritesContract.View view, MealRepositoryImpl repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getFavorites() {
        repository.getStoredFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            if (meals.isEmpty()) view.showEmptyState();
                            else view.showFavorites(meals);
                        },
                        error -> {
                            Log.e("FavPresenter", "Error", error);
                            view.showError("Error loading favorites");
                        }
                );
    }

    @Override
    public void removeFavorite(Meal meal) {
        repository.removeFromFavorites(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                        },
                        error -> view.showError("Could not delete")
                );
    }
}