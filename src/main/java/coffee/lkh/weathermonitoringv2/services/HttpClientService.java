package coffee.lkh.weathermonitoringv2.services;

import coffee.lkh.weathermonitoringv2.models.exceptions.WeatherforecastsNotFoundException;
import coffee.lkh.weathermonitoringv2.models.remote.weatherbit.CityWeatherForecasts;
import coffee.lkh.weathermonitoringv2.models.remote.weatherbit.rapidapi.geocode.CityInfo;
import coffee.lkh.weathermonitoringv2.repositories.ICityWeatherForecastsRepository;
import coffee.lkh.weathermonitoringv2.services.base.IHttpClientService;
import coffee.lkh.weathermonitoringv2.services.remote.GeocodeApi;
import coffee.lkh.weathermonitoringv2.services.remote.WeatherBitApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
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
    private String _weatherBitApiKey;

    @Value("${CITY_INFO_API_KEY}")
    private String _cityInfoApiKey;

    @Value("${WEATHER_FORECASTS_API_BASE_URI}")
    private String _weatherBitBaseUri;

    @Value("${CITY_INFO_API_BASE_URI}")
    private String _cityInfoBaseUri;

    @Value("${CITY_INFO_RAPID_API_HOST}")
    private String _cityInfoRapidApiHost;


    @Autowired
    ICityWeatherForecastsRepository _weatherForecastsRepository;

    private WeatherBitApi _weatherBitApi;
    private GeocodeApi _geocodeApi;
    private final ThreadPoolExecutor _executor;
    public HttpClientService() {
        _logger = LoggerFactory.getLogger(IHttpClientService.class);
        _executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    @Override
    public Future<Optional<CityWeatherForecasts>> getForecastByCityAsync(String city, String country, Optional<String> state) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(_weatherBitBaseUri)
                .addConverterFactory(JacksonConverterFactory.create()) // or any other JSON converter you prefer
                .build();

        _weatherBitApi = retrofit.create(WeatherBitApi.class);
        Future<Optional<CityWeatherForecasts>> result = null;
        try {
            result = _executor.submit(() -> {
                Optional<CityWeatherForecasts> taskResult = Optional.empty();
                try {
                    Call<CityWeatherForecasts> call;
                    if(state.isEmpty())
                        call = _weatherBitApi.getForecastByCityAndCountry(_weatherBitApiKey, city, country, "16");
                    else
                        call = _weatherBitApi.getForecastByCityAndCountryAndState(_weatherBitApiKey, city, country, state.get(), "16");

                    Response<CityWeatherForecasts> response = call.execute();

                    if (response.isSuccessful()) {
                        taskResult = Optional.ofNullable(response.body());
                    } else {
                        throw new WeatherforecastsNotFoundException(String.format("%d %s", response.code(), response.message()));
                    }
                } catch (WeatherforecastsNotFoundException ex) {
                    _logger.error(String.format("Can't fetch %s %s weather forecasts!", city, country));
                    throw ex;
                }

                return taskResult;
            });
        } catch (Exception ex) {
            _logger.error("Thread pool executor error in IHttpClientService implementation !");
            if(ex.getClass().equals(WeatherforecastsNotFoundException.class))
                throw ex;
        }
        return result;
    }

    @Override
    public Future<Optional<List<CityInfo>>> getCityInfoAsync(double[] location) {
        Future<Optional<List<CityInfo>>> result = null;
        try{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(_cityInfoBaseUri)
                    .addConverterFactory(JacksonConverterFactory.create()) // or any other JSON converter you prefer
                    .build();
            _geocodeApi = retrofit.create(GeocodeApi.class);
            result = _executor.submit(() -> {
                Optional<List<CityInfo>> taskResult = Optional.empty();
                try {
                    Call<List<CityInfo>> call;
                    call = _geocodeApi.getCityInfo(_cityInfoRapidApiHost, _cityInfoApiKey, 15000,  location[0], location[1]);
                    _logger.warn(String.format("Fetched longitude:%f latitude%f city information!", location[0], location[1] ));
                    Response<List<CityInfo>> response = call.execute();
                    if (response.isSuccessful()) {
                        taskResult = Optional.ofNullable(response.body());
                    } else {
                        throw new WeatherforecastsNotFoundException(String.format("%d %s", response.code(), response.message()));
                    }
                } catch (IOException ex) {
                    _logger.error(String.format("Can't fetch longitude:%f latitude%f city information!", location[0], location[1] ));
                }

                return taskResult;
            });
        }catch (Exception ex){
            _logger.error("Thread pool executor error in IHttpClientService implementation !");
        }
        return result;
    }
}