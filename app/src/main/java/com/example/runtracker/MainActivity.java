package com.example.runtracker;

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

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // TODO: cargar fragment de inicio
                return true;
            } else if (id == R.id.nav_run) {
                // TODO: cargar fragment de correr
                return true;
            } else if (id == R.id.nav_profile) {
                // TODO: cargar fragment de perfil
                return true;
            }
            return false;
        });

        // Seleccionar inicio por defecto
        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}