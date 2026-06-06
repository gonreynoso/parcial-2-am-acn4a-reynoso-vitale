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
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvJoinDate, tvTotalKm, tvTotalRuns, tvBestTime, tvAvatarInitial;
    private MaterialButton btnEditProfile, btnSignOut;
    private LinearLayout navHome, navProfile;
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