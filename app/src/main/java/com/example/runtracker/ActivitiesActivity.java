package com.example.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ActivitiesActivity extends AppCompatActivity {

    private LinearLayout navHome, navHistory, navStats, navProfile;
    private FloatingActionButton fabRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        RecyclerView rvActivities = findViewById(R.id.rvActivities);
        rvActivities.setLayoutManager(new LinearLayoutManager(this));
        rvActivities.setAdapter(new ActivityAdapter(ActivityData.getRecommended()));

        setupNavbar();
        NavbarHelper.markActiveTab(this, NavbarHelper.Tab.HISTORY);
    }

    private void setupNavbar() {
        navHome    = findViewById(R.id.navHome);
        navHistory = findViewById(R.id.navHistory);
        navStats   = findViewById(R.id.navStats);
        navProfile = findViewById(R.id.navProfile);
        fabRun     = findViewById(R.id.fabRun);

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        navHistory.setOnClickListener(v -> {});
        navStats.setOnClickListener(v -> {
            startActivity(new Intent(this, ProgressActivity.class));
            finish();
        });
        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
        fabRun.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}