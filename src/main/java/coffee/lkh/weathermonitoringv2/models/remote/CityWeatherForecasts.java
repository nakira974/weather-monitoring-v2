package coffee.lkh.weathermonitoringv2.models.remote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Document(value = "city_weather_forecasts")
@CompoundIndexes({
        @CompoundIndex(name = "location_index", def = "{'location' : '2dsphere'}")
})
public class CityWeatherForecasts {
    public String getId() {
        return id;
    }

    @Id
    @JsonIgnore
    private String id;
    @JsonProperty("city_name")
    public String getCity_name() {
        return this.city_name; }
    public void setCity_name(String city_name) {
        this.city_name = city_name; }

    @Field("city_name")
    String city_name;
    @JsonProperty("state_code")
    public String getState_code() {
        return this.state_code; }
    public void setState_code(String state_code) {
        this.state_code = state_code; }

    @Field("state_code")
    @JsonIgnore
    String state_code;
    @JsonProperty("country_code")
    public String getCountry_code() {
        return this.country_code; }
    public void setCountry_code(String country_code) {
        this.country_code = country_code; }


    public double[] getLocation() {
        return location;
    }

    public void setLocation() {
        this.location = new double[]{Double.parseDouble(this.getLon()), Double.parseDouble(this.getLat())};
    }

    @Field("location")
    public double[] location;
    @Field("country_code")
    String country_code;
    @JsonProperty("lat")
    public String getLat() {
        return this.lat; }
    public void setLat(String lat) {
        this.lat = lat; }
    @Transient
    String lat;
    @JsonProperty("lon")
    public String getLon() {
        return this.lon; }
    public void setLon(String lon) {
        this.lon = lon; }
    @Transient
    String lon;
    @JsonProperty("timezone")
    public String getTimezone() {
        return this.timezone; }
    public void setTimezone(String timezone) {
        this.timezone = timezone; }
    @Field("timezone")
    String timezone;
    @JsonProperty("data")
    public List<Datum> getData() {
        return this.data; }
    public void setData(List<Datum> data) {
        this.data = data; }

    @DBRef
    @Field("data")
    List<Datum> data;
}
