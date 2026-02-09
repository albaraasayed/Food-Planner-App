package com.example.foodplannerapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;

import com.example.foodplannerapp.R;

public class AppAlert {
    public static void showLoginRequiredDialog(Context context, NavController navController) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_guest_alert, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnLogin = view.findViewById(R.id.btnLoginNow);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnLogin.setOnClickListener(v -> {
            dialog.dismiss();
            if (navController != null) navController.navigate(R.id.authScreen);
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public static void showOfflineAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_offline_alert, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnOk = view.findViewById(R.id.btnOk);
        if (btnOk != null) {
            btnOk.setOnClickListener(v -> dialog.dismiss());
        }
        dialog.show();
    }
}