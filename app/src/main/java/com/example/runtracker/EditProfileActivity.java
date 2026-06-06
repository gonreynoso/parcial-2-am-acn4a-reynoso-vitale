package com.example.runtracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Collections;

public class EditProfileActivity extends AppCompatActivity {

    private EditText inputUsername;
    private TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        inputUsername = findViewById(R.id.inputUsername);
        txtError      = findViewById(R.id.txtError);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        ImageView btnBack = findViewById(R.id.btnBack);

        UserPreferences prefs = new UserPreferences(this);
        inputUsername.setText(prefs.getUsername());

        btnBack.setOnClickListener(v -> finish());

        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = inputUsername.getText().toString().trim();
            if (TextUtils.isEmpty(nuevoNombre)) {
                mostrarError("El nombre no puede estar vacío");
                return;
            }
            if (nuevoNombre.length() < 2) {
                mostrarError("El nombre debe tener al menos 2 caracteres");
                return;
            }

            // Always persist locally first
            prefs.setUsername(nuevoNombre);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Merge so we don't overwrite joinYear or other fields
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(user.getUid())
                        .set(Collections.singletonMap("username", nuevoNombre), SetOptions.merge())
                        .addOnSuccessListener(unused ->
                                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Perfil actualizado (sin conexión)", Toast.LENGTH_SHORT).show()
                        );
            } else {
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }

    private void mostrarError(String mensaje) {
        txtError.setText(mensaje);
        txtError.setVisibility(android.view.View.VISIBLE);
    }
}