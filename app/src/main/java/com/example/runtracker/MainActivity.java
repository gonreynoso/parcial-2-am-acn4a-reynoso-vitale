package com.example.runtracker;



import android.os.Bundle;

import android.view.View;

import android.view.animation.Animation;

import android.view.animation.AnimationUtils;

import android.widget.Button;

import android.widget.LinearLayout;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {



    private TextView txtPasos;

    private Button btnIniciar;

    private LinearLayout contenedorDinamico;

    private boolean estaCorriendo = false;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);





        txtPasos = findViewById(R.id.txtPasos);

        btnIniciar = findViewById(R.id.btnIniciar);

        contenedorDinamico = findViewById(R.id.contenedorDinamico);

        Animation animacion = AnimationUtils.loadAnimation(this, R.anim.aparecer);





        LinearLayout cardPasos = findViewById(R.id.cardPasos);

        btnIniciar.setOnClickListener(v -> gestionarInicio());

        cardPasos.startAnimation(animacion);

    }



    private void gestionarInicio() {

        if (!estaCorriendo) {



            txtPasos.setText(R.string._105);

            btnIniciar.setText(R.string.detener_actividad);





            TextView tvMensa = new TextView(this);

            tvMensa.setText(R.string.conector_gps);

            tvMensa.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white));

            tvMensa.setGravity(View.TEXT_ALIGNMENT_CENTER);





            contenedorDinamico.addView(tvMensa);



            estaCorriendo = true;

        } else {



            btnIniciar.setText(getString(R.string.btnIniciar));

            contenedorDinamico.removeAllViews();

            estaCorriendo = false;

        }

    }

}