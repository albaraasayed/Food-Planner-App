package com.example.foodplannerapp.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplannerapp.R;

public class SplashScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        LottieAnimationView lottie = view.findViewById(R.id.lottieSplash);

        lottie.playAnimation();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded()) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_splash_to_home);
            }
        }, 3000);

        return view;
    }
}
