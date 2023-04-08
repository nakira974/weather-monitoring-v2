package coffee.lkh.weathermonitoringv2.services;

import coffee.lkh.weathermonitoringv2.models.Weatherforecast;
import coffee.lkh.weathermonitoringv2.models.remote.CityWeatherForecasts;
import coffee.lkh.weathermonitoringv2.models.remote.Datum;
import coffee.lkh.weathermonitoringv2.repositories.IDatumRepository;
import coffee.lkh.weathermonitoringv2.repositories.IWeatherRepository;
import coffee.lkh.weathermonitoringv2.repositories.ICityWeatherForecastsRepository;
import coffee.lkh.weathermonitoringv2.services.base.IDbContext;
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

    private final ThreadPoolExecutor _executor;

    public ApplicationDbContext(ICityWeatherForecastsRepository weatherForecastsRepository, IWeatherRepository weatherRepository, IDatumRepository datumRepository) {
        _weatherRepository = weatherRepository;
        _datumRepository = datumRepository;
        _logger = LoggerFactory.getLogger(IDbContext.class);
        _weatherForecastsRepository = weatherForecastsRepository;
        _executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    public Future<Boolean> insertForecastsAsync(CityWeatherForecasts forecasts){
        Future<Boolean> result = null;
        try{
            result = _executor.submit(() -> {
                var isInserted = Boolean.FALSE;
                try{
                    var forecastsToInsert = new ArrayList<Datum>();
                    for(var data : forecasts.getData()){
                        var registeredWeather = _weatherRepository.findDistinctByCode(data.getWeather().getCode());
                        data.setWeather(Objects.requireNonNullElseGet(registeredWeather, () -> _weatherRepository.save(data.getWeather())));
                        var registeredData = _datumRepository.findDistinctByWeatherAndDate(data.getWeather(), data.getDatetime());
                        if(registeredData == null)forecastsToInsert.add(data);
                    }
                    var insertedDatum = _datumRepository.saveAll(forecastsToInsert);
                    forecasts.setData(insertedDatum);
                    var registeredCityForecasts = _weatherForecastsRepository.findDistinctByCity_nameAndCountry_codeAndState_code(
                            forecasts.getCity_name(), forecasts.getCountry_code(), forecasts.getState_code());
                    if(registeredCityForecasts == null){
                        forecasts.setLocation();
                        registeredCityForecasts=  _weatherForecastsRepository.save(forecasts);
                        _logger.info(String.format("Entity %s %s %s has been inserted",
                                registeredCityForecasts.getCity_name(),
                                registeredCityForecasts.getCountry_code(),
                                registeredCityForecasts.getState_code()));

                    }else{
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
                        _weatherRepository.delete(datum.getWeather());
                        _datumRepository.delete(datum);
                    });
                    _weatherForecastsRepository.delete(registeredEntity.get());
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
