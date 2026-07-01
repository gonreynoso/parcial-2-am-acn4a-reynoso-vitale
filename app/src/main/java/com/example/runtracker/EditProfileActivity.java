package com.example.runtracker;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditProfileActivity extends AppCompatActivity {

    private EditText inputUsername, inputWeight, inputHeight, inputAge;
    private RadioGroup groupGender;
    private ImageView ivAvatar;
    private TextView txtError;

    private UserPreferences prefs;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        inputUsername = findViewById(R.id.inputUsername);
        inputWeight   = findViewById(R.id.inputWeight);
        inputHeight   = findViewById(R.id.inputHeight);
        inputAge      = findViewById(R.id.inputAge);
        groupGender   = findViewById(R.id.groupGender);
        ivAvatar      = findViewById(R.id.ivAvatar);
        txtError      = findViewById(R.id.txtError);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        ImageView btnBack = findViewById(R.id.btnBack);
        MaterialCardView cardAvatar = findViewById(R.id.cardAvatar);
        TextView btnCambiarFoto = findViewById(R.id.btnCambiarFoto);

        prefs = new UserPreferences(this);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) guardarFoto(uri);
                });

        cargarDatos();

        btnBack.setOnClickListener(v -> finish());
        View.OnClickListener pickPhoto = v -> pickImageLauncher.launch("image/*");
        cardAvatar.setOnClickListener(pickPhoto);
        btnCambiarFoto.setOnClickListener(pickPhoto);

        btnGuardar.setOnClickListener(v -> guardar());
    }

    private void cargarDatos() {
        inputUsername.setText(prefs.getUsername());

        if (prefs.getWeightKg() > 0) {
            inputWeight.setText(String.valueOf(prefs.getWeightKg()));
        }
        if (prefs.getHeightCm() > 0) {
            inputHeight.setText(String.valueOf(prefs.getHeightCm()));
        }
        if (prefs.getAge() > 0) {
            inputAge.setText(String.valueOf(prefs.getAge()));
        }

        switch (prefs.getGender()) {
            case "M": groupGender.check(R.id.radioMale); break;
            case "F": groupGender.check(R.id.radioFemale); break;
            case "O": groupGender.check(R.id.radioOther); break;
            default: break;
        }

        Bitmap photo = ImageStorage.load(prefs.getPhotoPath());
        if (photo != null) {
            mostrarFoto(photo);
        }
    }

    private void guardarFoto(Uri uri) {
        executor.execute(() -> {
            try {
                String path = ImageStorage.saveProfilePhoto(this, uri);
                Bitmap photo = ImageStorage.load(path);
                runOnUiThread(() -> {
                    prefs.setPhotoPath(path);
                    if (photo != null) mostrarFoto(photo);
                });
            } catch (IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(this, R.string.edit_profile_photo_error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void mostrarFoto(Bitmap photo) {
        ivAvatar.setImageTintList(null);
        ivAvatar.setPadding(0, 0, 0, 0);
        ivAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ivAvatar.setImageBitmap(photo);
    }

    private void guardar() {
        String nuevoNombre = inputUsername.getText().toString().trim();
        if (TextUtils.isEmpty(nuevoNombre)) {
            mostrarError("El nombre no puede estar vacío");
            return;
        }
        if (nuevoNombre.length() < 2) {
            mostrarError("El nombre debe tener al menos 2 caracteres");
            return;
        }

        float weight = parseFloatOrZero(inputWeight.getText().toString());
        int height = parseIntOrZero(inputHeight.getText().toString());
        int age = parseIntOrZero(inputAge.getText().toString());
        String gender = readGender();

        prefs.setUsername(nuevoNombre);
        prefs.setWeightKg(weight);
        prefs.setHeightCm(height);
        prefs.setAge(age);
        prefs.setGender(gender);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("username", nuevoNombre);
            data.put("weightKg", weight);
            data.put("heightCm", height);
            data.put("age", age);
            data.put("gender", gender);
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener(unused ->
                            Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Perfil actualizado (sin conexión)", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private String readGender() {
        int checked = groupGender.getCheckedRadioButtonId();
        if (checked == R.id.radioMale) return "M";
        if (checked == R.id.radioFemale) return "F";
        if (checked == R.id.radioOther) return "O";
        return "";
    }

    private float parseFloatOrZero(String value) {
        try {
            return TextUtils.isEmpty(value.trim()) ? 0f : Float.parseFloat(value.trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    private int parseIntOrZero(String value) {
        try {
            return TextUtils.isEmpty(value.trim()) ? 0 : Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void mostrarError(String mensaje) {
        txtError.setText(mensaje);
        txtError.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
