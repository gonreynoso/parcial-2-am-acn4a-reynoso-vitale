package com.example.runtracker;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private TextView txtPasos;
    private LinearLayout contenedorDinamico;
    private LinearLayout navHome, navHistory, navStats, navProfile;
    private FloatingActionButton fabRun;
    private boolean estaCorriendo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pantalla principal
        txtPasos = findViewById(R.id.txtPasos);
        contenedorDinamico = findViewById(R.id.contenedorDinamico);
        LinearLayout cardPasos = findViewById(R.id.cardPasos);

        Animation animacion = AnimationUtils.loadAnimation(this, R.anim.aparecer);
        cardPasos.startAnimation(animacion);

        // Navbar + FAB
        navHome    = findViewById(R.id.navHome);
        navHistory = findViewById(R.id.navHistory);
        navStats   = findViewById(R.id.navStats);
        navProfile = findViewById(R.id.navProfile);
        fabRun     = findViewById(R.id.fabRun);

        navHome.setOnClickListener(v -> {});
        navHistory.setOnClickListener(v -> {});
        navStats.setOnClickListener(v -> {});
        navProfile.setOnClickListener(v -> {});
        fabRun.setOnClickListener(v -> gestionarInicio());
    }

    private void gestionarInicio() {
        if (!estaCorriendo) {
            txtPasos.setText(R.string._105);

            TextView tvMensa = new TextView(this);
            tvMensa.setText(R.string.conector_gps);
            tvMensa.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white));
            tvMensa.setGravity(View.TEXT_ALIGNMENT_CENTER);

            contenedorDinamico.addView(tvMensa);
            estaCorriendo = true;
        } else {
            contenedorDinamico.removeAllViews();
            estaCorriendo = false;
        }
    }
}