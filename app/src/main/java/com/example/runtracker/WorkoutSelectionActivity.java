package com.example.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WorkoutSelectionActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView btnCrearEntrenamiento;
    private LinearLayout btnCorrer, btnSenderismo, btnBicicleta, btnCaminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection);

        btnBack = findViewById(R.id.btnBack);
        btnCrearEntrenamiento = findViewById(R.id.btnCrearEntrenamiento);
        btnCorrer = findViewById(R.id.btnCorrer);
        btnSenderismo = findViewById(R.id.btnSenderismo);
        btnBicicleta = findViewById(R.id.btnBicicleta);
        btnCaminar = findViewById(R.id.btnCaminar);

        btnBack.setOnClickListener(v -> finish());

        btnCrearEntrenamiento.setOnClickListener(v ->
                Toast.makeText(this, "Próximamente", Toast.LENGTH_SHORT).show());

        btnCorrer.setOnClickListener(v -> abrirTracking(getString(R.string.nav_run), R.drawable.ic_directions_run));
        btnSenderismo.setOnClickListener(v -> abrirTracking(getString(R.string.actividad_senderismo), R.drawable.ic_directions_walk));
        btnBicicleta.setOnClickListener(v -> abrirTracking(getString(R.string.actividad_bicicleta), R.drawable.ic_directions_bike));
        btnCaminar.setOnClickListener(v -> abrirTracking(getString(R.string.actividad_caminar), R.drawable.ic_directions_walk));
    }

    private void abrirTracking(String nombre, int icono) {
        Intent intent = new Intent(this, WorkoutTrackingActivity.class);
        intent.putExtra("nombre", nombre);
        intent.putExtra("icono", icono);
        startActivity(intent);
    }
}
