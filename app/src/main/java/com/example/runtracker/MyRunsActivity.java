package com.example.runtracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class MyRunsActivity extends AppCompatActivity {

    private static final int MAX_RUNS = 50;

    private final RunRepository runRepository = new RunRepository();
    private LinearLayout contenedorCarreras;
    private MaterialButton btnBorrarTodas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_runs);

        contenedorCarreras = findViewById(R.id.contenedorCarreras);
        btnBorrarTodas = findViewById(R.id.btnBorrarTodas);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        btnBorrarTodas.setOnClickListener(v -> confirmarBorrado());
    }

    @Override
    protected void onResume() {
        super.onResume();
        runRepository.loadRuns(MAX_RUNS, this::render);
    }

    private void render(List<Run> runs) {
        contenedorCarreras.removeAllViews();
        if (runs.isEmpty()) {
            mostrarVacio();
            btnBorrarTodas.setVisibility(View.GONE);
            return;
        }
        btnBorrarTodas.setVisibility(View.VISIBLE);
        LayoutInflater inflater = getLayoutInflater();
        for (Run run : runs) {
            contenedorCarreras.addView(RunCardFactory.create(inflater, contenedorCarreras, run));
        }
    }

    private void confirmarBorrado() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.runs_delete_confirm_title)
                .setMessage(R.string.runs_delete_confirm_message)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.runs_delete_confirm_ok, (dialog, which) -> borrarTodas())
                .show();
    }

    private void borrarTodas() {
        btnBorrarTodas.setEnabled(false);
        runRepository.deleteAll(() -> {
            btnBorrarTodas.setEnabled(true);
            Toast.makeText(this, R.string.runs_deleted, Toast.LENGTH_SHORT).show();
            runRepository.loadRuns(MAX_RUNS, this::render);
        });
    }

    private void mostrarVacio() {
        TextView tvVacio = new TextView(this);
        tvVacio.setText(R.string.runs_history_empty);
        tvVacio.setTextColor(getColor(R.color.text_hint));
        contenedorCarreras.addView(tvVacio);
    }
}
