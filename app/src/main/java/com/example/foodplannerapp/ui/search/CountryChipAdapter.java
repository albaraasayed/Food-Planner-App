package com.example.foodplannerapp.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplannerapp.R;
import com.example.foodplannerapp.model.Country;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class CountryChipAdapter extends RecyclerView.Adapter<CountryChipAdapter.ViewHolder> {
    private List<Country> countries = new ArrayList<>();
    private final OnCountryClickListener listener;

    public interface OnCountryClickListener {
        void onCountryClick(String countryName);
    }

    public CountryChipAdapter(OnCountryClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<Country> countries) {
        this.countries = countries;
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
        Country country = countries.get(position);
        holder.chip.setText(country.getName());
        holder.chip.setOnClickListener(v -> listener.onCountryClick(country.getName()));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        ViewHolder(View v) {
            super(v);
            chip = v.findViewById(R.id.chipCategory);
        }
    }
}