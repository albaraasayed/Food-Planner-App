package com.example.foodplannerapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.foodplannerapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private ImageView imgProfile;
    private TextView tvName, tvEmail;
    private MaterialButton btnLogout;
    private ImageButton btnBack;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        initViews(view);
        setupGoogleClient();

        loadUserProfile();

        btnLogout.setOnClickListener(v -> logoutUser(v));

        btnBack.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp()
        );
    }

    private void initViews(View view) {
        imgProfile = view.findViewById(R.id.imgProfile);
        tvName = view.findViewById(R.id.tvUserName);
        tvEmail = view.findViewById(R.id.tvUserEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnBack = view.findViewById(R.id.btnBack);
    }

    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            if (name != null && !name.isEmpty()) {
                tvName.setText(name);
            } else {
                tvName.setText("User");
            }

            tvEmail.setText(user.getEmail());

            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.ic_person) // Default icon
                        .error(R.drawable.ic_person)
                        .circleCrop()
                        .into(imgProfile);
            }
        }
    }

    private void setupGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    private void logoutUser(View view) {
        mAuth.signOut();

        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            Navigation.findNavController(view).navigate(R.id.action_profile_to_auth);

            // Or if you want to clear the back stack so user can't go back:
            /*
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.homeScreen, true) // Clear history
                    .build();
            Navigation.findNavController(view).navigate(R.id.authScreen, null, navOptions);
            */

            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
        });
    }
}