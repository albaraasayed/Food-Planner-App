package com.example.foodplannerapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView; // Import TextView

import androidx.annotation.NonNull; // Import NonNull
import androidx.annotation.Nullable; // Import Nullable
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplannerapp.R;

public class AuthScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_authScreen_to_homeScreen);
        });

        Button btnSkip = view.findViewById(R.id.btnSkip);
        if(btnSkip != null) {
            btnSkip.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_authScreen_to_homeScreen);
            });
        }

        TextView tvSignUp = view.findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_authScreen_to_registerFragment);
        });
    }
}