package com.example.foodplannerapp.ui.details;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import com.example.foodplannerapp.model.MealPlan; // Import MealPlan
import com.google.android.material.dialog.MaterialAlertDialogBuilder; // Import Dialog
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsFragment extends Fragment {

    private ImageView detailMealImg;
    private TextView tvTitle, tvArea, tvCategory, tvInstructions;
    private YouTubePlayerView youTubePlayerView;
    private ImageButton btnBack, btnFavorite;
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

        // Initialize Views
        detailMealImg = view.findViewById(R.id.detailMealImg);
        tvTitle = view.findViewById(R.id.tvMealTitle);
        tvArea = view.findViewById(R.id.tvArea);
        tvCategory = view.findViewById(R.id.tvCategory);
        tvInstructions = view.findViewById(R.id.tvInstructions);
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        btnBack = view.findViewById(R.id.btnBack);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        fabPlan = view.findViewById(R.id.fabPlan);
        rvIngredients = view.findViewById(R.id.rvIngredients);

        // Setup Adapter
        ingredientAdapter = new DetailsIngredientAdapter();
        rvIngredients.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvIngredients.setAdapter(ingredientAdapter);

        // Setup Repository
        repository = MealRepositoryImpl.getInstance(
                MealRemoteDataSourceImpl.getInstance(RetrofitClient.getService()),
                MealLocalDataSourceImpl.getInstance(getContext())
        );

        // Setup Listeners
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // --- 1. OPEN DAY PICKER ON CLICK ---
        fabPlan.setOnClickListener(v -> {
            if (currentMeal != null) {
                showDayPickerDialog();
            } else {
                Toast.makeText(getContext(), "Please wait for meal to load", Toast.LENGTH_SHORT).show();
            }
        });

        btnFavorite.setOnClickListener(v -> toggleFavorite());

        getLifecycle().addObserver(youTubePlayerView);

        // Load Data
        if (getArguments() != null) {
            currentMeal = (Meal) getArguments().getSerializable("meal_data");
            if (currentMeal != null) {
                checkFavoriteStatus(currentMeal.getId());
                if (currentMeal.getInstructions() == null || currentMeal.getInstructions().isEmpty()) {
                    displayBasicInfo(currentMeal);
                    fetchFullMealDetails(currentMeal.getId());
                } else {
                    displayFullMeal(currentMeal);
                }
            }
        }
    }

    // --- NEW: LOGIC TO SHOW DAYS DIALOG ---
    private void showDayPickerDialog() {
        // 1. Calculate days from Today to Saturday
        List<String> daysList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);

        // Loop: Add today, then increment until we pass Saturday
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Standard week: Sunday=1 ... Saturday=7
        // We want to show days from Today until Saturday.
        // If today is Saturday, show just Saturday.

        while (currentDayOfWeek <= Calendar.SATURDAY) {
            Date date = calendar.getTime();
            daysList.add(dayFormat.format(date));

            // Move to next day
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Safety break if we loop back to Sunday (meaning we finished the week)
            if (currentDayOfWeek == Calendar.SUNDAY) break;
        }

        // Convert to CharSequence array for the Dialog
        final CharSequence[] daysArray = daysList.toArray(new CharSequence[0]);

        // 2. Show Dialog
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Plan this meal for:")
                .setItems(daysArray, (dialog, which) -> {
                    String selectedDay = daysArray[which].toString();
                    addToPlanner(selectedDay);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // --- NEW: SAVE TO DATABASE ---
    private void addToPlanner(String day) {
        if (currentMeal == null) return;

        // Create MealPlan object (Mapping Meal -> MealPlan)
        MealPlan plan = new MealPlan();
        plan.setDay(day);
        plan.setMealId(currentMeal.getId());
        plan.setMealName(currentMeal.getName());
        plan.setMealThumb(currentMeal.getThumbUrl());
        plan.setMealArea(currentMeal.getArea());

        // Save using Repository
        repository.addMealToPlan(plan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Toast.makeText(getContext(), "Added to " + day, Toast.LENGTH_SHORT).show(),
                        error -> {
                            Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("Planner", "Error adding plan", error);
                        }
                );
    }

    // --- EXISTING METHODS BELOW ---

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
                        }
                );
    }

    private void toggleFavorite() {
        if (currentMeal == null) return;

        if (isFavorite) {
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
        if (getContext() == null) return;
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite); // Ensure you have this drawable
            btnFavorite.setColorFilter(ContextCompat.getColor(requireContext(), R.color.error_red)); // Ensure color exists or use generic red
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
            btnFavorite.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black));
        }
    }

    private void fetchFullMealDetails(String mealId) {
        repository.getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                                currentMeal = response.getMeals().get(0);
                                displayFullMeal(currentMeal);
                                checkFavoriteStatus(currentMeal.getId());
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

        String videoId = getVideoId(meal.getYoutubeUrl());
        if (!videoId.isEmpty()) {
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
    }

    private List<Ingredient> extractIngredients(Meal meal) {
        List<Ingredient> ingredientsList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            try {
                Method ingredientMethod = meal.getClass().getMethod("getStrIngredient" + i);
                Object ingredientObj = ingredientMethod.invoke(meal);
                Method measureMethod = meal.getClass().getMethod("getStrMeasure" + i);
                Object measureObj = measureMethod.invoke(meal);

                String ingredientName = (ingredientObj != null) ? ingredientObj.toString().trim() : "";
                String measure = (measureObj != null) ? measureObj.toString().trim() : "";

                if (!ingredientName.isEmpty()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(ingredientName + "\n" + measure);
                    // You might want to build a proper URL for thumbnails here if needed
                    ingredient.setThumbnail("https://www.themealdb.com/images/ingredients/" + ingredientName + "-Small.png");
                    ingredientsList.add(ingredient);
                }
            } catch (Exception e) {
                Log.e("ExtractIngredients", "Error extracting ingredient " + i, e);
            }
        }
        return ingredientsList;
    }

    private String getVideoId(String url) {
        if (url == null || url.isEmpty()) return "";
        String videoId = "";
        try {
            if (url.contains("v=")) {
                String[] split = url.split("v=");
                if (split.length > 1) {
                    videoId = split[1];
                    int ampersandPosition = videoId.indexOf('&');
                    if (ampersandPosition != -1) {
                        videoId = videoId.substring(0, ampersandPosition);
                    }
                }
            } else if (url.contains("youtu.be/")) {
                String[] split = url.split("youtu.be/");
                if (split.length > 1) {
                    videoId = split[1];
                    int questionMark = videoId.indexOf('?');
                    if (questionMark != -1) {
                        videoId = videoId.substring(0, questionMark);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoId.trim();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (youTubePlayerView != null) youTubePlayerView.release();
    }
}