package com.example.foodplannerapp.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplannerapp.R;
import com.example.foodplannerapp.data.repository.MealRepositoryImpl;
import com.example.foodplannerapp.model.Category;
import com.example.foodplannerapp.model.Country;
import com.example.foodplannerapp.model.Ingredient;
import com.example.foodplannerapp.model.Meal;
import com.example.foodplannerapp.data.config.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchContract.View {

    private SearchPresenter presenter;
    private SearchMealsAdapter mealsAdapter;
    private CategoryChipAdapter chipAdapter;
    private CountryChipAdapter countryAdapter;
    private IngredientChipAdapter ingredientAdapter;
    private ProgressBar progressBar;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Ensure fragment_search.xml exists and has all IDs
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialize Views
        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.searchView);
        RecyclerView rvSearchResults = view.findViewById(R.id.rvSearchResults);
        RecyclerView rvCategoryChips = view.findViewById(R.id.rvCategoryChips);
        RecyclerView rvCountryChips = view.findViewById(R.id.rvCountryChips);
        RecyclerView rvIngredientChips = view.findViewById(R.id.rvIngredientChips);

        // 2. Setup Meal Grid Adapter
        mealsAdapter = new SearchMealsAdapter(meal -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("meal_data", meal);
            Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_mealDetailsFragment, bundle);
        });
        rvSearchResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvSearchResults.setAdapter(mealsAdapter);

        // 3. Setup Category Adapter
        chipAdapter = new CategoryChipAdapter(categoryName -> presenter.filterByCategory(categoryName));
        rvCategoryChips.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCategoryChips.setAdapter(chipAdapter);

        // 4. Setup Country Adapter
        countryAdapter = new CountryChipAdapter(countryName -> presenter.filterByCountry(countryName));
        rvCountryChips.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCountryChips.setAdapter(countryAdapter);

        // 5. Setup Ingredient Adapter
        ingredientAdapter = new IngredientChipAdapter(ingredientName -> presenter.filterByIngredient(ingredientName));
        rvIngredientChips.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvIngredientChips.setAdapter(ingredientAdapter);

        // 6. Initialize Presenter and Fetch Data
        presenter = new SearchPresenter(this, MealRepositoryImpl.getInstance(RetrofitClient.getService()));
        presenter.getCategories();
        presenter.getCountries();
        presenter.getIngredients();
        presenter.searchMeals(""); // Initial random/empty search

        // 7. Search Bar Listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.searchMeals(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // --- View Implementation Methods ---

    @Override
    public void showLoading() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSearchResults(List<Meal> meals) {
        if (meals == null) meals = new ArrayList<>();
        mealsAdapter.setList(meals);
    }

    @Override
    public void showCategories(List<Category> categories) {
        chipAdapter.setList(categories);
    }

    @Override
    public void showCountries(List<Country> countries) {
        countryAdapter.setList(countries);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        ingredientAdapter.setList(ingredients);
    }

    @Override
    public void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}