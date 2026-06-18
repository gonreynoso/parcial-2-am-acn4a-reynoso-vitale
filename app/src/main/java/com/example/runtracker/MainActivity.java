package com.example.runtracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
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
    private static final int PASOS_INICIALES = 0;
    private static final int PASOS_CORRIENDO = 105;
    private static final int RACHA_DIAS = 3;
    // Flat estimate (kcal per km); no user weight/age data exists yet for a personalized calculation.
    private static final double CALORIAS_POR_KM = 60;

    private LinearLayout navHome, navProfile;
    private MaterialCardView fabRun;

    private TextView txtPasos, txtObjetivoProgreso, txtDistancia, txtCalorias, txtDuracion;
    private ProgressBar progressObjetivo;
    private LinearLayout contenedorDinamico;

    private boolean estaCorriendo = false;

    private android.os.Handler handler = new android.os.Handler();
    private Runnable runnable;
    private int contadorPasos = PASOS_INICIALES;

    private LocationTracker locationTracker = new LocationTracker();
    private float distanciaMetros = 0f;
    private ActivityResultLauncher<String> permisoUbicacionLauncher;

    private int pasosAlIniciarSesion;
    private long inicioSesionMillis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dashboard
        txtPasos            = findViewById(R.id.txtPasos);
        txtObjetivoProgreso = findViewById(R.id.txtObjetivoProgreso);
        progressObjetivo    = findViewById(R.id.progressObjetivo);
        contenedorDinamico  = findViewById(R.id.contenedorDinamico);
        txtDistancia        = findViewById(R.id.txtDistancia);
        txtCalorias         = findViewById(R.id.txtCalorias);
        txtDuracion         = findViewById(R.id.txtDuracion);

        permisoUbicacionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                concedido -> {
                    if (concedido && estaCorriendo) {
                        locationTracker.start(this, this::onDistanciaActualizada);
                    }
                });

        loadSaludo();
        loadFrase();
        loadRacha();
        txtPasos.setText(String.valueOf(contadorPasos));
        actualizarObjetivo(PASOS_INICIALES);
        actualizarCalorias(0f);
        actualizarDuracion(0);

        setupNavbar();
        NavbarHelper.markActiveTab(this, NavbarHelper.Tab.HOME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (estaCorriendo) {
            locationTracker.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (estaCorriendo && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationTracker.start(this, this::onDistanciaActualizada);
        }
    }

    private void loadSaludo() {
        TextView txtSaludo = findViewById(R.id.txtSaludo);
        UserPreferences prefs = new UserPreferences(this);
        prefs.initializeJoinYearIfNeeded();
        txtSaludo.setText("Hola, " + prefs.getUsername() + " 👋");
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
          estaCorriendo = true;
            pasosAlIniciarSesion = contadorPasos;
            inicioSesionMillis = System.currentTimeMillis();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationTracker.start(this, this::onDistanciaActualizada);
            } else {
                permisoUbicacionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            runnable = new Runnable() {
                @Override
                public void run() {
                    if (contadorPasos < 10000) {
                        contadorPasos++;
                        txtPasos.setText(String.valueOf(contadorPasos));
                        actualizarObjetivo(contadorPasos);

                        long segundosTranscurridos = (System.currentTimeMillis() - inicioSesionMillis) / 1000;
                        actualizarDuracion(segundosTranscurridos);

                        handler.postDelayed(this, 1000);
                }
            }
        };
            handler.post(runnable);
    } else {
            estaCorriendo = false;
            handler.removeCallbacks(runnable);

            locationTracker.stop();

            int pasosSesion = contadorPasos - pasosAlIniciarSesion;
            long duracionSegundos = (System.currentTimeMillis() - inicioSesionMillis) / 1000;
            guardarCarrera(distanciaMetros / 1000, pasosSesion, duracionSegundos);

            distanciaMetros = 0f;
            actualizarDistancia(distanciaMetros);
            actualizarDuracion(0);

            contenedorDinamico.removeAllViews();

            agregarResumenHistorial();


        }
    }

    private void guardarCarrera(double distanciaKm, int pasos, long duracionSegundos) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        if (pasos <= 0 && distanciaKm <= 0) return;

        Map<String, Object> carrera = new HashMap<>();
        carrera.put("distanciaKm", distanciaKm);
        carrera.put("pasos", pasos);
        carrera.put("duracionSegundos", duracionSegundos);
        carrera.put("timestamp", FieldValue.serverTimestamp());

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("runs")
                .add(carrera);
    }

    private void onDistanciaActualizada(float metros) {
        runOnUiThread(() -> {
            distanciaMetros = metros;
            actualizarDistancia(distanciaMetros);
        });
    }

    private void actualizarDistancia(float metros) {
        double km = metros / 1000;
        txtDistancia.setText(String.format("%.2f km", km));
        actualizarCalorias(metros);
    }

    private void actualizarCalorias(float metros) {
        double calorias = (metros / 1000.0) * CALORIAS_POR_KM;
        txtCalorias.setText(String.format("%,d cal", Math.round(calorias)));
    }

    private void actualizarDuracion(long segundos) {
        long horas = segundos / 3600;
        long minutos = (segundos % 3600) / 60;
        long segs = segundos % 60;
        if (horas > 0) {
            txtDuracion.setText(String.format("%d:%02d:%02d", horas, minutos, segs));
        } else {
            txtDuracion.setText(String.format("%02d:%02d", minutos, segs));
        }
    }

    private void agregarResumenHistorial() {

        TextView tvHistorial = new TextView(this);


        tvHistorial.setText("🏃 Sesión finalizada: " + contadorPasos + " pasos.");


        tvHistorial.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white));


        tvHistorial.setBackgroundResource(R.drawable.bg_card);
        tvHistorial.setPadding(40, 40, 40, 40);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 30, 0, 0);
        tvHistorial.setLayoutParams(params);


        contenedorDinamico.addView(tvHistorial);
    }

    private void setupNavbar() {
        navHome    = findViewById(R.id.navHome);
        navProfile = findViewById(R.id.navProfile);
        fabRun     = findViewById(R.id.fabRun);

        navHome.setOnClickListener(v -> {});
        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));

        });
        fabRun.setOnClickListener(v -> gestionarInicio());
    }
}