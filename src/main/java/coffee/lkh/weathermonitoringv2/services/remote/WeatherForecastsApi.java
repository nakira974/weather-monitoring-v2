package coffee.lkh.weathermonitoringv2.services.remote;

import coffee.lkh.weathermonitoringv2.models.remote.CityWeatherForecasts;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WeatherForecastsApi {

    @POST("forecast/daily")
    Call<CityWeatherForecasts> getForecastByCity(@Query("key") String apiKey,
                                                 @Query("city") String city,
                                                 @Query("country") String country,
                                                 @Query("days") String days);
}