package com.example.foodplannerapp.ui.home;

import com.example.foodplannerapp.model.Category;
import com.example.foodplannerapp.model.Country;
import com.example.foodplannerapp.model.Meal;

import java.util.List;

public interface HomeContract {

    interface View {
        void showLoading();

        void hideLoading();

        void showMealOfDay(Meal meal);

        void showCategories(List<Category> categoryList);

        void showCountries(List<Country> countryList);

        void showError(String errorMsg);
    }

    interface Presenter {
        void getDailyInspiration();

        void getCategoryList();

        void getCountryList();
    }
}