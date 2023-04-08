package com.example.weathermonitoringv2.services;


import com.example.weathermonitoringv2.models.remote.WeatherForecasts;

public interface IWeatherforecastService {

    public boolean InsertForecasts(WeatherForecasts forecasts);
    public WeatherForecasts SelectForecasts(String city_name);
    public boolean UpdateForecasts(String city_name);

    public boolean DeleteForecasts(String city_name);
}
