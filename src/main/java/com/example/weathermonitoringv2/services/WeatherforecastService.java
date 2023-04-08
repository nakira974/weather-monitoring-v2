package com.example.weathermonitoringv2.services;

import com.example.weathermonitoringv2.models.remote.WeatherForecasts;
import com.example.weathermonitoringv2.repositories.IWeatherforecastRepository;
import org.springframework.stereotype.Service;

@Service
public class WeatherforecastService implements IWeatherforecastService {

    IWeatherforecastRepository repository;
    public WeatherforecastService(IWeatherforecastRepository repository){

    }

    /**
     * @param forecasts
     * @return
     */
    @Override
    public boolean InsertForecasts(WeatherForecasts forecasts) {
        return false;
    }

    /**
     * @param city_name
     * @return
     */
    @Override
    public WeatherForecasts SelectForecasts(String city_name) {
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
