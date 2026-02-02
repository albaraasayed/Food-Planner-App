package com.example.foodplannerapp.ui.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplannerapp.R;
import com.example.foodplannerapp.model.Meal;
import com.google.android.material.button.MaterialButton;

public class MealDetailsFragment extends Fragment {

    private ImageView detailMealImg;
    private TextView tvTitle, tvArea, tvInstructions;
    private RecyclerView rvIngredients;
    private MaterialButton btnWatchVideo;
    // MVP: You would add Presenter here later to fetch by ID

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialize Views
        detailMealImg = view.findViewById(R.id.detailMealImg);
        tvTitle = view.findViewById(R.id.tvMealTitle);
        tvArea = view.findViewById(R.id.tvMealArea);
        tvInstructions = view.findViewById(R.id.tvInstructions);
        rvIngredients = view.findViewById(R.id.rvIngredients);
        btnWatchVideo = view.findViewById(R.id.btnWatchVideo);

        // 2. Setup Back Button
        view.findViewById(R.id.toolbar).setOnClickListener(v -> requireActivity().onBackPressed());

        // 3. Get Data (Assumes you passed the Meal object via SafeArgs or Bundle)
        // For now, let's assume you passed the "Meal" object directly
        if (getArguments() != null) {
            Meal meal = (Meal) getArguments().getSerializable("meal_data"); // Make sure Meal implements Serializable
            if (meal != null) {
                displayMeal(meal);
            }
        }
    }

    private void displayMeal(Meal meal) {
        tvTitle.setText(meal.getName());
        tvArea.setText(meal.getArea() + "  •  " + meal.getCategory());
        tvInstructions.setText(meal.getInstructions());
        Glide.with(this).load(meal.getThumbUrl()).into(detailMealImg);

        // Setup Video
        btnWatchVideo.setOnClickListener(v -> {
            // Ideally open a WebViewFragment or Intent
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(meal.getYoutubeUrl()));
            startActivity(intent);
        });

        // Setup Ingredients (This requires parsing the strIngredient1...20 fields)
        // I'll show the logic to setup the adapter here.
        // List<Ingredient> ingredients = parseIngredients(meal);
        // IngredientsAdapter adapter = new IngredientsAdapter(getContext(), ingredients);
        // rvIngredients.setAdapter(adapter);
    }
}