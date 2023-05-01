package coffee.lkh.weathermonitoringv2.controllers;


import coffee.lkh.weathermonitoringv2.models.CityWeatherForecastDto;
import coffee.lkh.weathermonitoringv2.models.remote.weatherbitapi.CityWeatherForecasts;
import coffee.lkh.weathermonitoringv2.services.base.IDbContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import coffee.lkh.weathermonitoringv2.models.exceptions.WeatherforecastsNotFoundException;
import coffee.lkh.weathermonitoringv2.services.base.IHttpClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;

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
    @CrossOrigin
    @ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "Entity deleted correctly")
    @ExceptionHandler({ WeatherforecastsNotFoundException.class })
    public Map<String, String> home(@NotNull @AuthenticationPrincipal DefaultOAuth2User user) {
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
    @CrossOrigin
    @ResponseStatus(code = HttpStatus.ACCEPTED, reason = "Entity selected correctly")
    @ExceptionHandler({ WeatherforecastsNotFoundException.class })
    public ResponseEntity<List<CityWeatherForecastDto>> getWeatherInfo(@RequestParam  String city, @RequestParam  String country, Optional<String> state){
        var fetchFromMongoTask = _dbContext.selectForecastsAsync(city,country, Optional.empty());
        var result = new ArrayList<CityWeatherForecastDto>();
        try{
            var registeredEntity = fetchFromMongoTask.get();
            CityWeatherForecasts forecasts;
            if(registeredEntity.isEmpty()){
                var fetchFromApiTask = _httpClientService.getForecastByCityAsync(city ,country, state).get();
                if(fetchFromApiTask.isEmpty()) throw new Exception();
                forecasts = fetchFromApiTask.get();
                if(!_dbContext.insertOrUpdateForecastsAsync(forecasts).get()) throw new Exception("Database insert error!");
            }else
                forecasts = registeredEntity.get();


            return getListResponseEntity(result, forecasts);
        }catch (Exception ex){
            throw  new WeatherforecastsNotFoundException(String.format("Could not fetch weather data for city=%s, country=%s\nCAUSE: %s", city, country, ex.getMessage()));
        }
    }
    @RequestMapping("/weather-map")
    public String login(){
        return "pages/weather-map";
    }

    @PatchMapping(value = "/weather")
    @CrossOrigin
    @ResponseStatus(code = HttpStatus.CREATED, reason = "Entity updated correctly")
    @ExceptionHandler({ WeatherforecastsNotFoundException.class })
    public ResponseEntity<List<CityWeatherForecastDto>> updateWeatherInfo(@RequestParam  String city, @RequestParam  String country, Optional<String> state){
        var result = new ArrayList<CityWeatherForecastDto>();
        try{
            CityWeatherForecasts forecasts;
            var fetchFromApiTask = _httpClientService.getForecastByCityAsync(city ,country, state).get();
            if(fetchFromApiTask.isEmpty()) throw new Exception();
            forecasts = fetchFromApiTask.get();
            if(!_dbContext.insertOrUpdateForecastsAsync(forecasts).get()) throw new Exception("Database update error!");


            return getListResponseEntity(result, forecasts);
        }catch (Exception ex){
            throw  new WeatherforecastsNotFoundException(String.format("Could not fetch weather data for city=%s, country=%s\nCAUSE: %s", city, country, ex.getMessage()));
        }
    }

    @Contract("_, _ -> new")
    @NotNull
    private ResponseEntity<List<CityWeatherForecastDto>> getListResponseEntity(ArrayList<CityWeatherForecastDto> result, @NotNull CityWeatherForecasts forecasts) {
        forecasts.getData().forEach(x->{
            var forecast = new CityWeatherForecastDto(
                    forecasts.getCity_name(),
                    forecasts.getCountry_code(),
                    forecasts.getState_code(),
                    forecasts.getLocation(),
                    x.getDatetime(),
                    x.getTemp(),
                    x.getWeather().getDescription());
            result.add(forecast);
        });

        return new ResponseEntity<List<CityWeatherForecastDto>>(result, HttpStatusCode.valueOf(200));
    }
}
