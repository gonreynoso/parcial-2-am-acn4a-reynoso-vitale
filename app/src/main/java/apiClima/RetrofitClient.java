package apiClima;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {

    private static final String BASE_URL = "https://api.openweathermap.org/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Si la conexión no existe, la armamos
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Le decimos que use Gson para traducir
                    .build();
        }
        return retrofit; // Devolvemos la conexión lista para usar
    }


}
