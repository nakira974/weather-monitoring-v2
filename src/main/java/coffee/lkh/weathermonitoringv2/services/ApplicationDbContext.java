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
                var fetchCityInfoTask = _httpClientService.getCityInfoAsync(new double[]{Double.parseDouble(forecasts.getLon()), Double.parseDouble(forecasts.getLat())});
                var cityInfo = fetchCityInfoTask.get();
                double radius = 15;
                var isInserted = Boolean.FALSE;
                try{
                    var forecastsToInsert = new ArrayList<Datum>();
                    //Avant le foreach un peu d'opti je recupère mon future
                    if(cityInfo.isPresent()) radius = cityInfo.get().stream().findFirst().orElseThrow().getDistance();
                    for(var data : forecasts.getData()){
                        //We linked our data by date and location in addition to link the ID in mongodb...
                        data.getWeather().setMeasureDate(data.getDatetime());
                        data.getWeather().setLocation(forecasts.getLon(), forecasts.getLat());
                        data.setLocation(forecasts.getLon(), forecasts.getLat());
                        //Check if entities are still existing
                        var longitude = data.getWeather().getLocation()[0];
                        var latitude = data.getWeather().getLocation()[1];
                        ArrayList<Double> location = new ArrayList<Double>() {};
                        location.add(longitude);
                        location.add(latitude);

                        var registeredWeather = _weatherRepository.findUniqueByForecastDateAndLocation(data.getWeather().getMeasureDate(), location , radius);
                        //If no forecast at this location at this time then we save into db
                        if(registeredWeather.isEmpty())
                            data.setWeather(_weatherRepository.save(data.getWeather()));
                        //If a forecast is present at the same place at the same time then
                        else
                            data.setWeather(registeredWeather.get());

                         longitude = data.getWeather().getLocation()[0];
                         latitude = data.getWeather().getLocation()[1];
                         location = new ArrayList<Double>() {};
                         location.add(longitude);
                         location.add(latitude);
                        var registeredData = _datumRepository.findUniqueByForecastDateAndLocation(data.getMeasureDate(), location, radius);
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
                        _logger.info(String.format("\u001B[32m Entity %s %s %s has been inserted\u001B[0m",
                                registeredCityForecasts.getCity_name(),
                                registeredCityForecasts.getCountry_code(),
                                registeredCityForecasts.getState_code()));

                    }
                    //Else if city is already stored, we just update it (datum ids -> each linked to a weather id)
                    else{
                        var oldData = registeredCityForecasts.getData();
                        oldData.addAll(forecasts.getData());
                        _weatherForecastsRepository.save(registeredCityForecasts);
                        _logger.info(String.format("\u001B[36m Entity %s %s %s has been updated\u001B[0m",
                                registeredCityForecasts.getCity_name(),
                                registeredCityForecasts.getCountry_code(),
                                registeredCityForecasts.getState_code()));
                    }

                    isInserted = Boolean.TRUE;
                }catch (Exception ex){
                    _logger.error(String.format("\u001B[31m Can't select  %s %s weather forecasts in mongodb!\u001B[0m", forecasts.getCity_name(), forecasts.getCountry_code()));
                }
                return isInserted;
            });
        }catch (Exception ex){
            _logger.error("\u001B[31m Thread pool executor error in IDbService implementation\u001B[0m");
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
                    if(registeredEntity.isEmpty()) throw  new Exception("\u001B[31m Could not find entity to delete ! \u001B[0m");
                    registeredEntity.get().getData().forEach(datum -> {
                        try{
                            _weatherRepository.delete(datum.getWeather());
                            _logger.warn(String.format("\u001B[34m Entity 'weather' %s has been deleted\u001B[0m", datum.getWeather().getId()));
                        }catch (Exception ex){
                            _logger.error(String.format("\u001B[31m No 'weather' related entity has been deleted for ID : %s \u001B[0m", datum.getId()));
                        }
                        try{
                            _datumRepository.delete(datum);
                            _logger.warn(String.format("\u001B[34m Entity 'datum' %s has been deleted\u001B[0m", datum.getId()));
                        }catch (Exception ex){
                            _logger.error(String.format("\u001B[31m No 'datum' related entity has been deleted for ID : %s \u001B[0m", registeredEntity.get().getId()));
                        }
                    });
                    try {
                        _weatherForecastsRepository.delete(registeredEntity.get());
                        _logger.warn(String.format("\u001B[34m Entity 'weather' %s has been deleted", registeredEntity.get().getId()));
                    }catch (Exception ex){
                        _logger.error(String.format("\u001B[31m No 'city_weather_forecasts' entity has been deleted for ID : %s \u001B[0m", registeredEntity.get().getId()));
                    }
                    isDeleted = Boolean.TRUE;
                }catch (Exception ex){
                    _logger.error("\u001B[31m Error while deleting entity!\u001B[0m");
                }
                return isDeleted;
            });

            }catch (Exception ex){
            _logger.warn("\u001B[31m Thread pool executor in IDbService\u001B[0m");
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
