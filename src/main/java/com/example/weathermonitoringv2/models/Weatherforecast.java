package com.example.weathermonitoringv2.models;

import java.sql.Date;

public class Weatherforecast {

    public Date Date;

    public int TemperatureC;

    public int getTemperatureF() {
        return  32 + (int)(this.TemperatureC / 0.5556);
    }

    public int TemperatureF;

    public String Summary;
}
