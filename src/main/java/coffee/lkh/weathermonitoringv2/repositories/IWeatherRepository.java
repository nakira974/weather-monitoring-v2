package coffee.lkh.weathermonitoringv2.repositories;

import coffee.lkh.weathermonitoringv2.models.remote.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface IWeatherRepository extends MongoRepository<Weather, String> {

    @Query("{code:'?0'}")

    public Weather findDistinctByCode(String code);
}
