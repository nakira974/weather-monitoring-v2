package coffee.lkh.weathermonitoringv2.services.base;



import coffee.lkh.weathermonitoringv2.models.remote.weatherbit.CityWeatherForecasts;
import coffee.lkh.weathermonitoringv2.models.remote.weatherbit.rapidapi.geocode.CityInfo;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


public interface IHttpClientService {
    Future<Optional<CityWeatherForecasts>> getForecastByCityAsync(String city, String country, Optional<String> state);
    Future<Optional<CityInfo>> getCityInfoAsync(double[] location);

}
