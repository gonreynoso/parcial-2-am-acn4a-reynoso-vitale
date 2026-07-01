package com.example.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class WorkoutSelectionActivity extends AppCompatActivity {

    static final String EXTRA_ACTIVITY_TYPE = "activityType";
    static final String EXTRA_GOAL_TYPE = "goalType";
    static final String EXTRA_GOAL_VALUE = "goalValue";

    private final WorkoutRepository workoutRepository = new WorkoutRepository();
    private LinearLayout contenedorEntrenamientos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_selection);

        ImageView btnBack = findViewById(R.id.btnBack);
        TextView btnCrearEntrenamiento = findViewById(R.id.btnCrearEntrenamiento);
        contenedorEntrenamientos = findViewById(R.id.contenedorEntrenamientos);

        btnBack.setOnClickListener(v -> finish());
        btnCrearEntrenamiento.setOnClickListener(v ->
                startActivity(new Intent(this, CreateWorkoutActivity.class)));

        findViewById(R.id.btnCorrer).setOnClickListener(v -> abrirTracking(ActivityType.RUN));
        findViewById(R.id.btnSenderismo).setOnClickListener(v -> abrirTracking(ActivityType.HIKE));
        findViewById(R.id.btnBicicleta).setOnClickListener(v -> abrirTracking(ActivityType.BIKE));
        findViewById(R.id.btnCaminar).setOnClickListener(v -> abrirTracking(ActivityType.WALK));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEntrenamientos();
    }

    private void cargarEntrenamientos() {
        workoutRepository.loadWorkouts(workouts -> {
            contenedorEntrenamientos.removeAllViews();
            LayoutInflater inflater = getLayoutInflater();
            for (Workout workout : workouts) {
                contenedorEntrenamientos.addView(crearItem(inflater, workout));
            }
        });
    }

    private View crearItem(LayoutInflater inflater, Workout workout) {
        View item = inflater.inflate(R.layout.item_workout, contenedorEntrenamientos, false);
        ImageView icon = item.findViewById(R.id.ivWorkoutIcon);
        TextView name = item.findViewById(R.id.tvWorkoutName);
        TextView goal = item.findViewById(R.id.tvWorkoutGoal);

        icon.setImageResource(workout.type.iconRes);
        name.setText(workout.name);
        goal.setText(buildSubtitle(workout));

        item.setOnClickListener(v -> abrirTracking(workout));
        return item;
    }

    private String buildSubtitle(Workout workout) {
        String typeName = getString(workout.type.nameRes);
        if (!workout.hasGoal()) {
            return typeName;
        }
        String meta = Workout.GOAL_TIME.equals(workout.goalType)
                ? getString(R.string.workout_goal_time, formatNumber(workout.goalValue))
                : getString(R.string.workout_goal_distance, formatNumber(workout.goalValue));
        return typeName + " · " + meta;
    }

    private void abrirTracking(ActivityType type) {
        Intent intent = new Intent(this, WorkoutTrackingActivity.class);
        intent.putExtra(EXTRA_ACTIVITY_TYPE, type.name());
        startActivity(intent);
    }

    private void abrirTracking(Workout workout) {
        Intent intent = new Intent(this, WorkoutTrackingActivity.class);
        intent.putExtra(EXTRA_ACTIVITY_TYPE, workout.type.name());
        if (workout.hasGoal()) {
            intent.putExtra(EXTRA_GOAL_TYPE, workout.goalType);
            intent.putExtra(EXTRA_GOAL_VALUE, workout.goalValue);
        }
        startActivity(intent);
    }

    private String formatNumber(double value) {
        if (value == Math.rint(value)) {
            return String.valueOf((int) value);
        }
        return String.format(Locale.getDefault(), "%.1f", value);
    }
}
