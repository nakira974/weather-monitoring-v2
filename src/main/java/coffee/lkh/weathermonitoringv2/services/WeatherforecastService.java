package coffee.lkh.weathermonitoringv2.services;

import coffee.lkh.weathermonitoringv2.models.remote.weatherbitapi.CityWeatherForecasts;
import coffee.lkh.weathermonitoringv2.repositories.ICityWeatherForecastsRepository;
import coffee.lkh.weathermonitoringv2.services.base.IWeatherforecastService;
import org.springframework.stereotype.Service;

@Service
public class WeatherforecastService implements IWeatherforecastService {

    ICityWeatherForecastsRepository repository;
    public WeatherforecastService(ICityWeatherForecastsRepository repository){

    }

    /**
     * @param forecasts
     * @return
     */
    @Override
    public boolean InsertForecasts(CityWeatherForecasts forecasts) {
        return false;
    }

    /**
     * @param city_name
     * @return
     */
    @Override
    public CityWeatherForecasts SelectForecasts(String city_name) {
        return null;
    }

    /**
     * @param city_name
     * @return
     */
    @Override
    public boolean UpdateForecasts(String city_name) {
        return false;
    }

    /**
     * @param city_name
     * @return
     */
    @Override
    public boolean DeleteForecasts(String city_name) {
        return false;
    }
}
