package com.example.foodplannerapp.ui.favorites;

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

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private List<Meal> meals = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(Meal meal);
    }

    public FavoritesAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_search, parent, false); // Reusing your grid item layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.name.setText(meal.getName());
        Glide.with(holder.itemView).load(meal.getThumbUrl()).into(holder.img);
        holder.itemView.setOnClickListener(v -> listener.onClick(meal));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;

        ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.imgMeal);
            name = v.findViewById(R.id.tvMealName);
        }
    }
}