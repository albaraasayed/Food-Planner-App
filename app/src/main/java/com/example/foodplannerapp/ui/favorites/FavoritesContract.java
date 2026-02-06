package com.example.foodplannerapp.ui.favorites;
import com.example.foodplannerapp.model.Meal;
import java.util.List;

public interface FavoritesContract {
    interface View {
        void showFavorites(List<Meal> meals);
        void showEmptyState();
        void showError(String msg);
    }
    interface Presenter {
        void getFavorites();
        void removeFavorite(Meal meal);
    }
}