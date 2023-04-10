package coffee.lkh.weathermonitoringv2.services.base;


import coffee.lkh.weathermonitoringv2.models.remote.weatherbitapi.CityWeatherForecasts;

import java.util.Optional;
import java.util.concurrent.Future;

public interface IDbContext {


    public Future<Boolean> insertOrUpdateForecastsAsync(CityWeatherForecasts forecasts);
    public Future<Boolean> deleteForecastsAsync(String city, String country, Optional<String> state);
    public Future<Optional<CityWeatherForecasts>> selectForecastsAsync(String city, String country,Optional<String> state);
}
