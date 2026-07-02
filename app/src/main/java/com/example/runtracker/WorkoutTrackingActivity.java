package com.example.runtracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;
import java.util.Random;

public class WorkoutTrackingActivity extends AppCompatActivity {

    private ImageView ivActivityIcon;
    private TextView tvActivityName, tvDuracion, tvDistancia, tvRitmo, tvMeta;
    private MaterialButton btnPausa, btnDetener, btnReanudar;
    private LinearLayout contenedorPausado;

    private final Stopwatch stopwatch = new Stopwatch();
    private final RunRepository runRepository = new RunRepository();
    private final Random random = new Random();

    private ActivityType activityType;
    private float distanciaMetros = 0f;
    private long duracionSegundos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_tracking);

        ivActivityIcon = findViewById(R.id.ivActivityIcon);
        tvActivityName = findViewById(R.id.tvActivityName);
        tvDuracion = findViewById(R.id.tvDuracion);
        tvDistancia = findViewById(R.id.tvDistancia);
        tvRitmo = findViewById(R.id.tvRitmo);
        tvMeta = findViewById(R.id.tvMeta);
        btnPausa = findViewById(R.id.btnPausa);
        contenedorPausado = findViewById(R.id.contenedorPausado);
        btnDetener = findViewById(R.id.btnDetener);
        btnReanudar = findViewById(R.id.btnReanudar);

        activityType = resolveActivityType();
        tvActivityName.setText(activityType.nameRes);
        ivActivityIcon.setImageResource(activityType.iconRes);
        mostrarMeta();

        btnPausa.setOnClickListener(v -> pausar());
        btnReanudar.setOnClickListener(v -> reanudar());
        btnDetener.setOnClickListener(v -> detener());

        stopwatch.start(this::onTick);
    }

    private void mostrarMeta() {
        String goalType = getIntent().getStringExtra(WorkoutSelectionActivity.EXTRA_GOAL_TYPE);
        double goalValue = getIntent().getDoubleExtra(WorkoutSelectionActivity.EXTRA_GOAL_VALUE, 0);
        if (goalType == null || goalValue <= 0) {
            tvMeta.setVisibility(View.GONE);
            return;
        }
        String value = goalValue == Math.rint(goalValue)
                ? String.valueOf((int) goalValue)
                : String.format(Locale.getDefault(), "%.1f", goalValue);
        tvMeta.setText(Workout.GOAL_TIME.equals(goalType)
                ? getString(R.string.workout_goal_time, value)
                : getString(R.string.workout_goal_distance, value));
        tvMeta.setVisibility(View.VISIBLE);
    }

    private ActivityType resolveActivityType() {
        String name = getIntent().getStringExtra(WorkoutSelectionActivity.EXTRA_ACTIVITY_TYPE);
        try {
            return name != null ? ActivityType.valueOf(name) : ActivityType.RUN;
        } catch (IllegalArgumentException e) {
            return ActivityType.RUN;
        }
    }

    private void pausar() {
        stopwatch.pause();
        btnPausa.setVisibility(View.GONE);
        contenedorPausado.setVisibility(View.VISIBLE);
    }

    private void reanudar() {
        stopwatch.resume();
        contenedorPausado.setVisibility(View.GONE);
        btnPausa.setVisibility(View.VISIBLE);
    }

    private void detener() {
        stopwatch.stop();
        runRepository.save(activityType.key, distanciaMetros / 1000.0, 0, duracionSegundos);
        finish();
    }

    
    private void onTick(long segundos) {
        duracionSegundos = segundos;

        
        
        float jitter = 0.85f + random.nextFloat() * 0.30f;
        distanciaMetros += activityType.speedMetersPerSecond * jitter;

        tvDuracion.setText(formatDuracion(segundos));
        tvDistancia.setText(String.format(Locale.getDefault(), "%.2f", distanciaMetros / 1000.0));
        actualizarRitmo();
    }

    private void actualizarRitmo() {
        double km = distanciaMetros / 1000.0;
        if (km < 0.01) {
            tvRitmo.setText("--");
            return;
        }
        long segundosPorKm = Math.round(duracionSegundos / km);
        String pace = String.format(Locale.getDefault(), "%d:%02d", segundosPorKm / 60, segundosPorKm % 60);
        tvRitmo.setText(getString(R.string.ritmo_formato, pace));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopwatch.stop();
    }
}
