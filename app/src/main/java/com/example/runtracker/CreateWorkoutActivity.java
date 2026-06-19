package com.example.runtracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateWorkoutActivity extends AppCompatActivity {

    private EditText inputName, inputGoalValue;
    private RadioGroup groupType, groupGoal;

    private final WorkoutRepository workoutRepository = new WorkoutRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        inputName = findViewById(R.id.inputName);
        inputGoalValue = findViewById(R.id.inputGoalValue);
        groupType = findViewById(R.id.groupType);
        groupGoal = findViewById(R.id.groupGoal);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        btnGuardar.setOnClickListener(v -> guardar());
    }

    private void guardar() {
        String name = inputName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, R.string.create_workout_name_required, Toast.LENGTH_SHORT).show();
            return;
        }

        ActivityType type = readType();
        double goalValue = parseGoalValue(inputGoalValue.getText().toString());
        String goalType = goalValue <= 0
                ? Workout.GOAL_NONE
                : (groupGoal.getCheckedRadioButtonId() == R.id.goalTime
                        ? Workout.GOAL_TIME
                        : Workout.GOAL_DISTANCE);

        workoutRepository.save(name, type, goalType, goalValue, success -> {
            Toast.makeText(this,
                    success ? R.string.create_workout_saved : R.string.create_workout_error,
                    Toast.LENGTH_SHORT).show();
            if (success) finish();
        });
    }

    private ActivityType readType() {
        int checked = groupType.getCheckedRadioButtonId();
        if (checked == R.id.typeHike) return ActivityType.HIKE;
        if (checked == R.id.typeBike) return ActivityType.BIKE;
        if (checked == R.id.typeWalk) return ActivityType.WALK;
        return ActivityType.RUN;
    }

    private double parseGoalValue(String value) {
        try {
            return TextUtils.isEmpty(value.trim()) ? 0 : Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
