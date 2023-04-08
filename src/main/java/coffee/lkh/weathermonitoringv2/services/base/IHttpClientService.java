package coffee.lkh.weathermonitoringv2.services.base;



import coffee.lkh.weathermonitoringv2.models.remote.CityWeatherForecasts;

import java.util.Optional;
import java.util.concurrent.Future;


public interface IHttpClientService {
    Future<Optional<CityWeatherForecasts>> getForecastByCityAsync(String city, String country, Optional<String> state);
}
