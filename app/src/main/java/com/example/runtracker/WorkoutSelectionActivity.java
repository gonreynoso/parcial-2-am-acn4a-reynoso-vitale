package com.example.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WorkoutSelectionActivity extends AppCompatActivity {

    static final String EXTRA_ACTIVITY_TYPE = "activityType";

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

        btnCorrer.setOnClickListener(v -> abrirTracking(ActivityType.RUN));
        btnSenderismo.setOnClickListener(v -> abrirTracking(ActivityType.HIKE));
        btnBicicleta.setOnClickListener(v -> abrirTracking(ActivityType.BIKE));
        btnCaminar.setOnClickListener(v -> abrirTracking(ActivityType.WALK));
    }

    private void abrirTracking(ActivityType type) {
        Intent intent = new Intent(this, WorkoutTrackingActivity.class);
        intent.putExtra(EXTRA_ACTIVITY_TYPE, type.name());
        startActivity(intent);
    }
}
