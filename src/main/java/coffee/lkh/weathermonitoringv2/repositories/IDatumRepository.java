package coffee.lkh.weathermonitoringv2.repositories;

import coffee.lkh.weathermonitoringv2.models.remote.weatherbitapi.Datum;
import coffee.lkh.weathermonitoringv2.models.remote.weatherbitapi.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IDatumRepository extends MongoRepository<Datum, String> {
    @Query("{weather:'?0', datetime: '?1'}")

    public Datum findDistinctByWeatherAndDate(Weather weather, Date datetime);

    @Query("{$and: [{'location': { $geoWithin: { $centerSphere: [[?0, ?1], ?2] } }}, {'forecast_date': { $eq: ?3 }}]}")
    public Datum findDistinctByLocationAndForecastDate(double longitude, double latitude, double radius, Date forecastDate);

    @Query("{" +
            "$and: [" +
            "  { forecast_date: { $eq: ?0 } }," +
            "  {" +
            "    location: {" +
            "      $geoWithin: {" +
            "        $centerSphere: [ ?1, ?2 ]" +
            "      }" +
            "    }" +
            "  }" +
            "]" +
            "}")
    Optional<Datum> findUniqueByForecastDateAndLocation(Date forecastDate, List<Double> location, double radius);
}
