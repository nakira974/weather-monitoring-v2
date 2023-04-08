package com.example.weathermonitoringv2.services;



import com.example.weathermonitoringv2.models.remote.WeatherForecasts;

import java.util.Optional;
import java.util.concurrent.Future;


public interface IHttpClientService {
    Future<Optional<WeatherForecasts>> getForecastByCityAsync(String city, String country);
}
