package coffee.lkh.weathermonitoringv2.services.base;



import coffee.lkh.weathermonitoringv2.models.remote.weatherbitapi.CityWeatherForecasts;
import coffee.lkh.weathermonitoringv2.models.remote.rapidapi.geocode.CityInfo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;


public interface IHttpClientService {
    Future<Optional<CityWeatherForecasts>> getForecastByCityAsync(String city, String country, Optional<String> state);
    Future<Optional<List<CityInfo>>> getCityInfoAsync(double[] location);

}
