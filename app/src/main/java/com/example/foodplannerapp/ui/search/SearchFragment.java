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
import com.example.foodplannerapp.data.MealRepository;
import com.example.foodplannerapp.model.Category;
import com.example.foodplannerapp.model.Country;
import com.example.foodplannerapp.model.Meal;
import com.example.foodplannerapp.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchContract.View {

    private SearchPresenter presenter;
    private SearchMealsAdapter mealsAdapter;
    private CategoryChipAdapter chipAdapter;
    private CountryChipAdapter countryAdapter; // Adapter for Countries
    private ProgressBar progressBar;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Init Views
        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.searchView);
        RecyclerView rvSearchResults = view.findViewById(R.id.rvSearchResults);
        RecyclerView rvCategoryChips = view.findViewById(R.id.rvCategoryChips);
        RecyclerView rvCountryChips = view.findViewById(R.id.rvCountryChips); // Ensure this ID exists in XML

        // 2. Setup Meal Grid Adapter
        mealsAdapter = new SearchMealsAdapter(meal -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("meal_data", meal);
            Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_mealDetailsFragment, bundle);
        });
        rvSearchResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvSearchResults.setAdapter(mealsAdapter);

        // 3. Setup Category Chip Adapter
        chipAdapter = new CategoryChipAdapter(categoryName -> {
            presenter.filterByCategory(categoryName);
        });
        rvCategoryChips.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCategoryChips.setAdapter(chipAdapter);

        // 4. Setup Country Chip Adapter
        countryAdapter = new CountryChipAdapter(countryName -> {
            presenter.filterByCountry(countryName);
        });
        rvCountryChips.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCountryChips.setAdapter(countryAdapter);

        // 5. Initialize Presenter & Fetch Data
        presenter = new SearchPresenter(this, MealRepository.getInstance(RetrofitClient.getService()));
        presenter.getCategories();
        presenter.getCountries(); // Fetch countries
        presenter.searchMeals(""); // Initial load

        // 6. Search Listener
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

    // --- SearchContract.View Implementation ---

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
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
        // This was the missing method causing the error!
        countryAdapter.setList(countries);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}