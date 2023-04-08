package com.example.weathermonitoringv2.models.remote;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;


@Document(value = "weather_forecasts")
public class WeatherForecasts{
    @Id
    private String id;
    @JsonProperty("data")
    public ArrayList<Datum> getData() {
        return this.data; }
    public void setData(ArrayList<Datum> data) {
        this.data = data; }
    ArrayList<Datum> data;
    @JsonProperty("city_name")
    public String getCity_name() {
        return this.city_name; }
    public void setCity_name(String city_name) {
        this.city_name = city_name; }
    String city_name;
    @JsonProperty("lon")
    public String getLon() {
        return this.lon; }
    public void setLon(String lon) {
        this.lon = lon; }
    String lon;
    @JsonProperty("timezone")
    public String getTimezone() {
        return this.timezone; }
    public void setTimezone(String timezone) {
        this.timezone = timezone; }
    String timezone;
    @JsonProperty("lat")
    public String getLat() {
        return this.lat; }
    public void setLat(String lat) {
        this.lat = lat; }
    String lat;
    @JsonProperty("country_code")
    public String getCountry_code() {
        return this.country_code; }
    public void setCountry_code(String country_code) {
        this.country_code = country_code; }
    String country_code;
    @JsonProperty("state_code")
    public String getState_code() {
        return this.state_code; }
    public void setState_code(String state_code) {
        this.state_code = state_code; }
    String state_code;
}
