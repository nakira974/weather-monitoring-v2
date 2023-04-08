package com.example.weathermonitoringv2.repositories;

import com.example.weathermonitoringv2.models.remote.WeatherForecasts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;


@Repository
@EnableMongoRepositories(basePackages = "com.example.weathermonitoringv2.repositories")
public interface IWeatherforecastRepository extends MongoRepository<WeatherForecasts, String> {

    @Query("{city_name:'?0'}")
    public WeatherForecasts findDistinctByCity_name(String city_name);

    @Query("{city_name:'?0'}")
    public WeatherForecasts updateByCity_name(String city_name);

    @Query("{city_name:'?0'}")
    public boolean deleteDistinctByCity_name(String city_name);

}

