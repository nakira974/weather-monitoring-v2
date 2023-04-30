package coffee.lkh.weathermonitoringv2.services.remote;

import coffee.lkh.weathermonitoringv2.models.remote.weatherbitapi.CityWeatherForecasts;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WeatherBitApi {

    @POST("forecast/daily")
    Call<CityWeatherForecasts> getForecastByCityAndCountry(@Query("key") String apiKey,
                                                           @Query("city") String city,
                                                           @Query("country") String country,
                                                           @Query("days") String days);

    @POST("forecast/daily")
    Call<CityWeatherForecasts> getForecastByCityAndCountryAndState(@Query("key") String apiKey,
                                                                   @Query("city") String city,
                                                                   @Query("country") String country,
                                                                   @Query("state") String state,
                                                                   @Query("days") String days);
}
