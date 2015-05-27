package cz.synek.pavel.yahooweather.rest;

import cz.synek.pavel.yahooweather.utils.C;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public class WeatherAPIService {

    private WeatherAPIService() {
        // no instances
    }

    public interface WeatherApi {
        @GET(C.WEATHER_API_URL)
        Observable<WeatherForecast> getWeather(@Query("q") String location);
    }

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder()
            .setEndpoint(C.WEATHER_API_ENDPOINT)
            .build();

    public static final WeatherApi WEATHER_API = REST_ADAPTER.create(WeatherApi.class);
}
