package com.example.runtracker;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private LinearLayout navHome, navProfile;
    private FloatingActionButton fabRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navHome    = findViewById(R.id.navHome);
        navProfile = findViewById(R.id.navProfile);
        fabRun     = findViewById(R.id.fabRun);

        navHome.setOnClickListener(v -> setSelected(0));
        fabRun.setOnClickListener(v -> setSelected(1));
        navProfile.setOnClickListener(v -> setSelected(2));

        setSelected(0);
    }

    private void setSelected(int index) {
        int active   = 0xFF00E5A0;
        int inactive = 0xFF888888;


    }
}