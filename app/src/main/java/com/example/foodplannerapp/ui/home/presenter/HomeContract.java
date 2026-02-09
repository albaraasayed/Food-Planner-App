package com.example.foodplannerapp.ui.home.presenter;

import com.example.foodplannerapp.model.Meal;
import java.util.List;

public interface HomeContract {

    interface View {
        void showLoading();
        void hideLoading();
        void showMealOfDay(Meal meal);
        void showSweetMeals(List<Meal> meals);
        void showSaltyMeals(List<Meal> meals);
        void showError(String errorMsg);
    }

    interface Presenter {
        void getDailyInspiration();
        void getSweetMeals();
        void getSaltyMeals();
    }
}