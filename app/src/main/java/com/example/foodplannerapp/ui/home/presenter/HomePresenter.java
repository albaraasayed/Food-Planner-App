package com.example.foodplannerapp.ui.home.presenter;

import com.example.foodplannerapp.data.repository.MealRepositoryImpl;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.View view;
    private final MealRepositoryImpl repository;

    public HomePresenter(HomeContract.View view, MealRepositoryImpl repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getDailyInspiration() {
        view.showLoading();
        repository.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                                view.showMealOfDay(response.getMeals().get(0));
                            }
                            view.hideLoading();
                        },
                        error -> {
                            view.showError(error.getMessage());
                            view.hideLoading();
                        }
                );
    }

    @Override
    public void getSweetMeals() {
        repository.filterByCategory("Dessert")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showSweetMeals(response.getMeals()),
                        error -> view.showError("Failed to load sweets")
                );
    }

    @Override
    public void getSaltyMeals() {
        repository.filterByCategory("Miscellaneous")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showSaltyMeals(response.getMeals()),
                        error -> view.showError("Failed to load salty meals")
                );
    }
}