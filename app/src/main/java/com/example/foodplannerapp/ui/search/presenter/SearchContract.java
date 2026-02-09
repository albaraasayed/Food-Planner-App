package com.example.foodplannerapp.ui.search.presenter;

import com.example.foodplannerapp.model.Category;
import com.example.foodplannerapp.model.Country;
import com.example.foodplannerapp.model.Ingredient;
import com.example.foodplannerapp.model.Meal;

import java.util.List;

public interface SearchContract {

    interface View {
        void showLoading();

        void hideLoading();

        void showError(String message);

        void showSearchResults(List<Meal> meals);

        void showCategories(List<Category> categories);

        void showCountries(List<Country> countries);

        void showIngredients(List<Ingredient> ingredients);
    }

    interface Presenter {
        void searchMeals(String query);

        void getCategories();

        void getCountries();

        void getIngredients();

        void filterByCategory(String categoryName);

        void filterByCountry(String countryName);

        void filterByIngredient(String ingredient);
    }
}