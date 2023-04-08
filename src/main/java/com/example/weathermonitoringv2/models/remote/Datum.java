package com.example.weathermonitoringv2.models.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Datum{
    @JsonProperty("valid_date")
    public String getValid_date() {
        return this.valid_date; }
    public void setValid_date(String valid_date) {
        this.valid_date = valid_date; }
    String valid_date;
    @JsonProperty("ts")
    public int getTs() {
        return this.ts; }
    public void setTs(int ts) {
        this.ts = ts; }
    int ts;
    @JsonProperty("datetime")
    public String getDatetime() {
        return this.datetime; }
    public void setDatetime(String datetime) {
        this.datetime = datetime; }
    String datetime;
    @JsonProperty("wind_gust_spd")
    public double getWind_gust_spd() {
        return this.wind_gust_spd; }
    public void setWind_gust_spd(double wind_gust_spd) {
        this.wind_gust_spd = wind_gust_spd; }
    double wind_gust_spd;
    @JsonProperty("wind_spd")
    public double getWind_spd() {
        return this.wind_spd; }
    public void setWind_spd(double wind_spd) {
        this.wind_spd = wind_spd; }
    double wind_spd;
    @JsonProperty("wind_dir")
    public int getWind_dir() {
        return this.wind_dir; }
    public void setWind_dir(int wind_dir) {
        this.wind_dir = wind_dir; }
    int wind_dir;
    @JsonProperty("wind_cdir")
    public String getWind_cdir() {
        return this.wind_cdir; }
    public void setWind_cdir(String wind_cdir) {
        this.wind_cdir = wind_cdir; }
    String wind_cdir;
    @JsonProperty("wind_cdir_full")
    public String getWind_cdir_full() {
        return this.wind_cdir_full; }
    public void setWind_cdir_full(String wind_cdir_full) {
        this.wind_cdir_full = wind_cdir_full; }
    String wind_cdir_full;
    @JsonProperty("temp")
    public int getTemp() {
        return this.temp; }
    public void setTemp(int temp) {
        this.temp = temp; }
    int temp;
    @JsonProperty("max_temp")
    public int getMax_temp() {
        return this.max_temp; }
    public void setMax_temp(int max_temp) {
        this.max_temp = max_temp; }
    int max_temp;
    @JsonProperty("min_temp")
    public int getMin_temp() {
        return this.min_temp; }
    public void setMin_temp(int min_temp) {
        this.min_temp = min_temp; }
    int min_temp;
    @JsonProperty("high_temp")
    public int getHigh_temp() {
        return this.high_temp; }
    public void setHigh_temp(int high_temp) {
        this.high_temp = high_temp; }
    int high_temp;
    @JsonProperty("low_temp")
    public double getLow_temp() {
        return this.low_temp; }
    public void setLow_temp(double low_temp) {
        this.low_temp = low_temp; }
    double low_temp;
    @JsonProperty("app_max_temp")
    public double getApp_max_temp() {
        return this.app_max_temp; }
    public void setApp_max_temp(double app_max_temp) {
        this.app_max_temp = app_max_temp; }
    double app_max_temp;
    @JsonProperty("app_min_temp")
    public double getApp_min_temp() {
        return this.app_min_temp; }
    public void setApp_min_temp(double app_min_temp) {
        this.app_min_temp = app_min_temp; }
    double app_min_temp;
    @JsonProperty("pop")
    public int getPop() {
        return this.pop; }
    public void setPop(int pop) {
        this.pop = pop; }
    int pop;
    @JsonProperty("precip")
    public int getPrecip() {
        return this.precip; }
    public void setPrecip(int precip) {
        this.precip = precip; }
    int precip;
    @JsonProperty("snow")
    public int getSnow() {
        return this.snow; }
    public void setSnow(int snow) {
        this.snow = snow; }
    int snow;
    @JsonProperty("snow_depth")
    public int getSnow_depth() {
        return this.snow_depth; }
    public void setSnow_depth(int snow_depth) {
        this.snow_depth = snow_depth; }
    int snow_depth;
    @JsonProperty("slp")
    public int getSlp() {
        return this.slp; }
    public void setSlp(int slp) {
        this.slp = slp; }
    int slp;
    @JsonProperty("pres")
    public double getPres() {
        return this.pres; }
    public void setPres(double pres) {
        this.pres = pres; }
    double pres;
    @JsonProperty("dewpt")
    public double getDewpt() {
        return this.dewpt; }
    public void setDewpt(double dewpt) {
        this.dewpt = dewpt; }
    double dewpt;
    @JsonProperty("rh")
    public double getRh() {
        return this.rh; }
    public void setRh(double rh) {
        this.rh = rh; }
    double rh;
    @JsonProperty("weather")
    public Weather getWeather() {
        return this.weather; }
    public void setWeather(Weather weather) {
        this.weather = weather; }
    Weather weather;
    @JsonProperty("clouds_low")
    public int getClouds_low() {
        return this.clouds_low; }
    public void setClouds_low(int clouds_low) {
        this.clouds_low = clouds_low; }
    int clouds_low;
    @JsonProperty("clouds_mid")
    public int getClouds_mid() {
        return this.clouds_mid; }
    public void setClouds_mid(int clouds_mid) {
        this.clouds_mid = clouds_mid; }
    int clouds_mid;
    @JsonProperty("clouds_hi")
    public int getClouds_hi() {
        return this.clouds_hi; }
    public void setClouds_hi(int clouds_hi) {
        this.clouds_hi = clouds_hi; }
    int clouds_hi;
    @JsonProperty("clouds")
    public int getClouds() {
        return this.clouds; }
    public void setClouds(int clouds) {
        this.clouds = clouds; }
    int clouds;
    @JsonProperty("vis")
    public int getVis() {
        return this.vis; }
    public void setVis(int vis) {
        this.vis = vis; }
    int vis;
    @JsonProperty("max_dhi")
    public int getMax_dhi() {
        return this.max_dhi; }
    public void setMax_dhi(int max_dhi) {
        this.max_dhi = max_dhi; }
    int max_dhi;
    @JsonProperty("uv")
    public int getUv() {
        return this.uv; }
    public void setUv(int uv) {
        this.uv = uv; }
    int uv;
    @JsonProperty("moon_phase")
    public double getMoon_phase() {
        return this.moon_phase; }
    public void setMoon_phase(double moon_phase) {
        this.moon_phase = moon_phase; }
    double moon_phase;
    @JsonProperty("moon_phase_lunation")
    public double getMoon_phase_lunation() {
        return this.moon_phase_lunation; }
    public void setMoon_phase_lunation(double moon_phase_lunation) {
        this.moon_phase_lunation = moon_phase_lunation; }
    double moon_phase_lunation;
    @JsonProperty("moonrise_ts")
    public int getMoonrise_ts() {
        return this.moonrise_ts; }
    public void setMoonrise_ts(int moonrise_ts) {
        this.moonrise_ts = moonrise_ts; }
    int moonrise_ts;
    @JsonProperty("moonset_ts")
    public int getMoonset_ts() {
        return this.moonset_ts; }
    public void setMoonset_ts(int moonset_ts) {
        this.moonset_ts = moonset_ts; }
    int moonset_ts;
    @JsonProperty("sunrise_ts")
    public int getSunrise_ts() {
        return this.sunrise_ts; }
    public void setSunrise_ts(int sunrise_ts) {
        this.sunrise_ts = sunrise_ts; }
    int sunrise_ts;
    @JsonProperty("sunset_ts")
    public int getSunset_ts() {
        return this.sunset_ts; }
    public void setSunset_ts(int sunset_ts) {
        this.sunset_ts = sunset_ts; }
    int sunset_ts;
}
