package com.example.runtracker;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runtracker.climaApi.RetrofitClient;
import com.example.runtracker.climaApi.WeatherApiService;
import com.example.runtracker.climaApi.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    // Vistas de la tarjeta
    private TextView txtUbicacion, txtFechaHora, txtTempPrincipal, txtCondicion;
    private TextView txtSensacion, txtVisibilidad, txtViento, txtHumedad;
    private ImageView imgIconoClima;
    private ImageButton btnVolver;

    private TextView txtRecomendacion;

    private final String API_KEY = "8c007f8359d4a2bfba99da12b16c7a60";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        inicializarVistas();
        configurarBotonVolver();


        actualizarFechaHora();

        obtenerClimaActual(-34.6037, -58.3816);
    }

    private void inicializarVistas() {
        txtUbicacion = findViewById(R.id.txtUbicacion);
        txtFechaHora = findViewById(R.id.txtFechaHora);
        txtTempPrincipal = findViewById(R.id.txtTempPrincipal);
        txtCondicion = findViewById(R.id.txtCondicion);
        txtSensacion = findViewById(R.id.txtSensacion);
        txtVisibilidad = findViewById(R.id.txtVisibilidad);
        txtViento = findViewById(R.id.txtViento);
        txtHumedad = findViewById(R.id.txtHumedad);
        imgIconoClima = findViewById(R.id.imgIconoClima);
        btnVolver = findViewById(R.id.btnVolver);
        txtRecomendacion = findViewById(R.id.txtRecomendacion);
    }

    private void configurarBotonVolver() {
        btnVolver.setOnClickListener(v -> finish()); // Cierra la actividad y vuelve al Dashboard
    }

    private void actualizarFechaHora() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm 'HS' - dd MMM", new Locale("es", "ES"));
        String fechaActual = sdf.format(new Date());
        txtFechaHora.setText(fechaActual.toUpperCase());
    }

    private void obtenerClimaActual(double lat, double lon) {
        WeatherApiService apiService = RetrofitClient.getClient().create(WeatherApiService.class);

        // Pedimos en español ("es") y en grados Celsius ("metric")
        Call<WeatherResponse> call = apiService.getCurrentWeather(lat, lon, API_KEY, "metric", "es");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherData = response.body();

                    // 1. Datos Principales
                    txtUbicacion.setText(weatherData.getName() + ", AR");
                    txtTempPrincipal.setText(Math.round(weatherData.getMain().getTemp()) + "°C");

                    float tempActual = weatherData.getMain().getTemp();

                    if (tempActual < 10) {
                        txtRecomendacion.setText("¡Hace bastante frío! Ideal salir con equipo térmico, guantes y entrar bien en calor. 🥶");
                    } else if (tempActual >= 10 && tempActual < 18) {
                        txtRecomendacion.setText("Clima fresco ideal para correr. Un rompevientos liviano es más que suficiente. 🏃‍♂️💨");
                    } else if (tempActual >= 18 && tempActual < 26) {
                        txtRecomendacion.setText("¡Clima excelente! Ropa liviana y no te olvides de mantener una buena hidratación. ☀️💧");
                    } else {
                        txtRecomendacion.setText("¡Hace mucho calor! Evitá correr bajo el sol directo, llevá mucha agua y usá gorra. 🔥🏃‍♂️");
                    }

                    if (weatherData.getWeather() != null && !weatherData.getWeather().isEmpty()) {
                        txtCondicion.setText(weatherData.getWeather().get(0).getDescription());
                    }

                    // 3. Grilla Premium
                    txtSensacion.setText(Math.round(weatherData.getMain().getFeelsLike()) + "°C");
                    txtHumedad.setText(weatherData.getMain().getHumidity() + "%");

                    // Viento (OpenWeather devuelve m/s, lo multiplicamos por 3.6 para km/h)
                    int vientoKmh = (int) Math.round(weatherData.getWind().getSpeed() * 3.6);
                    txtViento.setText(vientoKmh + " KM/H");

                    // Visibilidad (Viene en metros, la pasamos a KM)
                    int visibilidadKm = weatherData.getVisibility() / 1000;
                    txtVisibilidad.setText(visibilidadKm + " KM");

                } else {
                    Toast.makeText(WeatherActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("API_ERROR", "Fallo la conexión: " + t.getMessage());
                Toast.makeText(WeatherActivity.this, "Sin conexión a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}