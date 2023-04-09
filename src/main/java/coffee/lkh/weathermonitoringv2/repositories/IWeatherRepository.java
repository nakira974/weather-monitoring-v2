package coffee.lkh.weathermonitoringv2.repositories;

import coffee.lkh.weathermonitoringv2.models.remote.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface IWeatherRepository extends MongoRepository<Weather, String> {

    @Query("{code:'?0'}")

    public List<Weather> findByCode(String code);

    @Query("{'location': { $nearSphere: { $geometry: { type: 'Point', coordinates: [?0, ?1] } } }, 'forecast_date': ?2}")
    public Weather findDistinctByLocationAndForecastDate(double longitude, double latitude, Date forecastDate);
}
