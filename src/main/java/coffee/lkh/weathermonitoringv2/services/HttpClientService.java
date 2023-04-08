package coffee.lkh.weathermonitoringv2.services;

import coffee.lkh.weathermonitoringv2.models.exceptions.WeatherforecastsNotFoundException;
import coffee.lkh.weathermonitoringv2.models.remote.CityWeatherForecasts;
import coffee.lkh.weathermonitoringv2.repositories.ICityWeatherForecastsRepository;
import coffee.lkh.weathermonitoringv2.services.base.IHttpClientService;
import coffee.lkh.weathermonitoringv2.services.remote.WeatherForecastsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.Call;

@Service
public class HttpClientService implements IHttpClientService {
    private final Logger _logger;

    @Value("${WEATHER_FORECASTS_API_KEY}")
    private String _apiKey;

    @Value("${WEATHER_FORECASTS_API_BASE_URI}")
    private String _baseUri;

    @Autowired
    ICityWeatherForecastsRepository _weatherForecastsRepository;

    private WeatherForecastsApi _weatherForecastsApi;
    private final ThreadPoolExecutor _executor;
    public HttpClientService() {
        _logger = LoggerFactory.getLogger(IHttpClientService.class);
        _executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public Future<Optional<CityWeatherForecasts>> getForecastByCityAsync(String city, String country) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(_baseUri)
                .addConverterFactory(JacksonConverterFactory.create()) // or any other JSON converter you prefer
                .build();

        _weatherForecastsApi = retrofit.create(WeatherForecastsApi.class);
        Future<Optional<CityWeatherForecasts>> result = null;
        try {
            result = _executor.submit(() -> {
                Optional<CityWeatherForecasts> taskResult = Optional.empty();
                try {
                    Call<CityWeatherForecasts> call = _weatherForecastsApi.getForecastByCity(_apiKey, city, country, "31");
                    Response<CityWeatherForecasts> response = call.execute();
                    if (response.isSuccessful()) {
                        taskResult = Optional.ofNullable(response.body());
                    } else {
                        throw new WeatherforecastsNotFoundException("Response unsuccessful!");
                    }
                } catch (IOException ex) {
                    _logger.error(String.format("Can't fetch %s %s weather forecasts!", city, country));
                }

                return taskResult;
            });
        } catch (Exception ex) {
            _logger.error(String.format("Can't fetch %s %s weather forecasts!", city, country));
        }
        return result;
    }
}