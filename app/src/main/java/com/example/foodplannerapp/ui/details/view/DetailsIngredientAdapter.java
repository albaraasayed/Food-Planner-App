package com.example.foodplannerapp.ui.details.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.foodplannerapp.R;
import com.example.foodplannerapp.model.Ingredient;
import java.util.ArrayList;
import java.util.List;

public class DetailsIngredientAdapter extends RecyclerView.Adapter<DetailsIngredientAdapter.ViewHolder> {
    private List<Ingredient> list = new ArrayList<>();

    public void setList(List<Ingredient> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient_circle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient item = list.get(position);
        holder.name.setText(item.getName());

        Glide.with(holder.itemView)
                .load(item.getThumbnail())
                .circleCrop()
                .into(holder.img);
    }

    @Override public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img; TextView name;
        ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.imgIngredient);
            name = v.findViewById(R.id.tvIngredientName);
        }
    }
}