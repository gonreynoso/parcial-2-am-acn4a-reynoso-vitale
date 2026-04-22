package com.example.runtracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

            prefs.setUsername(nuevoNombre);
            Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void mostrarError(String mensaje) {
        txtError.setText(mensaje);
        txtError.setVisibility(android.view.View.VISIBLE);
    }
}