package com.example.foodplannerapp.ui.home;

import com.example.foodplannerapp.data.MealRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.View view;
    private final MealRepository repository;

    public HomePresenter(HomeContract.View view, MealRepository repository) {
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
        // "Sweet" -> Dessert Category
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
        // "Salty" -> Miscellaneous Category (Contains Poutine, Pizza, etc.)
        repository.filterByCategory("Miscellaneous")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showSaltyMeals(response.getMeals()),
                        error -> view.showError("Failed to load salty meals")
                );
    }
}