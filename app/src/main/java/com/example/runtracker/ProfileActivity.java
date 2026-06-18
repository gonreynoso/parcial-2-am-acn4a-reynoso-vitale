package com.example.runtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvJoinDate, tvTotalKm, tvTotalRuns, tvBestTime, tvAvatarInitial;
    private MaterialButton btnEditProfile, btnSignOut;
    private LinearLayout navHome, navProfile;
    private LinearLayout contenedorCarreras;
    private MaterialCardView fabRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bindViews();
        loadProfile();
        setupNavbar();
        NavbarHelper.markActiveTab(this, NavbarHelper.Tab.PROFILE);

        btnEditProfile.setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class))
        );

        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
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
        btnEditProfile  = findViewById(R.id.btnEditProfile);
        tvAvatarInitial = findViewById(R.id.tvAvatarInitial);
        btnSignOut      = findViewById(R.id.btnSignOut);
        contenedorCarreras = findViewById(R.id.contenedorCarreras);
    }

    private void loadProfile() {
        UserPreferences prefs = new UserPreferences(this);
        prefs.initializeJoinYearIfNeeded();

        // Show local data immediately for a snappy UI
        String username = prefs.getUsername();
        tvUsername.setText(username);
        tvAvatarInitial.setText(getInitial(username));
        tvJoinDate.setText(getString(R.string.profile_join_date, prefs.getJoinYear()));
        tvTotalKm.setText(getString(R.string.stats_value_km));
        tvTotalRuns.setText(getString(R.string.stats_value_runs));
        tvBestTime.setText(getString(R.string.stats_value_time));

        // Refresh from Firestore if the user is authenticated
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
                });

        cargarCarreras();
    }

    private void cargarCarreras() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            contenedorCarreras.removeAllViews();
            mostrarCarrerasVacio();
            return;
        }

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("runs")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    contenedorCarreras.removeAllViews();
                    if (querySnapshot.isEmpty()) {
                        mostrarCarrerasVacio();
                        return;
                    }
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        contenedorCarreras.addView(crearTarjetaCarrera(doc));
                    }
                });
    }

    private void mostrarCarrerasVacio() {
        TextView tvVacio = new TextView(this);
        tvVacio.setText(R.string.runs_history_empty);
        tvVacio.setTextColor(getColor(R.color.text_hint));
        contenedorCarreras.addView(tvVacio);
    }

    private LinearLayout crearTarjetaCarrera(DocumentSnapshot doc) {
        LinearLayout tarjeta = new LinearLayout(this);
        tarjeta.setOrientation(LinearLayout.VERTICAL);
        tarjeta.setBackgroundColor(getColor(R.color.gris_tarjeta));

        int padding = getResources().getDimensionPixelSize(R.dimen.padding_card);
        tarjeta.setPadding(padding, padding, padding, padding);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.spacing_xs), 0, 0);
        tarjeta.setLayoutParams(params);

        Date fecha = doc.getTimestamp("timestamp") != null ? doc.getTimestamp("timestamp").toDate() : null;
        Double distanciaKm = doc.getDouble("distanciaKm");
        Long pasos = doc.getLong("pasos");
        Long duracionSegundos = doc.getLong("duracionSegundos");

        TextView tvFecha = new TextView(this);
        if (fecha != null) {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "AR"));
            tvFecha.setText(formato.format(fecha));
        }
        tvFecha.setTextColor(getColor(R.color.white));
        tvFecha.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_body));
        tvFecha.setTypeface(tvFecha.getTypeface(), android.graphics.Typeface.BOLD);

        TextView tvDetalle = new TextView(this);
        String distanciaTexto = String.format("%.2f km", distanciaKm != null ? distanciaKm : 0);
        String duracionTexto = formatearDuracion(duracionSegundos != null ? duracionSegundos : 0);
        tvDetalle.setText(distanciaTexto + " · " + (pasos != null ? pasos : 0) + " pasos · " + duracionTexto);
        tvDetalle.setTextColor(getColor(R.color.text_hint));
        tvDetalle.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_caption));

        LinearLayout.LayoutParams detalleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        detalleParams.topMargin = getResources().getDimensionPixelSize(R.dimen.spacing_xs);
        tvDetalle.setLayoutParams(detalleParams);

        tarjeta.addView(tvFecha);
        tarjeta.addView(tvDetalle);
        return tarjeta;
    }

    private String formatearDuracion(long duracionSegundos) {
        long horas = duracionSegundos / 3600;
        long minutos = (duracionSegundos % 3600) / 60;
        long segundos = duracionSegundos % 60;
        if (horas > 0) {
            return String.format("%d:%02d:%02d", horas, minutos, segundos);
        }
        return String.format("%02d:%02d", minutos, segundos);
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
        fabRun.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}