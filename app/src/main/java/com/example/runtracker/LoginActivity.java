package com.example.runtracker;

import android.content.Intent;
import android.widget.Toast;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private MaterialButton btnAuth, btnGoogle, btnGithub;
    private TextView tvToggleMode, tvAuthError, tvAuthTitle, tvAuthSubtitle, tvForgotPassword;

    private FirebaseAuth auth;
    private boolean isLoginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        inputEmail      = findViewById(R.id.inputEmail);
        inputPassword   = findViewById(R.id.inputPassword);
        btnAuth         = findViewById(R.id.btnAuth);
        btnGoogle       = findViewById(R.id.btnGoogle);
        btnGithub       = findViewById(R.id.btnGithub);
        tvToggleMode    = findViewById(R.id.tvToggleMode);
        tvAuthError     = findViewById(R.id.tvAuthError);
        tvAuthTitle     = findViewById(R.id.tvAuthTitle);
        tvAuthSubtitle  = findViewById(R.id.tvAuthSubtitle);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        updateModeUI();

        btnAuth.setOnClickListener(v -> handleAuth());

        tvToggleMode.setOnClickListener(v -> {
            isLoginMode = !isLoginMode;
            tvAuthError.setVisibility(View.GONE);
            updateModeUI();
        });

        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());

        btnGoogle.setOnClickListener(v ->
                Toast.makeText(this, "Google Sign-In próximamente", Toast.LENGTH_SHORT).show());
        btnGithub.setOnClickListener(v ->
                Toast.makeText(this, "GitHub Sign-In próximamente", Toast.LENGTH_SHORT).show());
    }

    private void updateModeUI() {
        if (isLoginMode) {
            tvAuthTitle.setText(R.string.auth_title_login);
            tvAuthSubtitle.setText(R.string.auth_subtitle_login);
            btnAuth.setText(R.string.auth_btn_login);
            tvToggleMode.setText(R.string.auth_toggle_register);
            tvForgotPassword.setVisibility(View.VISIBLE);
        } else {
            tvAuthTitle.setText(R.string.auth_title_register);
            tvAuthSubtitle.setText(R.string.auth_subtitle_register);
            btnAuth.setText(R.string.auth_btn_register);
            tvToggleMode.setText(R.string.auth_toggle_login);
            tvForgotPassword.setVisibility(View.GONE);
        }
    }

    private void handleForgotPassword() {
        String email = inputEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            showError("Ingresá tu email para recuperar la contraseña");
            return;
        }
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, R.string.auth_reset_sent, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> showError(e.getMessage()));
    }

    private void handleAuth() {
        String email    = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showError("Correo electrónico requerido");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showError("Contraseña requerida");
            return;
        }

        tvAuthError.setVisibility(View.GONE);
        btnAuth.setEnabled(false);

        if (isLoginMode) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> navigateToMain())
                    .addOnFailureListener(e -> {
                        showError(e.getMessage());
                        btnAuth.setEnabled(true);
                    });
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> {
                        String uid = result.getUser().getUid();
                        // Use the part before @ as the initial display name
                        String username = email.contains("@")
                                ? email.substring(0, email.indexOf('@'))
                                : email;
                        createUserDocument(uid, username);
                    })
                    .addOnFailureListener(e -> {
                        showError(e.getMessage());
                        btnAuth.setEnabled(true);
                    });
        }
    }

    private void createUserDocument(String uid, String username) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("joinYear", Calendar.getInstance().get(Calendar.YEAR));

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .set(data)
                .addOnSuccessListener(unused -> {
                    // Seed local prefs so ProfileActivity has something before Firestore loads
                    new UserPreferences(this).setUsername(username);
                    navigateToMain();
                })
                .addOnFailureListener(e -> {
                    // Auth succeeded; navigate anyway and let profile load from Firestore later
                    navigateToMain();
                });
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showError(String message) {
        tvAuthError.setText(message);
        tvAuthError.setVisibility(View.VISIBLE);
    }
}
