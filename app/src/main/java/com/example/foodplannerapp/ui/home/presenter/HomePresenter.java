package com.example.foodplannerapp.ui.home.presenter;

import android.content.SharedPreferences;
import com.example.foodplannerapp.data.repository.MealRepositoryImpl;
import com.example.foodplannerapp.model.Meal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.View view;
    private final MealRepositoryImpl repository;
    private final SharedPreferences sharedPreferences;

    public HomePresenter(HomeContract.View view, MealRepositoryImpl repository, SharedPreferences sharedPreferences) {
        this.view = view;
        this.repository = repository;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void getDailyInspiration() {
        view.showLoading();

        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String storedDate = sharedPreferences.getString("daily_date", "");
        String storedMealId = sharedPreferences.getString("daily_id", "");

        if (todayDate.equals(storedDate) && !storedMealId.isEmpty()) {
            repository.getMealDetails(storedMealId)
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
                                view.showError("Could not load daily meal");
                                view.hideLoading();
                            }
                    );
        } else {
            repository.getRandomMeal()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            response -> {
                                if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                                    Meal meal = response.getMeals().get(0);

                                    sharedPreferences.edit()
                                            .putString("daily_date", todayDate)
                                            .putString("daily_id", meal.getId())
                                            .apply();

                                    view.showMealOfDay(meal);
                                }
                                view.hideLoading();
                            },
                            error -> {
                                view.showError(error.getMessage());
                                view.hideLoading();
                            }
                    );
        }
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