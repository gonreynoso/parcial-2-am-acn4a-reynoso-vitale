package com.example.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            boolean isLoggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;
            Class<?> destination = isLoggedIn ? MainActivity.class : LoginActivity.class;
            startActivity(new Intent(this, destination));
            finish();
        }, SPLASH_DURATION);
    }
}