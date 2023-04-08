package com.example.weathermonitoringv2.controllers;



import com.example.weathermonitoringv2.models.Weatherforecast;
import com.example.weathermonitoringv2.models.exceptions.WeatherforecastsNotFoundException;
import com.example.weathermonitoringv2.services.IHttpClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController("weather_forecasts")
public class WeatherforecastController {

    protected final IHttpClientService _httpClientService;

    public WeatherforecastController(IHttpClientService httpClientService){
        _httpClientService = httpClientService;
    }


    @PutMapping("/weather/{city}/{country}")
    @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    @ExceptionHandler({ WeatherforecastsNotFoundException.class })
    public ResponseEntity<List<Weatherforecast>> getWeatherInfo(@PathVariable String city, @PathVariable String country){
        var result = new ArrayList<Weatherforecast>();
        try{
            var forecasts = _httpClientService.getForecastByCityAsync(city ,country).get();
            if(forecasts.isEmpty()) throw new Exception();
            forecasts.get().getData().forEach(x->{
                var forecast = new Weatherforecast();
                forecast.Date = Date.valueOf(x.getDatetime());
                forecast.Summary = x.getWeather().getDescription();
                forecast.TemperatureF = x.getTemp();
                forecast.TemperatureC = (x.getTemp()-32) *(5/9);
                result.add(forecast);
            });

            return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
        }catch (Exception ex){
            throw  new WeatherforecastsNotFoundException(String.format("Could not fetch weather data for city=%s, country=%s\nCAUSE: %s", city, country, ex.getMessage()));
        }
    }
}
