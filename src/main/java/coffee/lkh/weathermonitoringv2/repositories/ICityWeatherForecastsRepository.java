package coffee.lkh.weathermonitoringv2.repositories;

import coffee.lkh.weathermonitoringv2.models.remote.weatherbitapi.CityWeatherForecasts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;


@Repository
@EnableMongoRepositories(basePackages = "coffee.lkh.weathermonitoringv2.repositories")
public interface ICityWeatherForecastsRepository extends MongoRepository<CityWeatherForecasts, String> {

    @Query("{city_name:'?0', country_code:  '?1', state_code:  '?2'}")
    public CityWeatherForecasts findDistinctByCity_nameAndCountry_codeAndState_code(String city_name, String country, String state);

    @Query("{city_name:'?0', country_code:  '?1'}")
    public CityWeatherForecasts findDistinctByCity_nameAndCountry_code(String city_name, String country);

    @Query("{city_name:'?0', country_code:  '?1', state_code:  '?2'}")
    public CityWeatherForecasts updateByCity_nameAndAndCountry_codeAndState_code(String city_name, String country, String state);

    @Query("{city_name:'?0'}")
    public boolean deleteDistinctByCity_name(String city_name);


}

