package coffee.lkh.weathermonitoringv2.services.base;

import coffee.lkh.weathermonitoringv2.models.Weatherforecast;
import coffee.lkh.weathermonitoringv2.models.remote.CityWeatherForecasts;

import java.util.Optional;
import java.util.concurrent.Future;

public interface IDbContext {

    public Future<Boolean> insertForecastsAsync(CityWeatherForecasts forecasts);
    public Future<Optional<CityWeatherForecasts>> selectForecastsAsync(String city, String country,Optional<String> state);
}
