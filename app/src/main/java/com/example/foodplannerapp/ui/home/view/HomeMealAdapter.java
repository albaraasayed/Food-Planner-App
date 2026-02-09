package com.example.foodplannerapp.ui.home.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplannerapp.R;
import com.example.foodplannerapp.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class HomeMealAdapter extends RecyclerView.Adapter<HomeMealAdapter.ViewHolder> {
    private List<Meal> meals = new ArrayList<>();
    private final OnMealClickListener listener;

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    public HomeMealAdapter(OnMealClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Reusing the card layout from Search
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_search, parent, false);
        // Force width to be fixed for horizontal scrolling look
        view.getLayoutParams().width = 500;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.tvName.setText(meal.getName());
        Glide.with(holder.itemView).load(meal.getThumbUrl()).into(holder.imgMeal);

        holder.itemView.setOnClickListener(v -> listener.onMealClick(meal));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMeal;
        TextView tvName;

        ViewHolder(View v) {
            super(v);
            imgMeal = v.findViewById(R.id.imgMeal);
            tvName = v.findViewById(R.id.tvMealName);
        }
    }
}