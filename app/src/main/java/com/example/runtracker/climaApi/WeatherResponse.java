package com.example.runtracker.climaApi;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class WeatherResponse {


    private Main main;
    private List<Weather> weather;
    private Wind wind;
    private String name;
    private int visibility;

    public Main getMain() { return main; }
    public List<Weather> getWeather() { return weather; }
    public Wind getWind() { return wind; }
    public String getName() { return name; }
    public int getVisibility() { return visibility; }

    public class Main {
        private float temp;
        private int humidity;

        @SerializedName("feels_like")
        private float feelsLike; // NUEVO: Sensación térmica

        public float getTemp() { return temp; }
        public int getHumidity() { return humidity; }
        public float getFeelsLike() { return feelsLike; }
    }

    public class Weather {
        private String description;
        private String main;

        public String getDescription() { return description; }
        public String getMain() { return main; }
    }

    public class Wind {
        private float speed;
        public float getSpeed() { return speed; }
    }
}
