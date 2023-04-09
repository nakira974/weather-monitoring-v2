package coffee.lkh.weathermonitoringv2.models.remote.weatherbit.rapidapi.geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityInfo {
    @JsonProperty("Country")
    public String getCountry() {
        return this.country; }
    public void setCountry(String country) {
        this.country = country; }
    String country;
    @JsonProperty("CountryId")
    public String getCountryId() {
        return this.countryId; }
    public void setCountryId(String countryId) {
        this.countryId = countryId; }
    String countryId;
    @JsonProperty("City")
    public String getCity() {
        return this.city; }
    public void setCity(String city) {
        this.city = city; }
    String city;
    @JsonProperty("Population")
    public int getPopulation() {
        return this.population; }
    public void setPopulation(int population) {
        this.population = population; }
    int population;
    @JsonProperty("Distance")
    public double getDistance() {
        return this.distance; }
    public void setDistance(double distance) {
        this.distance = distance; }
    double distance;
    @JsonProperty("Latitude")
    public double getLatitude() {
        return this.latitude; }
    public void setLatitude(double latitude) {
        this.latitude = latitude; }
    double latitude;
    @JsonProperty("Longitude")
    public double getLongitude() {
        return this.longitude; }
    public void setLongitude(double longitude) {
        this.longitude = longitude; }
    double longitude;
}
