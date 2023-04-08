package coffee.lkh.weathermonitoringv2.controllers;


import coffee.lkh.weathermonitoringv2.models.remote.CityWeatherForecasts;
import coffee.lkh.weathermonitoringv2.services.base.IDbContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import coffee.lkh.weathermonitoringv2.models.Weatherforecast;
import coffee.lkh.weathermonitoringv2.models.exceptions.WeatherforecastsNotFoundException;
import coffee.lkh.weathermonitoringv2.services.base.IHttpClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;

@RestController("weather_forecasts")
public class WeatherforecastController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherforecastController.class);
    protected final IHttpClientService _httpClientService;
    protected final IDbContext _dbContext;

    public WeatherforecastController(IHttpClientService httpClientService, IDbContext dbContext){
        _httpClientService = httpClientService;
        _dbContext = dbContext;
    }

    @GetMapping("/")
    @ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "Entity deleted correctly")
    @ExceptionHandler({ WeatherforecastsNotFoundException.class })
    public Map<String, String> home(@AuthenticationPrincipal DefaultOAuth2User user) {
        return Map.of("message", "You are logged in, " + user.getName() + "!");
    }

    @DeleteMapping("/weather")
    public ResponseEntity<String> deleteWeatherInfo(@RequestParam  String city, @RequestParam  String country, @RequestParam Optional<String> state){
        try {
            var isDelete = _dbContext.deleteForecastsAsync(city, country, state).get();
            if(isDelete)
                return new ResponseEntity<String>(String.format("Entity %s %s has been deleted", city, country), HttpStatusCode.valueOf(204));
            else
                throw new Exception();
        }catch (Exception ex){
            throw  new WeatherforecastsNotFoundException(String.format("Could not delete weather data for city=%s, country=%s\nCAUSE: %s", city, country, ex.getMessage()));
        }
    }

    @GetMapping(value = "/weather")
    @ResponseStatus(code = HttpStatus.OK, reason = "Entity selected correctly")
    @ExceptionHandler({ WeatherforecastsNotFoundException.class })
    public ResponseEntity<List<Weatherforecast>> getWeatherInfo(@RequestParam  String city, @RequestParam  String country, Optional<String> state){
        var fetchFromMongoTask = _dbContext.selectForecastsAsync(city,country, Optional.empty());
        var result = new ArrayList<Weatherforecast>();
        try{
            CityWeatherForecasts forecasts;
            if(fetchFromMongoTask.get().isEmpty()){
                var fetchFromApiTask = _httpClientService.getForecastByCityAsync(city ,country, state).get();
                if(fetchFromApiTask.isEmpty()) throw new Exception();
                forecasts = fetchFromApiTask.get();
                if(!_dbContext.insertForecastsAsync(forecasts).get()) throw new Exception("Database insert error!");
            }else
                forecasts = fetchFromMongoTask.get().get();


            forecasts.getData().forEach(x->{
                var forecast = new Weatherforecast();
                forecast.Date = x.getDatetime();
                forecast.Summary = x.getWeather().getDescription();
                forecast.TemperatureF = x.getTemp();
                forecast.TemperatureC = (x.getTemp()-32) *(5/9);
                result.add(forecast);
            });

            return new ResponseEntity<List<Weatherforecast>>(result, HttpStatusCode.valueOf(200));
        }catch (Exception ex){
            throw  new WeatherforecastsNotFoundException(String.format("Could not fetch weather data for city=%s, country=%s\nCAUSE: %s", city, country, ex.getMessage()));
        }
    }
}
