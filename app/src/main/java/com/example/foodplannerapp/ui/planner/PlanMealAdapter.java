package com.example.foodplannerapp.ui.planner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.foodplannerapp.R;
import com.example.foodplannerapp.model.MealPlan;
import java.util.ArrayList;
import java.util.List;

public class PlanMealAdapter extends RecyclerView.Adapter<PlanMealAdapter.ViewHolder> {
    private List<MealPlan> meals = new ArrayList<>();
    private OnDeleteClickListener listener;

    public interface OnDeleteClickListener {
        void onDelete(MealPlan meal);
    }

    public PlanMealAdapter(List<MealPlan> meals, OnDeleteClickListener listener) {
        this.meals = meals;
        this.listener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_planner_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealPlan meal = meals.get(position);
        holder.name.setText(meal.getMealName());
        holder.area.setText(meal.getMealArea());
        Glide.with(holder.itemView).load(meal.getMealThumb()).into(holder.img);

        holder.btnDelete.setOnClickListener(v -> listener.onDelete(meal));
    }

    @Override public int getItemCount() { return meals.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img; TextView name, area; ImageButton btnDelete;
        ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.imgPlanMeal);
            name = v.findViewById(R.id.tvPlanName);
            area = v.findViewById(R.id.tvPlanArea);
            btnDelete = v.findViewById(R.id.btnPlanDelete);
        }
    }
}