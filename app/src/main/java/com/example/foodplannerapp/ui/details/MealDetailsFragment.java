package com.example.foodplannerapp.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplannerapp.R;
import com.example.foodplannerapp.data.config.RetrofitClient;
import com.example.foodplannerapp.data.local.local_datasource_implementation.MealLocalDataSourceImpl;
import com.example.foodplannerapp.data.remote.remote_datasource_implementation.MealRemoteDataSourceImpl;
import com.example.foodplannerapp.data.repository.MealRepositoryImpl;
import com.example.foodplannerapp.model.Ingredient;
import com.example.foodplannerapp.model.Meal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsFragment extends Fragment {

    private ImageView detailMealImg;
    private TextView tvTitle, tvArea, tvCategory, tvInstructions;
    private YouTubePlayerView youTubePlayerView;
    private ImageButton btnBack, btnFavorite; // Added btnFavorite
    private FloatingActionButton fabPlan;
    private RecyclerView rvIngredients;
    private DetailsIngredientAdapter ingredientAdapter;
    private MealRepositoryImpl repository;

    private Meal currentMeal;
    private boolean isFavorite = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Init Views
        detailMealImg = view.findViewById(R.id.detailMealImg);
        tvTitle = view.findViewById(R.id.tvMealTitle);
        tvArea = view.findViewById(R.id.tvArea);
        tvCategory = view.findViewById(R.id.tvCategory);
        tvInstructions = view.findViewById(R.id.tvInstructions);
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        btnBack = view.findViewById(R.id.btnBack);
        btnFavorite = view.findViewById(R.id.btnFavorite); // Bind View
        fabPlan = view.findViewById(R.id.fabPlan);
        rvIngredients = view.findViewById(R.id.rvIngredients);

        // 2. Setup Adapter
        ingredientAdapter = new DetailsIngredientAdapter();
        rvIngredients.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvIngredients.setAdapter(ingredientAdapter);

        // 3. Init Repository
        repository = MealRepositoryImpl.getInstance(
                MealRemoteDataSourceImpl.getInstance(RetrofitClient.getService()),
                MealLocalDataSourceImpl.getInstance(getContext())
        );

        // 4. Listeners
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        fabPlan.setOnClickListener(v -> Toast.makeText(getContext(), "Weekly Planner", Toast.LENGTH_SHORT).show());
        getLifecycle().addObserver(youTubePlayerView);

        // 5. Handle Favorite Click
        btnFavorite.setOnClickListener(v -> toggleFavorite());

        // 6. Load Data
        if (getArguments() != null) {
            currentMeal = (Meal) getArguments().getSerializable("meal_data");
            if (currentMeal != null) {
                // Check if it is already a favorite
                checkFavoriteStatus(currentMeal.getId());

                // Check if "Lite" meal or Full meal
                if (currentMeal.getInstructions() == null || currentMeal.getInstructions().isEmpty()) {
                    displayBasicInfo(currentMeal);
                    fetchFullMealDetails(currentMeal.getId());
                } else {
                    displayFullMeal(currentMeal);
                }
            }
        }
    }

    // --- FAVORITE LOGIC ---

    private void checkFavoriteStatus(String mealId) {
        repository.getStoredFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        favorites -> {
                            isFavorite = false;
                            for (Meal m : favorites) {
                                if (m.getId().equals(mealId)) {
                                    isFavorite = true;
                                    break;
                                }
                            }
                            updateFavoriteIcon();
                        },
                        error -> {
                        } // Ignore errors for check
                );
    }

    private void toggleFavorite() {
        if (currentMeal == null) return;

        if (isFavorite) {
            // Remove from DB
            repository.removeFromFavorites(currentMeal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                isFavorite = false;
                                updateFavoriteIcon();
                                Toast.makeText(getContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
                            },
                            error -> Toast.makeText(getContext(), "Error removing", Toast.LENGTH_SHORT).show()
                    );
        } else {
            // Add to DB
            repository.addToFavorites(currentMeal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                isFavorite = true;
                                updateFavoriteIcon();
                                Toast.makeText(getContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                            },
                            error -> Toast.makeText(getContext(), "Error adding", Toast.LENGTH_SHORT).show()
                    );
        }
    }

    private void updateFavoriteIcon() {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite);
            btnFavorite.setColorFilter(ContextCompat.getColor(requireContext(), R.color.error_red));
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_boarder);
            btnFavorite.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black));
        }
    }

    // --- EXISTING METHODS ---

    private void fetchFullMealDetails(String mealId) {
        repository.getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                                currentMeal = response.getMeals().get(0); // Update current meal object
                                displayFullMeal(currentMeal);
                            }
                        },
                        error -> Toast.makeText(getContext(), "Error loading details", Toast.LENGTH_SHORT).show()
                );
    }

    private void displayBasicInfo(Meal meal) {
        tvTitle.setText(meal.getName());
        Glide.with(this).load(meal.getThumbUrl()).into(detailMealImg);
        tvInstructions.setText("Loading...");
        tvArea.setText("");
        tvCategory.setText("");
    }

    private void displayFullMeal(Meal meal) {
        tvTitle.setText(meal.getName());
        tvArea.setText(meal.getArea());
        tvCategory.setText(meal.getCategory());
        tvInstructions.setText(meal.getInstructions());
        Glide.with(this).load(meal.getThumbUrl()).into(detailMealImg);

        List<Ingredient> ingredients = extractIngredients(meal);
        ingredientAdapter.setList(ingredients);

        if (meal.getYoutubeUrl() != null && !meal.getYoutubeUrl().isEmpty()) {
            String videoId = getVideoId(meal.getYoutubeUrl());
            if (videoId != null) {
                youTubePlayerView.setVisibility(View.VISIBLE);
                youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0);
                    }
                });
            } else {
                youTubePlayerView.setVisibility(View.GONE);
            }
        } else {
            youTubePlayerView.setVisibility(View.GONE);
        }
    }

    private List<Ingredient> extractIngredients(Meal meal) {
        List<Ingredient> ingredientsList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            try {
                Method ingredientMethod = meal.getClass().getMethod("getStrIngredient" + i);
                Method measureMethod = meal.getClass().getMethod("getStrMeasure" + i);
                Object ingredientObj = ingredientMethod.invoke(meal);
                Object measureObj = measureMethod.invoke(meal);

                String ingredientName = (ingredientObj != null) ? ingredientObj.toString().trim() : "";
                String measure = (measureObj != null) ? measureObj.toString().trim() : "";

                if (!ingredientName.isEmpty()) {
                    Ingredient ingredient = new Ingredient();
                    try {
                        Method setName = ingredient.getClass().getMethod("setStrIngredient", String.class);
                        setName.invoke(ingredient, ingredientName + "\n" + measure);
                    } catch (Exception e) {
                        Method setName = ingredient.getClass().getMethod("setName", String.class);
                        setName.invoke(ingredient, ingredientName + "\n" + measure);
                    }
                    ingredientsList.add(ingredient);
                }
            } catch (Exception e) {
            }
        }
        return ingredientsList;
    }

    private String getVideoId(String url) {
        String videoId = null;
        if (url != null && url.trim().length() > 0) {
            String expression = "^.*(youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=|&v=)([^#&?]*).*";
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String group = matcher.group(2);
                if (group != null && group.length() == 11) videoId = group;
            }
        }
        return videoId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (youTubePlayerView != null) youTubePlayerView.release();
    }
}