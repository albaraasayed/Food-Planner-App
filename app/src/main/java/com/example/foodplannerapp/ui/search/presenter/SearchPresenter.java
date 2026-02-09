package com.example.foodplannerapp.ui.search.presenter;

import com.example.foodplannerapp.data.repository.MealRepositoryImpl;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenter implements SearchContract.Presenter {

    private final SearchContract.View view;
    private final MealRepositoryImpl repository;

    public SearchPresenter(SearchContract.View view, MealRepositoryImpl repository) {
        this.view = view;
        this.repository = repository;
    }

    // --- SEARCH ---
    @Override
    public void searchMeals(String query) {
        view.showLoading();
        repository.searchMeals(query)
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

    // --- CATEGORIES ---
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
        repository.filterByCategory(categoryName)
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

    // --- COUNTRIES (CUISINES) ---
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

    // --- INGREDIENTS ---
    @Override
    public void getIngredients() {
        repository.getIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showIngredients(response.getIngredients()),
                        error -> view.showError(error.getMessage())
                );
    }

    @Override
    public void filterByIngredient(String ingredient) {
        view.showLoading();
        repository.filterByIngredient(ingredient)
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