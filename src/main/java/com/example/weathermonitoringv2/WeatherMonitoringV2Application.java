package com.example.weathermonitoringv2;

import com.example.weathermonitoringv2.repositories.IWeatherforecastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collections;

@SpringBootApplication
@EnableMongoRepositories
public class WeatherMonitoringV2Application  {

    @Autowired
    IWeatherforecastRepository weatherforecastRepository;
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WeatherMonitoringV2Application.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "8083"));
        app.run(args);
    }
}
