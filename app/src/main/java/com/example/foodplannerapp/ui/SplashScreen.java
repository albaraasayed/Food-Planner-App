package com.example.foodplannerapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplannerapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Handler().postDelayed(() -> {
            if (getContext() != null) {
                checkNavigationFlow(view);
            }
        }, 3000);
    }

    private void checkNavigationFlow(View view) {
        SharedPreferences settings = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        boolean isFirstTime = settings.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            Navigation.findNavController(view).navigate(R.id.action_splashScreen_to_onboarding);
        } else {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Navigation.findNavController(view).navigate(R.id.action_splashScreen_to_home);
            } else {
                Navigation.findNavController(view).navigate(R.id.action_splashScreen_to_auth);
            }
        }
    }
}