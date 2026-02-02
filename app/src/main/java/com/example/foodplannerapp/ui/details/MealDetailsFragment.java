package com.example.foodplannerapp.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.foodplannerapp.R;
import com.example.foodplannerapp.model.Meal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MealDetailsFragment extends Fragment {

    private ImageView detailMealImg;
    private TextView tvTitle, tvArea, tvInstructions;
    private WebView webViewVideo;
    private ImageButton btnBack, btnFavorite;
    private FloatingActionButton fabPlan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailMealImg = view.findViewById(R.id.detailMealImg);
        tvTitle = view.findViewById(R.id.tvMealTitle);
        tvArea = view.findViewById(R.id.tvMealArea);
        tvInstructions = view.findViewById(R.id.tvInstructions);
        webViewVideo = view.findViewById(R.id.webViewVideo);
        btnBack = view.findViewById(R.id.btnBack);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        fabPlan = view.findViewById(R.id.fabPlan);

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        btnFavorite.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Added to Favorites!", Toast.LENGTH_SHORT).show();
            // TODO: Implement Room DB logic here
        });

        fabPlan.setOnClickListener(v -> {
            // TODO: Open Dialog to select Day (Sat, Sun, etc.)
            Toast.makeText(getContext(), "Open Weekly Planner Dialog", Toast.LENGTH_SHORT).show();
        });

        if (getArguments() != null) {
            Meal meal = (Meal) getArguments().getSerializable("meal_data");
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

        // --- VIDEO LOGIC ---
        if (meal.getYoutubeUrl() != null && !meal.getYoutubeUrl().isEmpty()) {
            String videoId = getVideoId(meal.getYoutubeUrl());
            if (videoId != null) {
                // ADDED: ?playsinline=1 to keep video inside the app
                String embedUrl = "https://www.youtube.com/embed/" + videoId + "?playsinline=1";

                // Configure WebView
                webViewVideo.getSettings().setJavaScriptEnabled(true);

                // --- FIX FOR ERROR 153 ---
                webViewVideo.getSettings().setDomStorageEnabled(true);

                webViewVideo.setWebChromeClient(new WebChromeClient());
                webViewVideo.loadUrl(embedUrl);
            }
        } else {
            webViewVideo.setVisibility(View.GONE);
        }
    }

    private String getVideoId(String url) {
        if (url.contains("v=")) {
            int index = url.indexOf("v=");
            return url.substring(index + 2);
        }
        return null;
    }
}