package apiClima;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {

    @SerializedName("name")
    public String cityName;

    // Atrapa el bloque "main" (donde está la temperatura)
    @SerializedName("main")
    public MainData main;

    // Atrapa la lista de condiciones del clima
    @SerializedName("weather")
    public List<Weather> weatherList;

    @SerializedName("wind")
    public Wind wind;

    // --- Sub-clase para la temperatura ---
    public static class MainData {
        @SerializedName("temp")
        public float temp; // Temperatura actual

        @SerializedName("humidity")
        public int humidity; // Humedad
    }

    // --- Sub-clase para la descripción (ej: "Lluvia", "Despejado") ---
    public static class Weather {
        @SerializedName("main")
        public String mainCondition; // Ej: "Rain", "Clear"

        @SerializedName("description")
        public String description; // Ej: "l
    }

    public static class Wind {
        @SerializedName("speed")
        public float speed; // Ojo: OpenWeatherMap manda esto en metros por segundo (m/s)
    }
}
