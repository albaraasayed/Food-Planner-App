package com.example.foodplannerapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplannerapp.R;
import com.example.foodplannerapp.data.MealRepository;
import com.example.foodplannerapp.model.Meal;
import com.example.foodplannerapp.network.RetrofitClient;

import java.util.List;

public class HomeScreen extends Fragment implements HomeContract.View {

    private HomePresenter presenter;
    private ImageView imgMealOfDay;
    private TextView tvMealName;
    private Button btnViewRecipe;
    private CardView cardMealOfDay;
    private RecyclerView rvSweet, rvSalty; // Renamed

    private HomeMealAdapter sweetAdapter, saltyAdapter; // Renamed
    private Meal currentRandomMeal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Init Views
        imgMealOfDay = view.findViewById(R.id.imgMealOfDay);
        tvMealName = view.findViewById(R.id.tvMealName);
        btnViewRecipe = view.findViewById(R.id.btnViewRecipe);
        cardMealOfDay = view.findViewById(R.id.cardMealOfDay);

        // NEW IDs
        rvSweet = view.findViewById(R.id.rvSweet);
        rvSalty = view.findViewById(R.id.rvSalty);

        // 2. Setup Adapters
        sweetAdapter = new HomeMealAdapter(meal -> navigateToDetails(view, meal));
        rvSweet.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvSweet.setAdapter(sweetAdapter);

        saltyAdapter = new HomeMealAdapter(meal -> navigateToDetails(view, meal));
        rvSalty.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvSalty.setAdapter(saltyAdapter);

        // 3. Presenter
        presenter = new HomePresenter(this, MealRepository.getInstance(RetrofitClient.getService()));
        presenter.getDailyInspiration();
        presenter.getSweetMeals();
        presenter.getSaltyMeals();

        // 4. Listeners
        View.OnClickListener mainListener = v -> {
            if (currentRandomMeal != null) navigateToDetails(v, currentRandomMeal);
        };
        cardMealOfDay.setOnClickListener(mainListener);
        btnViewRecipe.setOnClickListener(mainListener);
    }

    private void navigateToDetails(View v, Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal_data", meal);
        Navigation.findNavController(v).navigate(R.id.action_home_to_details, bundle);
    }

    // --- View Methods ---

    @Override
    public void showMealOfDay(Meal meal) {
        this.currentRandomMeal = meal;
        tvMealName.setText(meal.getName());
        Glide.with(this).load(meal.getThumbUrl()).into(imgMealOfDay);
    }

    @Override
    public void showSweetMeals(List<Meal> meals) {
        sweetAdapter.setList(meals);
    }

    @Override
    public void showSaltyMeals(List<Meal> meals) {
        saltyAdapter.setList(meals);
    }

    @Override
    public void showLoading() {}

    @Override
    public void hideLoading() {}

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }
}