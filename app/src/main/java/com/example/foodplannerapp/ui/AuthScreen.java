package com.example.foodplannerapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.CredentialManager;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplannerapp.R;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuthScreen extends Fragment {

    private FirebaseAuth mAuth;
    private TextInputEditText etEmail, etPassword;
    private CredentialManager credentialManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        credentialManager = CredentialManager.create(requireContext()); // Initialize

        etEmail = view.findViewById(R.id.etLoginEmail);
        etPassword = view.findViewById(R.id.etLoginPassword);
        Button btnLogin = view.findViewById(R.id.btnLogin);
        Button btnGoogle = view.findViewById(R.id.btnGoogle);
        Button btnSkip = view.findViewById(R.id.btnSkip);
        TextView tvSignUp = view.findViewById(R.id.tvSignUp);

        // --- Email Login ---
        btnLogin.setOnClickListener(v -> {
            String email = (etEmail.getText() != null) ? etEmail.getText().toString().trim() : "";
            String password = (etPassword.getText() != null) ? etPassword.getText().toString().trim() : "";

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password, v);
            }
        });

        // --- Google Login (New Way) ---
        if (btnGoogle != null) {
            btnGoogle.setOnClickListener(v -> signInWithGoogle());
        }

        if (btnSkip != null) {
            btnSkip.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_authScreen_to_homeScreen));
        }

        tvSignUp.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_authScreen_to_registerFragment));
    }

    private void loginUser(String email, String password, View view) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Navigation.findNavController(view).navigate(R.id.action_authScreen_to_homeScreen);
                    } else {
                        Toast.makeText(getContext(), "Auth Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        // 1. Setup the Google ID Option
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false) // Allow user to pick any account
                .setServerClientId(getString(R.string.default_web_client_id))
                .setAutoSelectEnabled(false) // <--- CHANGE THIS TO FALSE
                .build();

        // 2. Create the Request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        // 3. Launch Credential Manager
        Executor executor = Executors.newSingleThreadExecutor();

        credentialManager.getCredentialAsync(
                requireContext(),
                request,
                null,
                executor,
                new androidx.credentials.CredentialManagerCallback<GetCredentialResponse, androidx.credentials.exceptions.GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleSignIn(result);
                    }

                    @Override
                    public void onError(androidx.credentials.exceptions.GetCredentialException e) {
                        Log.e("Auth", "Error: " + e.getMessage());
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
                    }
                }
        );
    }

    private void handleSignIn(GetCredentialResponse response) {
        CustomCredential credential = (CustomCredential) response.getCredential();

        if (credential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            try {
                // Extract the ID Token
                GoogleIdTokenCredential googleIdToken = GoogleIdTokenCredential.createFrom(credential.getData());
                String idToken = googleIdToken.getIdToken();

                // Authenticate with Firebase
                firebaseAuthWithGoogle(idToken);
            } catch (Exception e) {
                Log.e("Auth", "Invalid Google ID Token", e);
            }
        } else {
            Log.e("Auth", "Unexpected credential type");
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        if (getView() != null) {
                            Navigation.findNavController(getView()).navigate(R.id.action_authScreen_to_homeScreen);
                        }
                    } else {
                        Toast.makeText(getContext(), "Firebase Auth Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}