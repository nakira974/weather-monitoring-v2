package coffee.lkh.weathermonitoringv2.repositories;

import coffee.lkh.weathermonitoringv2.models.remote.weatherbitapi.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IWeatherRepository extends MongoRepository<Weather, String> {

    @Query("{code:'?0'}")

    public List<Weather> findByCode(String code);


    @Query("{$and: [{'location': { $geoWithin: { $centerSphere: [[?0, ?1], ?2] } }}, {'forecast_date': { $eq: ?3 }}]}")
    public Weather findDistinctByLocationAndForecastDate(double longitude, double latitude, double radius, Date forecastDate);

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
    Optional<Weather> findUniqueByForecastDateAndLocation(Date forecastDate, List<Double> location, double radius);
}
