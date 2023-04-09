package coffee.lkh.weathermonitoringv2.repositories;

import coffee.lkh.weathermonitoringv2.models.remote.Datum;
import coffee.lkh.weathermonitoringv2.models.remote.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;

public interface IDatumRepository extends MongoRepository<Datum, String> {
    @Query("{weather:'?0', datetime: '?1'}")

    public Datum findDistinctByWeatherAndDate(Weather weather, Date datetime);

    @Query("{'location': { $nearSphere: { $geometry: { type: 'Point', coordinates: [?0, ?1] } } }, 'forecast_date': ?2}")
    public Datum findDistinctByLocationAndForecastDate(double longitude, double latitude, Date forecastDate);
}
