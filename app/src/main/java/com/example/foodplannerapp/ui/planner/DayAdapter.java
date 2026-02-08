package com.example.foodplannerapp.ui.planner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplannerapp.R;
import com.example.foodplannerapp.model.MealPlan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private List<String> next7Days = new ArrayList<>();
    private Map<String, List<MealPlan>> mealsMap;
    private PlanMealAdapter.OnDeleteClickListener deleteListener;
    private PlanMealAdapter.OnItemClickListener itemListener; // 1. Add listener field
    private Context context;

    // 2. Update Constructor
    public DayAdapter(Context context,
                      PlanMealAdapter.OnDeleteClickListener deleteListener,
                      PlanMealAdapter.OnItemClickListener itemListener) {
        this.context = context;
        this.deleteListener = deleteListener;
        this.itemListener = itemListener;
        generateNext7Days();
    }

    public void setMeals(Map<String, List<MealPlan>> mealsMap) {
        this.mealsMap = mealsMap;
        notifyDataSetChanged();
    }

    private void generateNext7Days() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            next7Days.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_plan, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        String dayName = next7Days.get(position);
        holder.tvDayName.setText(dayName);

        List<MealPlan> dayMeals = mealsMap != null ? mealsMap.get(dayName) : new ArrayList<>();
        if (dayMeals == null) dayMeals = new ArrayList<>();

        holder.tvMealCount.setText("(" + dayMeals.size() + " meals)");

        PlanMealAdapter innerAdapter = new PlanMealAdapter(dayMeals, deleteListener, itemListener);
        holder.rvMeals.setLayoutManager(new LinearLayoutManager(context));
        holder.rvMeals.setAdapter(innerAdapter);
    }

    @Override
    public int getItemCount() { return next7Days.size(); }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayName, tvMealCount;
        RecyclerView rvMeals;

        DayViewHolder(View v) {
            super(v);
            tvDayName = v.findViewById(R.id.tvDayName);
            tvMealCount = v.findViewById(R.id.tvMealCount);
            rvMeals = v.findViewById(R.id.rvDayMeals);
        }
    }
}