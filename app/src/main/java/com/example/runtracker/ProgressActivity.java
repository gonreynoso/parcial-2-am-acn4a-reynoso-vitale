package com.example.runtracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProgressActivity extends AppCompatActivity {

    private static final String[] DAYS = { "D", "L", "M", "M", "J", "V", "S" };

    private LinearLayout navHome, navHistory, navStats, navProfile;
    private FloatingActionButton fabRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        loadStats();
        buildWeeklyChart();
        loadRecentRuns();

        setupNavbar();
        NavbarHelper.markActiveTab(this, NavbarHelper.Tab.STATS);
    }

    private void loadStats() {
        RunHistoryData.Stats stats = RunHistoryData.getStats();
        ((TextView) findViewById(R.id.txtTotalKm)).setText(stats.totalKm);
        ((TextView) findViewById(R.id.txtTotalRuns)).setText(stats.totalRuns);
        ((TextView) findViewById(R.id.txtTotalTime)).setText(stats.totalTime);
    }

    private void buildWeeklyChart() {
        LinearLayout chart = findViewById(R.id.chartWeekly);
        int[] values = RunHistoryData.getWeeklyKm();

        int max = 1;
        for (int v : values) if (v > max) max = v;

        int neon = ContextCompat.getColor(this, R.color.neon_green);
        int gray = ContextCompat.getColor(this, R.color.icon_inactive);

        for (int i = 0; i < values.length; i++) {
            LinearLayout column = new LinearLayout(this);
            column.setOrientation(LinearLayout.VERTICAL);
            column.setGravity(android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL);
            LinearLayout.LayoutParams colParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            column.setLayoutParams(colParams);

            // Barra
            View bar = new View(this);
            int barHeight = (int) (dpToPx(90) * ((float) values[i] / max));
            if (values[i] == 0) barHeight = dpToPx(4);
            LinearLayout.LayoutParams barParams = new LinearLayout.LayoutParams(
                    dpToPx(14), barHeight);
            bar.setLayoutParams(barParams);
            bar.setBackgroundColor(values[i] == 0 ? gray : neon);

            // Label día
            TextView label = new TextView(this);
            label.setText(DAYS[i]);
            label.setTextColor(gray);
            label.setTextSize(11);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = dpToPx(6);
            label.setLayoutParams(lp);

            column.addView(bar);
            column.addView(label);
            chart.addView(column);
        }
    }

    private void loadRecentRuns() {
        RecyclerView rv = findViewById(R.id.rvRecentRuns);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RunHistoryAdapter(RunHistoryData.getRecent()));
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
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
        navHistory.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivitiesActivity.class));
            finish();
        });
        navStats.setOnClickListener(v -> {});
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