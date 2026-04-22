package com.example.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvJoinDate, tvTotalKm, tvTotalRuns, tvBestTime;
    private LinearLayout navHome, navHistory, navStats, navProfile;
    private FloatingActionButton fabRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bindViews();
        loadProfile();
        setupNavbar();
        NavbarHelper.markActiveTab(this, NavbarHelper.Tab.PROFILE);
        TextView tvEditProfile = findViewById(R.id.tvEditProfile);
        tvEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, EditProfileActivity.class));
        });
    }

    private void bindViews() {
        tvUsername  = findViewById(R.id.tvUsername);
        tvJoinDate  = findViewById(R.id.tvJoinDate);
        tvTotalKm   = findViewById(R.id.tvTotalKm);
        tvTotalRuns = findViewById(R.id.tvTotalRuns);
        tvBestTime  = findViewById(R.id.tvBestTime);

        navHome    = findViewById(R.id.navHome);
        navHistory = findViewById(R.id.navHistory);
        navStats   = findViewById(R.id.navStats);
        navProfile = findViewById(R.id.navProfile);
        fabRun     = findViewById(R.id.fabRun);
    }

    private void loadProfile() {
        UserPreferences prefs = new UserPreferences(this);
        prefs.initializeJoinYearIfNeeded();

        tvUsername.setText(prefs.getUsername());
        tvJoinDate.setText(getString(R.string.profile_join_date, prefs.getJoinYear()));
        tvTotalKm.setText(getString(R.string.stats_value_km));
        tvTotalRuns.setText(getString(R.string.stats_value_runs));
        tvBestTime.setText(getString(R.string.stats_value_time));
    }

    private void setupNavbar() {
        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        navHistory.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivitiesActivity.class));
            finish();
        });
        navStats.setOnClickListener(v -> {
            startActivity(new Intent(this, ProgressActivity.class));
            finish();
        });
        navProfile.setOnClickListener(v -> {});
        fabRun.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}