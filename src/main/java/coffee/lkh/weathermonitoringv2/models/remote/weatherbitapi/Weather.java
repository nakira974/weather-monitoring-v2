package coffee.lkh.weathermonitoringv2.models.remote.weatherbitapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TimeSeries;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "weather")
@TimeSeries(collation = "weather", timeField = "forecast_date" )
@CompoundIndex(def = "{'location': '2dsphere'}")
public class Weather{
    public String getId() {
        return id;
    }

    @Id
    @JsonIgnore
    private String id;
    @JsonProperty("icon")
    public String getIcon() {
        return this.icon; }
    public void setIcon(String icon) {
        this.icon = icon; }

    @Field("icon")
    String icon;
    @JsonProperty("code")
    public String getCode() {
        return this.code; }
    public void setCode(String code) {
        this.code = code; }
    @Field("code")
    String code;
    @JsonProperty("description")
    public String getDescription() {
        return this.description; }
    public void setDescription(String description) {
        this.description = description; }

    @Field("description")
    String description;

    public double[] getLocation() {
        return location;
    }

    public void setLocation(String longitude, String latitude) {
        this.location = new double[]{Double.parseDouble(longitude), Double.parseDouble(latitude)};
    }

    @Field("location")
    public double[] location;

    public Date getMeasureDate() {
        return this.forecast_date; }
    public void setMeasureDate(Date timestamp_utc) {
        this.forecast_date = timestamp_utc; }
    @Field("forecast_date")
    Date forecast_date;

}
