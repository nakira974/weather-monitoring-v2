package coffee.lkh.weathermonitoringv2.services.remote;

import coffee.lkh.weathermonitoringv2.models.remote.weatherbit.CityWeatherForecasts;
import coffee.lkh.weathermonitoringv2.models.remote.weatherbit.rapidapi.geocode.CityInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GeocodeApi {

    @GET("GetLargestCities")
    Call<CityInfo> getCityInfo(
            @Header("X-RapidAPI-Host") String RapidApiHost,
            @Header("X-RapidAPI-Key") String RapidApiKey,
            @Query("range") int range,
            @Query("longitude") double longitude,
            @Query("latitude") double latitude);
}
