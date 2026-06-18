package apiClima;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface WeatherApiService {

    @GET("data/2.5/weather")
    Call<WeatherResponse> getCurrentWeather(
            @Query("q") String cityName,      // Ciudad (ej: "Buenos Aires")
            @Query("appid") String apiKey,    // Tu clave (La que sacaste de la web)
            @Query("units") String units,     // Para que venga en Grados Celsius ("metric")
            @Query("lang") String lang        // Para que venga en Español ("es")
    );

}
