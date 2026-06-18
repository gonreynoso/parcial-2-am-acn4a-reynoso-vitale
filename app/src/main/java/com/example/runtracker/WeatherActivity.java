package com.example.runtracker; // Asegurate de que este sea tu paquete correcto

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Importamos las clases que armamos antes
import apiClima.RetrofitClient;
import apiClima.WeatherApiService;
import apiClima.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView txtCiudad, txtTemperatura, txtDescripcionClima, txtRecomendacion, txtHumedad, txtViento;
    private Button btnVolver;

    //  clave de OpenWeatherMap
    private static final String API_KEY = "8c007f8359d4a2bfba99da12b16c7a60";
    // Pfijar una ciudad
    private static final String CIUDAD = "Buenos Aires";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // 1. Enlazar las vistas del XML con nuestras variables de Java
        txtHumedad = findViewById(R.id.txtHumedad);
        txtViento = findViewById(R.id.txtViento);
        txtCiudad = findViewById(R.id.txtCiudad);
        txtTemperatura = findViewById(R.id.txtTemperatura);
        txtDescripcionClima = findViewById(R.id.txtDescripcionClima);
        txtRecomendacion = findViewById(R.id.txtRecomendacion);
        btnVolver = findViewById(R.id.btnVolver);

        // 2. Configurar el botón de volver para cerrar esta pantalla
        btnVolver.setOnClickListener(v -> finish());

        // 3. Disparar la llamada a internet apenas se abre la pantalla
        obtenerClimaActual();
    }

    private void obtenerClimaActual() {
        // Usamos el cliente y la interfaz que armaste en la carpeta 'api'
        WeatherApiService service = RetrofitClient.getClient().create(WeatherApiService.class);

        // Preparamos la llamada ("metric" es para Celsius, "es" para español)
        Call<WeatherResponse> call = service.getCurrentWeather(CIUDAD, API_KEY, "metric", "es");

        // Ejecutamos la llamada en segundo plano (Asíncrono)
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherData = response.body();

                    // Extraemos los datos mapeados por Gson
                    float temp = weatherData.main.temp;
                    String ciudad = weatherData.cityName;
                    String descripcion = weatherData.weatherList.get(0).description;
                    String condicionPrincipal = weatherData.weatherList.get(0).mainCondition;

                    int humedad = weatherData.main.humidity;
                    float velocidadVientoMs = weatherData.wind.speed;

                    int vientoKmH = Math.round(velocidadVientoMs * 3.6f);


                    // Actualizamos la pantalla (UI Thread)
                    txtCiudad.setText(ciudad);
                    txtTemperatura.setText(Math.round(temp) + "°C");
                    txtDescripcionClima.setText(descripcion.substring(0, 1).toUpperCase() + descripcion.substring(1));

                    txtHumedad.setText("💧 Humedad: " + humedad + "%");
                    txtViento.setText("💨 Viento: " + vientoKmH + " km/h");

                    // Disparamos la lógica de negocio (El motor de recomendación)
                    generarRecomendacion(temp, condicionPrincipal, humedad, vientoKmH);
                } else {
                    txtCiudad.setText("Error al cargar la ciudad");
                    Toast.makeText(WeatherActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Si no hay internet o el servidor se cae
                txtCiudad.setText("Sin conexión");
                txtTemperatura.setText("--");
                Log.e("API_ERROR", "Fallo la conexión: " + t.getMessage());
                Toast.makeText(WeatherActivity.this, "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- El Cerebro de las Recomendaciones ---
    // --- Reemplazá tu método viejo por este nuevo ---
    private void generarRecomendacion(float temp, String condicion, int humedad, int vientoKmH) {
        String recomendacion = "";

        // 1. Evaluamos si llueve o hay tormenta
        if (condicion.equalsIgnoreCase("Rain") || condicion.equalsIgnoreCase("Thunderstorm")) {
            recomendacion = "El clima está inestable 🌧️. Es un día ideal para entrenar fuerza en casa y después relajarte jugando unas partidas de Warzone o League of Legends.";
        }
        // 2. Evaluamos si el viento es peligroso o molesto
        else if (vientoKmH > 35) {
            recomendacion = "Hay ráfagas fuertes de " + vientoKmH + " km/h 💨. Si salís, tené cuidado. Las rutinas de elongación en interiores son una gran alternativa hoy.";
        }
        // 3. Evaluamos calor extremo o humedad sofocante
        else if (temp > 30 || humedad > 80) {
            recomendacion = "Calor o humedad extrema (" + humedad + "%) 🥵. Si entrenás afuera, bajá el ritmo y llevá mucha agua. La natación es tu mejor opción.";
        }
        // 4. Evaluamos frío
        else if (temp < 10) {
            recomendacion = "Hace bastante frío 🥶. Abrigate bien si vas a sumar kilómetros, o probá una sesión de yoga para entrar en calor rápido.";
        }
        // 5. El clima ideal
        else {
            recomendacion = "¡Clima excelente! 🏃 Ponete las zapatillas para ir a correr, o aprovechá para preparar la caña y el reel e ir a pasar un rato tranquilo al río.";
        }

        txtRecomendacion.setText(recomendacion);
    }
}