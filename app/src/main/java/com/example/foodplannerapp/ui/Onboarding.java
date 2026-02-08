package com.example.foodplannerapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplannerapp.R;

public class Onboarding extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnNext = view.findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {
            SharedPreferences settings = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();

            Navigation.findNavController(v).navigate(R.id.action_onboarding_to_authScreen);
        });
    }
}