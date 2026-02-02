package com.example.foodplannerapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplannerapp.R;
import com.example.foodplannerapp.data.MealRepository;
import com.example.foodplannerapp.model.Category;
import com.example.foodplannerapp.model.Country;
import com.example.foodplannerapp.model.Meal;
import com.example.foodplannerapp.network.RetrofitClient;

import java.util.List;

public class HomeScreen extends Fragment implements HomeContract.View {

    private HomePresenter presenter;
    private ImageView imgMealOfDay;
    private TextView tvMealName;
    private CardView cardMealOfDay;
    private Meal currentRandomMeal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgMealOfDay = view.findViewById(R.id.imgMealOfDay);
        tvMealName = view.findViewById(R.id.tvMealName);
        cardMealOfDay = view.findViewById(R.id.cardMealOfDay);

        presenter = new HomePresenter(this, MealRepository.getInstance(RetrofitClient.getService()));

        presenter.getDailyInspiration();
        presenter.getCategoryList();
        presenter.getCountryList();

        cardMealOfDay.setOnClickListener(v -> {
            if (currentRandomMeal != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("meal_data", currentRandomMeal);
                Navigation.findNavController(v).navigate(R.id.action_home_to_details, bundle);
            }
        });
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMealOfDay(Meal meal) {
        this.currentRandomMeal = meal;
        tvMealName.setText(meal.getName());

        Glide.with(this)
                .load(meal.getThumbUrl())
                .into(imgMealOfDay);
    }

    @Override
    public void showCategories(List<Category> categoryList) {

    }

    @Override
    public void showCountries(List<Country> countryList) {
    }

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(getContext(), "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
    }
}