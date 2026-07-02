package com.example.runtracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private static final int PREVIEW_RUNS = 3;
    private static final int MAX_RUNS = 100;
    private static final float ACHIEVEMENT_LOCKED_ALPHA = 0.35f;

    private TextView tvUsername, tvJoinDate, tvTotalKm, tvTotalRuns, tvBestTime, tvAvatarInitial;
    private TextView tvVerTodasCarreras;
    private TextView tvHealthWeight, tvHealthHeight, tvHealthImc;
    private ImageView ivEditProfile, ivSignOut, ivAvatarPhoto;
    private LinearLayout navHome, navProfile;
    private LinearLayout contenedorCarreras, healthRow;
    private LinearLayout logroPrimeraCarrera, logro5k, logro10k;
    private MaterialCardView fabRun;

    private final RunRepository runRepository = new RunRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bindViews();
        loadProfile();
        setupNavbar();
        NavbarHelper.markActiveTab(this, NavbarHelper.Tab.PROFILE);

        ivEditProfile.setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class))
        );

        ivSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        tvVerTodasCarreras.setOnClickListener(v ->
                startActivity(new Intent(this, MyRunsActivity.class)));
    }

    protected void onResume() {
        super.onResume();
        loadProfile();
    }

    private void bindViews() {
        tvUsername  = findViewById(R.id.tvUsername);
        tvJoinDate  = findViewById(R.id.tvJoinDate);
        tvTotalKm   = findViewById(R.id.tvTotalKm);
        tvTotalRuns = findViewById(R.id.tvTotalRuns);
        tvBestTime  = findViewById(R.id.tvBestTime);
        navHome    = findViewById(R.id.navHome);
        navProfile = findViewById(R.id.navProfile);
        fabRun     = findViewById(R.id.fabRun);
        ivEditProfile   = findViewById(R.id.ivEditProfile);
        ivSignOut       = findViewById(R.id.ivSignOut);
        ivAvatarPhoto   = findViewById(R.id.ivAvatarPhoto);
        tvAvatarInitial = findViewById(R.id.tvAvatarInitial);
        contenedorCarreras = findViewById(R.id.contenedorCarreras);
        tvVerTodasCarreras = findViewById(R.id.tvVerTodasCarreras);
        logroPrimeraCarrera = findViewById(R.id.logroPrimeraCarrera);
        logro5k = findViewById(R.id.logro5k);
        logro10k = findViewById(R.id.logro10k);
        healthRow      = findViewById(R.id.healthRow);
        tvHealthWeight = findViewById(R.id.tvHealthWeight);
        tvHealthHeight = findViewById(R.id.tvHealthHeight);
        tvHealthImc    = findViewById(R.id.tvHealthImc);
    }

    private void loadProfile() {
        UserPreferences prefs = new UserPreferences(this);
        prefs.initializeJoinYearIfNeeded();

        
        String username = prefs.getUsername();
        tvUsername.setText(username);
        tvAvatarInitial.setText(getInitial(username));
        tvJoinDate.setText(getString(R.string.profile_join_date, prefs.getJoinYear()));
        tvTotalKm.setText(getString(R.string.stats_value_km));
        tvTotalRuns.setText(getString(R.string.stats_value_runs));
        tvBestTime.setText(getString(R.string.stats_value_time));
        mostrarFoto(prefs.getPhotoPath());
        renderSalud(prefs.getWeightKg(), prefs.getHeightCm());

        
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) return;
                    String remoteUsername = snapshot.getString("username");
                    Long remoteJoinYear   = snapshot.getLong("joinYear");
                    if (remoteUsername != null) {
                        prefs.setUsername(remoteUsername);
                        tvUsername.setText(remoteUsername);
                        tvAvatarInitial.setText(getInitial(remoteUsername));
                    }
                    if (remoteJoinYear != null) {
                        tvJoinDate.setText(getString(R.string.profile_join_date, remoteJoinYear.intValue()));
                    }

                    Double remoteWeight = snapshot.getDouble("weightKg");
                    Long remoteHeight   = snapshot.getLong("heightCm");
                    Long remoteAge      = snapshot.getLong("age");
                    String remoteGender = snapshot.getString("gender");
                    if (remoteWeight != null) prefs.setWeightKg(remoteWeight.floatValue());
                    if (remoteHeight != null) prefs.setHeightCm(remoteHeight.intValue());
                    if (remoteAge != null)    prefs.setAge(remoteAge.intValue());
                    if (remoteGender != null) prefs.setGender(remoteGender);
                    renderSalud(prefs.getWeightKg(), prefs.getHeightCm());
                });

        cargarCarreras();
    }

    private void mostrarFoto(String path) {
        Bitmap photo = ImageStorage.load(path);
        if (photo != null) {
            ivAvatarPhoto.setImageBitmap(photo);
            ivAvatarPhoto.setVisibility(View.VISIBLE);
        } else {
            ivAvatarPhoto.setVisibility(View.GONE);
        }
    }

    private void renderSalud(float weightKg, int heightCm) {
        if (weightKg <= 0 && heightCm <= 0) {
            healthRow.setVisibility(View.GONE);
            return;
        }
        healthRow.setVisibility(View.VISIBLE);
        tvHealthWeight.setText(weightKg > 0
                ? getString(R.string.profile_weight_value, formatNumber(weightKg))
                : getString(R.string.health_empty));
        tvHealthHeight.setText(heightCm > 0
                ? getString(R.string.profile_height_value, heightCm)
                : getString(R.string.health_empty));
        tvHealthImc.setText(calcularImc(weightKg, heightCm));
    }

    private String calcularImc(float weightKg, int heightCm) {
        if (weightKg <= 0 || heightCm <= 0) {
            return getString(R.string.health_empty);
        }
        double heightM = heightCm / 100.0;
        double imc = weightKg / (heightM * heightM);
        return String.format(Locale.getDefault(), "%.1f", imc);
    }

    private String formatNumber(float value) {
        if (value == Math.rint(value)) {
            return String.valueOf((int) value);
        }
        return String.format(Locale.getDefault(), "%.1f", value);
    }

    private void cargarCarreras() {
        runRepository.loadRuns(MAX_RUNS, runs -> {
            renderPreview(runs);
            renderEstadisticas(RunStats.from(runs));
        });
    }

    private void renderPreview(List<Run> runs) {
        contenedorCarreras.removeAllViews();
        if (runs.isEmpty()) {
            mostrarCarrerasVacio();
            return;
        }
        int shown = Math.min(PREVIEW_RUNS, runs.size());
        for (int i = 0; i < shown; i++) {
            contenedorCarreras.addView(
                    RunCardFactory.create(getLayoutInflater(), contenedorCarreras, runs.get(i)));
        }
    }

    private void renderEstadisticas(RunStats stats) {
        tvTotalKm.setText(String.format(Locale.getDefault(), "%.1f km", stats.totalKm));
        tvTotalRuns.setText(String.valueOf(stats.count));
        if (stats.bestPaceSecondsPerKm > 0) {
            long pace = stats.bestPaceSecondsPerKm;
            tvBestTime.setText(String.format(Locale.getDefault(), "%d:%02d", pace / 60, pace % 60));
        } else {
            tvBestTime.setText(R.string.stats_value_time);
        }

        setLogro(logroPrimeraCarrera, stats.count > 0);
        setLogro(logro5k, stats.maxDistanceKm >= 5);
        setLogro(logro10k, stats.maxDistanceKm >= 10);
    }

    private void setLogro(LinearLayout logro, boolean desbloqueado) {
        logro.setAlpha(desbloqueado ? 1f : ACHIEVEMENT_LOCKED_ALPHA);
    }

    private void mostrarCarrerasVacio() {
        TextView tvVacio = new TextView(this);
        tvVacio.setText(R.string.runs_history_empty);
        tvVacio.setTextColor(getColor(R.color.text_hint));
        contenedorCarreras.addView(tvVacio);
    }

    private String getInitial(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        return String.valueOf(name.trim().charAt(0)).toUpperCase();
    }

    private void setupNavbar() {
        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        navProfile.setOnClickListener(v -> {});
        fabRun.setOnClickListener(v ->
                startActivity(new Intent(this, WorkoutSelectionActivity.class)));
    }
}