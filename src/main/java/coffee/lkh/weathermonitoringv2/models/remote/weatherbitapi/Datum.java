package coffee.lkh.weathermonitoringv2.models.remote.weatherbitapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TimeSeries;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "datum")
@TimeSeries(collation = "datum", timeField = "forecast_date" )
@CompoundIndex(def = "{'location': '2dsphere'}")
public class Datum{

    public String getId() {
        return id;
    }

    @Id
    @JsonIgnore
    private String id;

    @JsonProperty("ts")
    public String getTs() {
        return this.ts; }
    public void setTs(String ts) {
        this.ts = ts; }
    private String ts;
    @JsonProperty("timestamp_local")
    public Date getLocalMeasureDate() {
        return this.timestamp_local; }
    public void setLocalMeasureDate(Date timestamp_local) {
        this.timestamp_local = timestamp_local; }

    @Transient
    private Date timestamp_local;
    @JsonProperty("timestamp_utc")
    public Date getMeasureDate() {
        return this.timestamp_utc; }
    public void setMeasureDate(Date timestamp_utc) {
        this.timestamp_utc = timestamp_utc; }
    @Transient
    private Date timestamp_utc;
    @JsonProperty("datetime")
    public Date  getDatetime() {
        return this.datetime; }

    public void setDatetime(Date  datetime) {
        this.datetime = datetime; }

    @Field("forecast_date")
    private Date  datetime;
    @JsonProperty("snow")
    public double getSnow() {
        return this.snow; }
    public void setSnow(double snow) {
        this.snow = snow; }
    private double snow;
    @JsonProperty("snow_depth")
    public int getSnow_depth() {
        return this.snow_depth; }
    public void setSnow_depth(int snow_depth) {
        this.snow_depth = snow_depth; }
    private int snow_depth;
    @JsonProperty("precip")
    public double getPrecip() {
        return this.precip; }
    public void setPrecip(double precip) {
        this.precip = precip; }
    private double precip;
    @JsonProperty("temp")
    public int getTemp() {
        return this.temp; }
    public void setTemp(int temp) {
        this.temp = temp; }
    private int temp;
    @JsonProperty("dewpt")
    public int getDewpt() {
        return this.dewpt; }
    public void setDewpt(int dewpt) {
        this.dewpt = dewpt; }
    private int dewpt;
    @JsonProperty("max_temp")
    public double getMax_temp() {
        return this.max_temp; }
    public void setMax_temp(double max_temp) {
        this.max_temp = max_temp; }
    private double max_temp;
    @JsonProperty("min_temp")
    public double getMin_temp() {
        return this.min_temp; }
    public void setMin_temp(double min_temp) {
        this.min_temp = min_temp; }
    private double min_temp;
    @JsonProperty("app_max_temp")
    public int getApp_max_temp() {
        return this.app_max_temp; }
    public void setApp_max_temp(int app_max_temp) {
        this.app_max_temp = app_max_temp; }
    private int app_max_temp;
    @JsonProperty("app_min_temp")
    public int getApp_min_temp() {
        return this.app_min_temp; }
    public void setApp_min_temp(int app_min_temp) {
        this.app_min_temp = app_min_temp; }
    private int app_min_temp;
    @JsonProperty("rh")
    public int getRh() {
        return this.rh; }
    public void setRh(int rh) {
        this.rh = rh; }
    private int rh;
    @JsonProperty("clouds")
    public int getClouds() {
        return this.clouds; }
    public void setClouds(int clouds) {
        this.clouds = clouds; }
    private int clouds;
    @JsonProperty("weather")
    public Weather getWeather() {
        return this.weather; }
    public void setWeather(Weather weather) {
        this.weather = weather; }

    @DBRef
    private Weather weather;
    @JsonProperty("slp")
    public double getSlp() {
        return this.slp; }
    public void setSlp(double slp) {
        this.slp = slp; }

    @Field("slp")
    private double slp;
    @JsonProperty("pres")
    public int getPres() {
        return this.pres; }
    public void setPres(int pres) {
        this.pres = pres; }

    @Field("pres")
    private int pres;
    @JsonProperty("uv")
    public double getUv() {
        return this.uv; }
    public void setUv(double uv) {
        this.uv = uv; }

    @Field("uv")
    private double uv;
    @JsonProperty("max_dhi")
    public String getMax_dhi() {
        return this.max_dhi; }
    public void setMax_dhi(String max_dhi) {
        this.max_dhi = max_dhi; }

    @Field("max_dhi")
    private String max_dhi;
    @JsonProperty("vis")
    public int getVis() {
        return this.vis; }
    public void setVis(int vis) {
        this.vis = vis; }

    @Field("vis")
    private int vis;
    @JsonProperty("pop")
    public int getPop() {
        return this.pop; }
    public void setPop(int pop) {
        this.pop = pop; }

    @Field("pop")
    private int pop;
    @JsonProperty("moon_phase")
    public double getMoon_phase() {
        return this.moon_phase; }
    public void setMoon_phase(double moon_phase) {
        this.moon_phase = moon_phase; }

    @Field("moon_phase")
    private double moon_phase;
    @JsonProperty("sunrise_ts")
    public int getSunrise_ts() {
        return this.sunrise_ts; }
    public void setSunrise_ts(int sunrise_ts) {
        this.sunrise_ts = sunrise_ts; }

    @Field("sunrise_ts")
    private int sunrise_ts;
    @JsonProperty("sunset_ts")
    public int getSunset_ts() {
        return this.sunset_ts; }
    public void setSunset_ts(int sunset_ts) {
        this.sunset_ts = sunset_ts; }

    @Field("sunset_ts")
    private int sunset_ts;
    @JsonProperty("moonrise_ts")
    public int getMoonrise_ts() {
        return this.moonrise_ts; }
    public void setMoonrise_ts(int moonrise_ts) {
        this.moonrise_ts = moonrise_ts; }

    @Field("moonrise_ts")
    private int moonrise_ts;
    @JsonProperty("moonset_ts")
    public int getMoonset_ts() {
        return this.moonset_ts; }
    public void setMoonset_ts(int moonset_ts) {
        this.moonset_ts = moonset_ts; }

    @Field("moonset_ts")
    private int moonset_ts;
    @JsonProperty("pod")
    public String getPod() {
        return this.pod; }
    public void setPod(String pod) {
        this.pod = pod; }

    @Field("pod")
    private String pod;
    @JsonProperty("wind_spd")
    public double getWind_spd() {
        return this.wind_spd; }
    public void setWind_spd(double wind_spd) {
        this.wind_spd = wind_spd; }

    @Field("wind_spd")
    private double wind_spd;
    @JsonProperty("wind_dir")
    public int getWind_dir() {
        return this.wind_dir; }
    public void setWind_dir(int wind_dir) {
        this.wind_dir = wind_dir; }

    @Field("wind_dir")
    private int wind_dir;
    @JsonProperty("wind_cdir")
    public String getWind_cdir() {
        return this.wind_cdir; }
    public void setWind_cdir(String wind_cdir) {
        this.wind_cdir = wind_cdir; }

    @Field("wind_cdir")
    private String wind_cdir;
    @JsonProperty("wind_cdir_full")
    public String getWind_cdir_full() {
        return this.wind_cdir_full; }
    public void setWind_cdir_full(String wind_cdir_full) {
        this.wind_cdir_full = wind_cdir_full; }

    @Field("wind_cdir_full")
    private String wind_cdir_full;

    public double[] getLocation() {
        return location;
    }

    public void setLocation(String longitude, String latitude) {
        this.location = new double[]{Double.parseDouble(longitude), Double.parseDouble(latitude)};
    }

    @Field("location")
    private double[] location;
}