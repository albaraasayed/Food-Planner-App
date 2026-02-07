package com.example.foodplannerapp.ui.planner;

import com.example.foodplannerapp.data.repository.MealRepositoryImpl;
import com.example.foodplannerapp.model.MealPlan;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannerPresenter {
    private PlannerFragment view;
    private MealRepositoryImpl repository;

    public PlannerPresenter(PlannerFragment view, MealRepositoryImpl repository) {
        this.view = view;
        this.repository = repository;
    }

    public void getPlannedMeals() {
        repository.getPlan()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            // Group List by Date
                            Map<String, List<MealPlan>> grouped = new HashMap<>();
                            for (MealPlan m : meals) {
                                if (!grouped.containsKey(m.getDate())) {
                                    grouped.put(m.getDate(), new ArrayList<>());
                                }
                                grouped.get(m.getDate()).add(m);
                            }
                            view.showPlan(grouped);
                        },
                        error -> view.showError(error.getMessage())
                );
    }

    public void deleteMeal(MealPlan meal) {
        repository.removeMealFromPlan(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getPlannedMeals, error -> view.showError("Error deleting"));
    }
}