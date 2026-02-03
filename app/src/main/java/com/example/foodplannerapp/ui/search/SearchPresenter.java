package com.example.foodplannerapp.ui.search;

import com.example.foodplannerapp.data.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenter implements SearchContract.Presenter {
    private final SearchContract.View view;
    private final MealRepository repository;

    public SearchPresenter(SearchContract.View view, MealRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void searchMeals(String query) {
        view.showLoading();
        repository.searchMeals(query) // Ensure this method exists in Repository
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.showSearchResults(response.getMeals());
                            view.hideLoading();
                        },
                        error -> {
                            view.showError("No meals found");
                            view.hideLoading();
                        }
                );
    }

    @Override
    public void getCategories() {
        repository.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showCategories(response.getCategories()),
                        error -> view.showError(error.getMessage())
                );
    }

    @Override
    public void filterByCategory(String categoryName) {
        view.showLoading();
        repository.filterByCategory(categoryName) // Ensure this method exists in Repository
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.showSearchResults(response.getMeals());
                            view.hideLoading();
                        },
                        error -> {
                            view.showError(error.getMessage());
                            view.hideLoading();
                        }
                );
    }
    // ... inside SearchPresenter class ...

    @Override
    public void getCountries() {
        repository.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showCountries(response.getCountries()),
                        error -> view.showError(error.getMessage())
                );
    }

    @Override
    public void filterByCountry(String countryName) {
        view.showLoading();
        repository.filterByArea(countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.showSearchResults(response.getMeals());
                            view.hideLoading();
                        },
                        error -> {
                            view.showError(error.getMessage());
                            view.hideLoading();
                        }
                );
    }
}