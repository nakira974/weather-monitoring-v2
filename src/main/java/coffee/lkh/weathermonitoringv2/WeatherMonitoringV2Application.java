package coffee.lkh.weathermonitoringv2;

import coffee.lkh.weathermonitoringv2.repositories.ICityWeatherForecastsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collections;

@SpringBootApplication
@EnableMongoRepositories
public class WeatherMonitoringV2Application  {

    @Autowired
    ICityWeatherForecastsRepository weatherforecastRepository;
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WeatherMonitoringV2Application.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "8083"));
        app.run(args);
    }
}
