package com.example.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import java.util.Locale;
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
    private static final int MAX_RUNS = 200;
    // Flat estimate (kcal per km). Weight is available now and could personalize this later.
    private static final double CALORIAS_POR_KM = 60;
    // Average stride length to simulate steps from real distance.
    private static final double STRIDE_METERS = 0.75;

    private final RunRepository runRepository = new RunRepository();

    private LinearLayout navHome, navProfile;
    private MaterialCardView fabRun;

    private TextView txtPasos, txtObjetivoProgreso, txtDistancia, txtCalorias, txtDuracion,
            txtCarrerasHoy, txtRachaTitulo, txtSaludo;
    private ProgressBar progressObjetivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSaludo           = findViewById(R.id.txtSaludo);
        txtPasos            = findViewById(R.id.txtPasos);
        txtObjetivoProgreso = findViewById(R.id.txtObjetivoProgreso);
        progressObjetivo    = findViewById(R.id.progressObjetivo);
        txtDistancia        = findViewById(R.id.txtDistancia);
        txtCalorias         = findViewById(R.id.txtCalorias);
        txtDuracion         = findViewById(R.id.txtDuracion);
        txtCarrerasHoy      = findViewById(R.id.txtCarrerasHoy);
        txtRachaTitulo      = findViewById(R.id.txtRachaTitulo);

        loadFrase();
        setupNavbar();
        NavbarHelper.markActiveTab(this, NavbarHelper.Tab.HOME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSaludo();
        cargarDashboard();
    }

    private void cargarDashboard() {
        runRepository.loadRuns(MAX_RUNS, runs -> {
            DashboardStats stats = DashboardStats.from(runs);

            int pasos = (int) Math.round(stats.todayKm * 1000 / STRIDE_METERS);
            txtPasos.setText(String.valueOf(pasos));
            actualizarObjetivo(pasos);
            txtDistancia.setText(String.format(Locale.getDefault(), "%.2f km", stats.todayKm));
            txtCalorias.setText(String.format(Locale.getDefault(), "%,d cal",
                    Math.round(stats.todayKm * CALORIAS_POR_KM)));
            txtDuracion.setText(formatDuracion(stats.todayDurationSeconds));
            txtCarrerasHoy.setText(String.valueOf(stats.todayRunCount));
            txtRachaTitulo.setText("Racha de " + stats.streakDays + " días");
        });
    }

    private void loadSaludo() {
        UserPreferences prefs = new UserPreferences(this);
        prefs.initializeJoinYearIfNeeded();
        txtSaludo.setText("Hola, " + prefs.getUsername() + " 👋");
    }

    private void loadFrase() {
        TextView txtFrase = findViewById(R.id.txtFrase);
        txtFrase.setText(FRASES[new Random().nextInt(FRASES.length)]);
    }

    private void actualizarObjetivo(int pasos) {
        progressObjetivo.setProgress(Math.min(pasos, OBJETIVO_PASOS));
        txtObjetivoProgreso.setText(String.format(Locale.getDefault(), "%,d / %,d", pasos, OBJETIVO_PASOS));
    }

    private String formatDuracion(long segundos) {
        long horas = segundos / 3600;
        long minutos = (segundos % 3600) / 60;
        long segs = segundos % 60;
        if (horas > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", horas, minutos, segs);
        }
        return String.format(Locale.getDefault(), "%02d:%02d", minutos, segs);
    }

    private void setupNavbar() {
        navHome    = findViewById(R.id.navHome);
        navProfile = findViewById(R.id.navProfile);
        fabRun     = findViewById(R.id.fabRun);

        navHome.setOnClickListener(v -> {});
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        fabRun.setOnClickListener(v -> startActivity(new Intent(this, WorkoutSelectionActivity.class)));
    }
}
