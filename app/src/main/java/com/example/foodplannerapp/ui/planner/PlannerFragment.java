package com.example.foodplannerapp.ui.planner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodplannerapp.R;
import com.example.foodplannerapp.data.config.RetrofitClient;
import com.example.foodplannerapp.data.local.local_datasource_implementation.MealLocalDataSourceImpl;
import com.example.foodplannerapp.data.remote.remote_datasource_implementation.MealRemoteDataSourceImpl;
import com.example.foodplannerapp.data.repository.MealRepositoryImpl;
import com.example.foodplannerapp.model.MealPlan;
import java.util.List;
import java.util.Map;

public class PlannerFragment extends Fragment {

    private RecyclerView rvPlanner;
    private DayAdapter adapter;
    private PlannerPresenter presenter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_planner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPlanner = view.findViewById(R.id.rvPlanner);
        rvPlanner.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup Presenter
        presenter = new PlannerPresenter(this, MealRepositoryImpl.getInstance(
                MealRemoteDataSourceImpl.getInstance(RetrofitClient.getService()),
                MealLocalDataSourceImpl.getInstance(getContext())
        ));

        // Setup Adapter
        adapter = new DayAdapter(getContext(), meal -> presenter.deleteMeal(meal));
        rvPlanner.setAdapter(adapter);

        presenter.getPlannedMeals();
    }

    public void showPlan(Map<String, List<MealPlan>> mealsMap) {
        adapter.setMeals(mealsMap);
    }

    public void showError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}