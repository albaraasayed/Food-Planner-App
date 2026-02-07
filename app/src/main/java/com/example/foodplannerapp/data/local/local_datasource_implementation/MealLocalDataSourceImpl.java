package com.example.foodplannerapp.data.local.local_datasource_implementation;

import android.content.Context;
import com.example.foodplannerapp.data.config.AppDatabase;
import com.example.foodplannerapp.data.local.dao.MealDAO;
import com.example.foodplannerapp.data.local.dao.MealPlanDAO; // 1. Import DAO
import com.example.foodplannerapp.data.local.local_datasource_interface.MealLocalDataSource;
import com.example.foodplannerapp.model.Meal;
import com.example.foodplannerapp.model.MealPlan; // 2. Import Model
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class MealLocalDataSourceImpl implements MealLocalDataSource {

    private MealDAO dao;
    private MealPlanDAO planDao;
    private static MealLocalDataSourceImpl instance = null;

    private MealLocalDataSourceImpl(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.dao = db.mealDAO();
        this.planDao = db.mealPlanDAO();
    }

    public static MealLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new MealLocalDataSourceImpl(context);
        }
        return instance;
    }

    // --- FAVORITES ---
    @Override
    public Completable insertFavorite(Meal meal) {
        return dao.insertMeal(meal);
    }

    @Override
    public Completable deleteFavorite(Meal meal) {
        return dao.deleteMeal(meal);
    }

    @Override
    public Single<List<Meal>> getFavorites() {
        return dao.getAllFavorites();
    }

    @Override
    public Completable insertMealPlan(MealPlan mealPlan) {
        return planDao.insertMealPlan(mealPlan);
    }

    @Override
    public Completable deleteMealPlan(MealPlan mealPlan) {
        return planDao.deleteMealPlan(mealPlan);
    }

    @Override
    public Single<List<MealPlan>> getAllPlans() {
        return planDao.getAllPlannedMeals();
    }
}