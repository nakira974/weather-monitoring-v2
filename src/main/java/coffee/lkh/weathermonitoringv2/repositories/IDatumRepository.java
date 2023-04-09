package coffee.lkh.weathermonitoringv2.repositories;

import coffee.lkh.weathermonitoringv2.models.remote.weatherbit.Datum;
import coffee.lkh.weathermonitoringv2.models.remote.weatherbit.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;

public interface IDatumRepository extends MongoRepository<Datum, String> {
    @Query("{weather:'?0', datetime: '?1'}")

    public Datum findDistinctByWeatherAndDate(Weather weather, Date datetime);

    @Query("{$and: [{'location': { $geoWithin: { $centerSphere: [[?0, ?1], ?2] } }}, {'forecast_date': { $eq: ?3 }}]}")
    public Datum findDistinctByLocationAndForecastDate(double longitude, double latitude, double radius, Date forecastDate);
}
