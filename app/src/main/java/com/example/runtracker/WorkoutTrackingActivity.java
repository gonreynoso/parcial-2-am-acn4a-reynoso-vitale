package com.example.runtracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class WorkoutTrackingActivity extends AppCompatActivity {

    private ImageView ivActivityIcon;
    private TextView tvActivityName;
    private MaterialButton btnPausa, btnDetener, btnReanudar;
    private LinearLayout contenedorPausado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_tracking);

        ivActivityIcon = findViewById(R.id.ivActivityIcon);
        tvActivityName = findViewById(R.id.tvActivityName);
        btnPausa = findViewById(R.id.btnPausa);
        contenedorPausado = findViewById(R.id.contenedorPausado);
        btnDetener = findViewById(R.id.btnDetener);
        btnReanudar = findViewById(R.id.btnReanudar);

        tvActivityName.setText(getIntent().getStringExtra("nombre"));
        ivActivityIcon.setImageResource(getIntent().getIntExtra("icono", R.drawable.ic_directions_run));

        btnPausa.setOnClickListener(v -> {
            btnPausa.setVisibility(View.GONE);
            contenedorPausado.setVisibility(View.VISIBLE);
        });

        btnReanudar.setOnClickListener(v -> {
            contenedorPausado.setVisibility(View.GONE);
            btnPausa.setVisibility(View.VISIBLE);
        });

        btnDetener.setOnClickListener(v -> finish());
    }
}
