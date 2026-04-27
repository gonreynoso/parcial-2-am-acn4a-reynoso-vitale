package com.example.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvJoinDate, tvTotalKm, tvTotalRuns, tvBestTime, tvAvatarInitial;
    private MaterialButton btnEditProfile;
    private LinearLayout navHome, navProfile;
    private MaterialCardView fabRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bindViews();
        loadProfile();
        setupNavbar();
        NavbarHelper.markActiveTab(this, NavbarHelper.Tab.PROFILE);

        btnEditProfile.setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class))
        );
    }
    protected void onResume() {
        super.onResume();
        loadProfile();
    }

    private void bindViews() {
        tvUsername  = findViewById(R.id.tvUsername);
        tvJoinDate  = findViewById(R.id.tvJoinDate);
        tvTotalKm   = findViewById(R.id.tvTotalKm);
        tvTotalRuns = findViewById(R.id.tvTotalRuns);
        tvBestTime  = findViewById(R.id.tvBestTime);
        navHome    = findViewById(R.id.navHome);
        navProfile = findViewById(R.id.navProfile);
        fabRun     = findViewById(R.id.fabRun);
        btnEditProfile  = findViewById(R.id.btnEditProfile);
        tvAvatarInitial = findViewById(R.id.tvAvatarInitial);
    }

    private void loadProfile() {
        UserPreferences prefs = new UserPreferences(this);
        prefs.initializeJoinYearIfNeeded();

        String username = prefs.getUsername();
        tvUsername.setText(username);
        tvAvatarInitial.setText(getInitial(username));
        tvJoinDate.setText(getString(R.string.profile_join_date, prefs.getJoinYear()));
        tvTotalKm.setText(getString(R.string.stats_value_km));
        tvTotalRuns.setText(getString(R.string.stats_value_runs));
        tvBestTime.setText(getString(R.string.stats_value_time));
    }

    private String getInitial(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        return String.valueOf(name.trim().charAt(0)).toUpperCase();
    }

    private void setupNavbar() {
        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        navProfile.setOnClickListener(v -> {});
        fabRun.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}