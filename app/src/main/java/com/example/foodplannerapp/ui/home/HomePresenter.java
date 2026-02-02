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
    public void getCategoryList() {
        repository.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showCategories(response.getCategories()),
                        error -> view.showError(error.getMessage())
                );
    }

    @Override
    public void getCountryList() {
        repository.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showCountries(response.getCountries()),
                        error -> view.showError(error.getMessage())
                );
    }
}