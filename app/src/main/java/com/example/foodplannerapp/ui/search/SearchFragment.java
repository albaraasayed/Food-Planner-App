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
import com.example.foodplannerapp.data.local.local_datasource_implementation.MealLocalDataSourceImpl;
import com.example.foodplannerapp.data.remote.remote_datasource_implementation.MealRemoteDataSourceImpl;
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.searchView);
        RecyclerView rvSearchResults = view.findViewById(R.id.rvSearchResults);
        RecyclerView rvCategoryChips = view.findViewById(R.id.rvCategoryChips);
        RecyclerView rvCountryChips = view.findViewById(R.id.rvCountryChips);
        RecyclerView rvIngredientChips = view.findViewById(R.id.rvIngredientChips);

        mealsAdapter = new SearchMealsAdapter(meal -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("meal_data", meal);
            Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_mealDetailsFragment, bundle);
        });
        rvSearchResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvSearchResults.setAdapter(mealsAdapter);

        chipAdapter = new CategoryChipAdapter(categoryName -> presenter.filterByCategory(categoryName));
        rvCategoryChips.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCategoryChips.setAdapter(chipAdapter);

        countryAdapter = new CountryChipAdapter(countryName -> presenter.filterByCountry(countryName));
        rvCountryChips.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCountryChips.setAdapter(countryAdapter);

        ingredientAdapter = new IngredientChipAdapter(ingredientName -> presenter.filterByIngredient(ingredientName));
        rvIngredientChips.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvIngredientChips.setAdapter(ingredientAdapter);

        presenter = new SearchPresenter(this,
                MealRepositoryImpl.getInstance(
                        MealRemoteDataSourceImpl.getInstance(RetrofitClient.getService()),
                        MealLocalDataSourceImpl.getInstance(getContext())
                )
        );

        presenter.getCategories();
        presenter.getCountries();
        presenter.getIngredients();
        presenter.searchMeals("");

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