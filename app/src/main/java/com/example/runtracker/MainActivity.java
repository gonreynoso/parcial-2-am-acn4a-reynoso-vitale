package com.example.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

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

    private View btnClima;

    private LinearLayout navHome, navProfile;
    private MaterialCardView fabRun;

    private TextView txtPasos, txtObjetivoProgreso;
    private ProgressBar progressObjetivo;
    private LinearLayout contenedorDinamico;

    private boolean estaCorriendo = false;

    private android.os.Handler handler = new android.os.Handler();
    private Runnable runnable;
    private int contadorPasos = PASOS_INICIALES;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BTN CLIMA
        btnClima = findViewById(R.id.btnClima);
        btnClima.setOnClickListener(v -> {
            // Viajamos a la nueva pantalla
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);
        });

        // Dashboard
        txtPasos            = findViewById(R.id.txtPasos);
        txtObjetivoProgreso = findViewById(R.id.txtObjetivoProgreso);
        progressObjetivo    = findViewById(R.id.progressObjetivo);
        contenedorDinamico  = findViewById(R.id.contenedorDinamico);

        loadSaludo();
        //loadFrase();
        loadRacha();
        actualizarObjetivo(PASOS_INICIALES);



        setupNavbar();
        NavbarHelper.markActiveTab(this, NavbarHelper.Tab.HOME);
    }

    private void loadSaludo() {
        TextView txtSaludo = findViewById(R.id.txtSaludo);
        UserPreferences prefs = new UserPreferences(this);
        prefs.initializeJoinYearIfNeeded();
        txtSaludo.setText("Hola, " + prefs.getUsername() + " 👋");
    }

    //private void loadFrase() {
      //  TextView txtFrase = findViewById(R.id.txtFrase);
       // String frase = FRASES[new Random().nextInt(FRASES.length)];
        //txtFrase.setText(frase);
    //}

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

            //TextView tvMensa = new TextView(this);
            //tvMensa.setText(R.string.conector_gps);
            //tvMensa.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white));
            //tvMensa.setGravity(View.TEXT_ALIGNMENT_CENTER);

            //contenedorDinamico.addView(tvMensa);

            runnable = new Runnable() {
                @Override
                public void run() {
                    if (contadorPasos < 10000) {
                        contadorPasos++;
                        txtPasos.setText(String.valueOf(contadorPasos));
                        actualizarObjetivo(contadorPasos);


                        handler.postDelayed(this, 1000);
                }
            }
        };
            handler.post(runnable);
    } else {
            estaCorriendo = false;
            handler.removeCallbacks(runnable);

            contenedorDinamico.removeAllViews();

            agregarResumenHistorial();


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