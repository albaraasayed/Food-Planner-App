package com.example.foodplannerapp.ui.favorites.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodplannerapp.R;
import com.example.foodplannerapp.data.config.RetrofitClient;
import com.example.foodplannerapp.data.local.local_datasource_implementation.MealLocalDataSourceImpl;
import com.example.foodplannerapp.data.remote.remote_datasource_implementation.MealRemoteDataSourceImpl;
import com.example.foodplannerapp.data.repository.MealRepositoryImpl;
import com.example.foodplannerapp.model.Meal;
import com.example.foodplannerapp.ui.favorites.presenter.FavoritesContract;
import com.example.foodplannerapp.ui.favorites.presenter.FavoritesPresenter;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesContract.View {

    private FavoritesPresenter presenter;
    private FavoritesAdapter adapter;
    private RecyclerView rvFavorites;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        adapter = new FavoritesAdapter(meal -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("meal_data", meal);
            Navigation.findNavController(view).navigate(R.id.action_favorites_to_details, bundle);
        });

        rvFavorites.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvFavorites.setAdapter(adapter);

        presenter = new FavoritesPresenter(this, MealRepositoryImpl.getInstance(
                MealRemoteDataSourceImpl.getInstance(RetrofitClient.getService()),
                MealLocalDataSourceImpl.getInstance(getContext())
        ));

        presenter.getFavorites();
    }

    @Override
    public void showFavorites(List<Meal> meals) {
        adapter.setList(meals);
    }

    @Override
    public void showEmptyState() {
        Toast.makeText(getContext(), "No Favorites yet!", Toast.LENGTH_SHORT).show();
        adapter.setList(new ArrayList<>());
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}