package com.example.foodplannerapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplannerapp.R;
import com.example.foodplannerapp.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categories;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Reuse your existing 'item_ingredient' layout or create a new 'item_category'
        // Ensure that layout has an ImageView (imgIngredient) and TextView (tvIngredientName)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);

        // CORRECTED: Using getName() based on your Category model
        holder.tvName.setText(category.getName());

        // CORRECTED: Using getThumbnail() based on your Category model
        Glide.with(holder.itemView.getContext())
                .load(category.getThumbnail())
                .placeholder(R.drawable.ic_launcher_background) // Add a placeholder if needed
                .into(holder.imgThumbnail);
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Make sure these IDs exist in 'item_ingredient.xml'
            imgThumbnail = itemView.findViewById(R.id.imgIngredient);
            tvName = itemView.findViewById(R.id.tvIngredientName);
        }
    }
}