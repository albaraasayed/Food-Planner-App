package com.example.foodplannerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            bottomNavigationView.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                boolean isGuest = (mAuth.getCurrentUser() == null);
                boolean isOnline = isNetworkAvailable();

                if (isGuest) {
                    // --- GUEST MODE ---
                    if (id == R.id.homeFragment || id == R.id.searchFragment) {
                        // Guest trying to access Home/Search while Offline -> Block
                        if (!isOnline) {
                            showOfflineAlert();
                            return false;
                        }
                        // Guest + Online -> Allow
                        return NavigationUI.onNavDestinationSelected(item, navController);
                    } else {
                        // Guest trying to access Protected Screens -> Block
                        showLoginRequiredDialog();
                        return false;
                    }
                } else {
                    // --- LOGGED IN USER ---
                    if (isOnline) {
                        // User + Online -> Allow Everything
                        return NavigationUI.onNavDestinationSelected(item, navController);
                    } else {
                        // User + Offline
                        if (id == R.id.favoritesFragment || id == R.id.plannerFragment) {
                            // Allow Offline Screens
                            return NavigationUI.onNavDestinationSelected(item, navController);
                        } else {
                            // Block Online Screens (Home/Search/Profile)
                            showOfflineAlert();
                            return false;
                        }
                    }
                }
            });

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.authScreen ||
                        destination.getId() == R.id.registerFragment ||
                        destination.getId() == R.id.splashScreen ||
                        destination.getId() == R.id.onboardingFragment) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    // --- UPDATED: Stronger Network Check ---
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) return false;
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            // Fallback for older Android versions
            android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }

    private void showLoginRequiredDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_guest_alert, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnLogin = view.findViewById(R.id.btnLoginNow);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnLogin.setOnClickListener(v -> {
            dialog.dismiss();
            if (navController != null) navController.navigate(R.id.authScreen);
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showOfflineAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_offline_alert, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnOk = view.findViewById(R.id.btnOk);
        if (btnOk != null) {
            btnOk.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }
}