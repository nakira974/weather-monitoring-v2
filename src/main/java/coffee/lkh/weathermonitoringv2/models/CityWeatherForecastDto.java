package coffee.lkh.weathermonitoringv2.models;

import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityWeatherForecastDto {

    public CityWeatherForecastDto(){};

    public CityWeatherForecastDto(String city, String country, String state, double[] location, Date date, int temperatureF, String summary) {
        this.city = city;
        this.country = country;
        this.state = state;
        this.location = location;
        this.date = date;
        this.temperatureF = temperatureF;
        this.summary = summary;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("location")
    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    @JsonProperty("date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @JsonProperty("temperatureC")
    public int getTemperatureC() {
        return (this.temperatureF-32) *(5/9);
    }

    @JsonProperty("temperatureF")
    public int getTemperatureF() {
        return this.temperatureF;
    }

    public void setTemperatureF(int temperatureF) {
        this.temperatureF = temperatureF;
    }

    public String getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    private String city;

    private String country;

    private String state;

    private double[] location;

    private Date date;

    private int temperatureC;



    private int temperatureF;

    private String summary;
}
