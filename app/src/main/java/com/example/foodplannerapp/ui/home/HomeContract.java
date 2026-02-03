package com.example.foodplannerapp.ui.home;

import com.example.foodplannerapp.model.Meal;
import java.util.List;

public interface HomeContract {

    interface View {
        void showLoading();
        void hideLoading();
        void showMealOfDay(Meal meal);

        // RENAMED METHODS
        void showSweetMeals(List<Meal> meals);
        void showSaltyMeals(List<Meal> meals);

        void showError(String errorMsg);
    }

    interface Presenter {
        void getDailyInspiration();

        // RENAMED METHODS
        void getSweetMeals();
        void getSaltyMeals();
    }
}