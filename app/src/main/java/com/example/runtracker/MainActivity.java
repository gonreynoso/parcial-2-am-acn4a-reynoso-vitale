package com.example.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String[] FRASES = {
            "Hoy es un buen día para correr",
            "Un paso más cerca de tu meta",
            "La constancia vence al talento",
            "No cuentes los días, hacé que los días cuenten",
            "Tu único límite sos vos mismo",
            "El dolor es temporal, el orgullo es para siempre"
    };

    private static final int OBJETIVO_PASOS = 10000;
    private static final int PASOS_INICIALES = 97;
    private static final int PASOS_CORRIENDO = 105;
    private static final int RACHA_DIAS = 3;

    private TextView txtPasos, txtObjetivoProgreso;
    private ProgressBar progressObjetivo;
    private LinearLayout contenedorDinamico;
    private LinearLayout navHome, navHistory, navStats, navProfile;
    private FloatingActionButton fabRun;
    private boolean estaCorriendo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dashboard
        txtPasos            = findViewById(R.id.txtPasos);
        txtObjetivoProgreso = findViewById(R.id.txtObjetivoProgreso);
        progressObjetivo    = findViewById(R.id.progressObjetivo);
        contenedorDinamico  = findViewById(R.id.contenedorDinamico);

        loadSaludo();
        loadFrase();
        loadRacha();
        actualizarObjetivo(PASOS_INICIALES);

        LinearLayout cardPasos = findViewById(R.id.cardPasos);
        Animation animacion = AnimationUtils.loadAnimation(this, R.anim.aparecer);
        cardPasos.startAnimation(animacion);

        setupNavbar();
        NavbarHelper.markActiveTab(this, NavbarHelper.Tab.HOME);
    }

    private void loadSaludo() {
        TextView txtSaludo = findViewById(R.id.txtSaludo);
        txtSaludo.setText("Hola, Gonzalo 👋");
    }

    private void loadFrase() {
        TextView txtFrase = findViewById(R.id.txtFrase);
        String frase = FRASES[new Random().nextInt(FRASES.length)];
        txtFrase.setText(frase);
    }

    private void loadRacha() {
        TextView txtRachaTitulo = findViewById(R.id.txtRachaTitulo);
        txtRachaTitulo.setText("Racha de " + RACHA_DIAS + " días");
    }

    private void actualizarObjetivo(int pasos) {
        progressObjetivo.setProgress(pasos);
        txtObjetivoProgreso.setText(String.format("%d / %,d", pasos, OBJETIVO_PASOS));
    }

    private void gestionarInicio() {
        if (!estaCorriendo) {
            txtPasos.setText(String.valueOf(PASOS_CORRIENDO));
            actualizarObjetivo(PASOS_CORRIENDO);

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

    private void setupNavbar() {
        navHome    = findViewById(R.id.navHome);
        navHistory = findViewById(R.id.navHistory);
        navStats   = findViewById(R.id.navStats);
        navProfile = findViewById(R.id.navProfile);
        fabRun     = findViewById(R.id.fabRun);

        navHome.setOnClickListener(v -> {});
        navHistory.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivitiesActivity.class));
            finish();
        });
        navStats.setOnClickListener(v -> {
            startActivity(new Intent(this, ProgressActivity.class));
            finish();
        });
        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
        fabRun.setOnClickListener(v -> gestionarInicio());
    }
}