package com.example.foodplannerapp.ui.search.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplannerapp.R;
import com.example.foodplannerapp.model.Ingredient;
import com.google.android.material.chip.Chip;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

public class IngredientChipAdapter extends RecyclerView.Adapter<IngredientChipAdapter.ViewHolder> {
    private List<Ingredient> ingredients = new ArrayList<>();
    private final OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(String ingredientName);
    }

    public IngredientChipAdapter(OnIngredientClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient item = ingredients.get(position);
        holder.chip.setText(item.getName());

        Glide.with(holder.itemView.getContext())
                .load(item.getThumbnail())
                .into(new CustomTarget<android.graphics.drawable.Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull android.graphics.drawable.Drawable resource, @Nullable Transition<? super android.graphics.drawable.Drawable> transition) {
                        holder.chip.setChipIcon(resource);
                        holder.chip.setChipIconVisible(true);
                    }

                    @Override
                    public void onLoadCleared(@Nullable android.graphics.drawable.Drawable placeholder) {
                    }
                });

        holder.chip.setOnClickListener(v -> listener.onIngredientClick(item.getName()));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        ViewHolder(View v) {
            super(v);
            chip = v.findViewById(R.id.chipCategory);
        }
    }
}