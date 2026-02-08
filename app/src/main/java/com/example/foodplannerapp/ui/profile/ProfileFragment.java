package com.example.foodplannerapp.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.CredentialManager;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.foodplannerapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private ImageView imgProfile;
    private TextView tvName, tvEmail;
    private MaterialButton btnLogout;
    private ImageButton btnBack;

    private FirebaseAuth mAuth;
    private CredentialManager credentialManager; // New API

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
        credentialManager = CredentialManager.create(requireContext());

        initViews(view);
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
            tvName.setText((name != null && !name.isEmpty()) ? name : "User");
            tvEmail.setText(user.getEmail());

            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .circleCrop()
                        .into(imgProfile);
            }
        }
    }

    private void logoutUser(View view) {
        // 1. Sign out of Firebase
        mAuth.signOut();

        // 2. Clear Credential Manager State (Replaces GoogleSignInClient.signOut)
        ClearCredentialStateRequest request = new ClearCredentialStateRequest();
        Executor executor = Executors.newSingleThreadExecutor();

        credentialManager.clearCredentialStateAsync(
                request,
                null,
                executor,
                new androidx.credentials.CredentialManagerCallback<Void, androidx.credentials.exceptions.ClearCredentialException>() {
                    @Override
                    public void onResult(Void result) {
                        // 3. Navigate back to Auth on Main Thread
                        requireActivity().runOnUiThread(() -> {
                            Navigation.findNavController(view).navigate(R.id.action_profile_to_auth);
                            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onError(androidx.credentials.exceptions.ClearCredentialException e) {
                        Log.e("Profile", "Error clearing credentials", e);
                        // Navigate anyway, even if clearing state failed
                        requireActivity().runOnUiThread(() ->
                                Navigation.findNavController(view).navigate(R.id.action_profile_to_auth)
                        );
                    }
                }
        );
    }
}