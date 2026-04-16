package com.example.runtracker;

import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNav);

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        0xFF00E5A0,
                        0xFF888888
                }
        );
        bottomNav.setItemTextColor(colorStateList);
        bottomNav.setItemIconTintList(colorStateList);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_run) {
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });

        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}