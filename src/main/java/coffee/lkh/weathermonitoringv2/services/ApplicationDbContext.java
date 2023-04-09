package coffee.lkh.weathermonitoringv2.services;

import coffee.lkh.weathermonitoringv2.models.remote.weatherbit.CityWeatherForecasts;
import coffee.lkh.weathermonitoringv2.models.remote.weatherbit.Datum;
import coffee.lkh.weathermonitoringv2.repositories.IDatumRepository;
import coffee.lkh.weathermonitoringv2.repositories.IWeatherRepository;
import coffee.lkh.weathermonitoringv2.repositories.ICityWeatherForecastsRepository;
import coffee.lkh.weathermonitoringv2.services.base.IDbContext;
import coffee.lkh.weathermonitoringv2.services.base.IHttpClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class ApplicationDbContext implements IDbContext {

    private final Logger _logger;
    final ICityWeatherForecastsRepository _weatherForecastsRepository;
    final IWeatherRepository _weatherRepository;
    final IDatumRepository _datumRepository;

    protected final IHttpClientService _httpClientService;

    private final ThreadPoolExecutor _executor;

    public ApplicationDbContext(ICityWeatherForecastsRepository weatherForecastsRepository, IWeatherRepository weatherRepository, IDatumRepository datumRepository, IHttpClientService httpClientService) {
        _weatherRepository = weatherRepository;
        _datumRepository = datumRepository;
        _httpClientService = httpClientService;
        _logger = LoggerFactory.getLogger(IDbContext.class);
        _weatherForecastsRepository = weatherForecastsRepository;
        _executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    public Future<Boolean> insertOrUpdateForecastsAsync(CityWeatherForecasts forecasts){
        Future<Boolean> result = null;
        try{

            result = _executor.submit(() -> {
                var cityInfo = _httpClientService.getCityInfoAsync(forecasts.getLocation());
                double radius = 15;
                var isInserted = Boolean.FALSE;
                try{
                    var forecastsToInsert = new ArrayList<Datum>();
                    //Avant le foreach un peu d'opti je recupère mon future
                    if(cityInfo.get().isPresent()) radius = cityInfo.get().get().getDistance();
                    for(var data : forecasts.getData()){
                        //We linked our data by date and location in addition to link the ID in mongodb...
                        data.getWeather().setMeasureDate(data.getDatetime());
                        data.getWeather().setLocation(forecasts.getLon(), forecasts.getLat());
                        data.setLocation(forecasts.getLon(), forecasts.getLat());
                        //Check if entities are still existing
                        var registeredWeather = _weatherRepository.findDistinctByLocationAndForecastDate(data.getWeather().getLocation()[0], data.getWeather().getLocation()[1],radius,data.getWeather().getMeasureDate());
                        data.setWeather(Objects.requireNonNullElseGet(registeredWeather, () -> _weatherRepository.save(data.getWeather())));
                        var registeredData = _datumRepository.findDistinctByLocationAndForecastDate(data.getLocation()[0], data.getLocation()[1],radius, data.getMeasureDate());
                        if(registeredData == null)forecastsToInsert.add(data);
                    }
                    var insertedDatum = _datumRepository.saveAll(forecastsToInsert);
                    forecasts.setData(insertedDatum);
                    //Check if a city doesn't exist in our database
                    var registeredCityForecasts = _weatherForecastsRepository.findDistinctByCity_nameAndCountry_codeAndState_code(
                            forecasts.getCity_name(), forecasts.getCountry_code(), forecasts.getState_code());
                    //If the city is uknown then save it
                    if(registeredCityForecasts == null){
                        forecasts.setLocation();
                        registeredCityForecasts=  _weatherForecastsRepository.save(forecasts);
                        _logger.info(String.format("Entity %s %s %s has been inserted",
                                registeredCityForecasts.getCity_name(),
                                registeredCityForecasts.getCountry_code(),
                                registeredCityForecasts.getState_code()));

                    }
                    //Else if city is already stored, we just update it (datum ids -> each linked to a weather id)
                    else{
                        var oldData = registeredCityForecasts.getData();
                        oldData.addAll(forecasts.getData());
                        _weatherForecastsRepository.save(registeredCityForecasts);
                        _logger.info(String.format("Entity %s %s %s has been updated",
                                registeredCityForecasts.getCity_name(),
                                registeredCityForecasts.getCountry_code(),
                                registeredCityForecasts.getState_code()));
                    }

                    isInserted = Boolean.TRUE;
                }catch (Exception ex){
                    _logger.error(String.format("Can't fetch %s %s weather forecasts!", forecasts.getCity_name(), forecasts.getCountry_code()));
                }
                return isInserted;
            });
        }catch (Exception ex){
            _logger.error("Thread pool executor error in IDbService implementation");
        }
        return result;
    }

    @Override
    public Future<Boolean> deleteForecastsAsync(String city, String country, Optional<String> state) {
        Future<Boolean> result = null;
        try{
            result = _executor.submit(() -> {
                var isDeleted = Boolean.FALSE;
                try {

                    var registeredEntity = state.map(s -> _weatherForecastsRepository.findDistinctByCity_nameAndCountry_codeAndState_code(city, country, s)).or(() -> Optional.ofNullable(_weatherForecastsRepository.findDistinctByCity_nameAndCountry_code(city, country)));
                    if(registeredEntity.isEmpty()) throw  new Exception("Could not find entity to delete !");
                    registeredEntity.get().getData().forEach(datum -> {
                        try{
                            _weatherRepository.delete(datum.getWeather());
                            _logger.warn(String.format("Entity 'weather' %s has been deleted", datum.getWeather().getId()));
                        }catch (Exception ex){
                            _logger.error(String.format("No 'weather' related entity has been deleted for ID : %s", datum.getId()));
                        }
                        try{
                            _datumRepository.delete(datum);
                            _logger.warn(String.format("Entity 'datum' %s has been deleted", datum.getId()));
                        }catch (Exception ex){
                            _logger.error(String.format("No 'datum' related entity has been deleted for ID : %s", registeredEntity.get().getId()));
                        }
                    });
                    try {
                        _weatherForecastsRepository.delete(registeredEntity.get());
                        _logger.warn(String.format("Entity 'weather' %s has been deleted", registeredEntity.get().getId()));
                    }catch (Exception ex){
                        _logger.error(String.format("No 'city_weather_forecasts' entity has been deleted for ID : %s", registeredEntity.get().getId()));
                    }
                    isDeleted = Boolean.TRUE;
                }catch (Exception ex){
                    _logger.error("Error while deleting entity!");
                }
                return isDeleted;
            });

            }catch (Exception ex){
            _logger.warn("Thread pool executor in IDbService");
        }
        return result;
    }

    @Override
    public Future<Optional<CityWeatherForecasts>> selectForecastsAsync(String city, String country, Optional<String> state) {
        Future<Optional<CityWeatherForecasts>> result = null;
        try{
            result = _executor.submit(() -> {
                Optional<CityWeatherForecasts> forecasts = Optional.empty();
                try{
                    forecasts = state.map(s -> _weatherForecastsRepository.findDistinctByCity_nameAndCountry_codeAndState_code(city, country, s)).or(() -> Optional.ofNullable(_weatherForecastsRepository.findDistinctByCity_nameAndCountry_code(city, country)));
              }  catch (Exception ex){
                  _logger.error("Error while selecting entity!");
              }
                return forecasts;
            });
        }catch (Exception ex){
            _logger.warn("Thread pool executor in IDbService");
        }
        return result;
    }
}
