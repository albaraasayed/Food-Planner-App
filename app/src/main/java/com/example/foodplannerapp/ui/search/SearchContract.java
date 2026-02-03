package com.example.foodplannerapp.ui.search;

import com.example.foodplannerapp.model.Category;
import com.example.foodplannerapp.model.Country;
import com.example.foodplannerapp.model.Meal;

import java.util.List;

public interface SearchContract {
    interface View {
        void showLoading();

        void hideLoading();

        void showSearchResults(List<Meal> meals);

        void showCategories(List<Category> categories);

        void showCountries(List<Country> countries); // NEW

        void showError(String message);
    }

    interface Presenter {
        void searchMeals(String query);

        void getCategories();

        void getCountries(); // NEW

        void filterByCategory(String categoryName);

        void filterByCountry(String countryName); // NEW
    }
}