package coffee.lkh.weathermonitoringv2.services.base;


import coffee.lkh.weathermonitoringv2.models.remote.weatherbitapi.CityWeatherForecasts;

public interface IWeatherforecastService {

    public boolean InsertForecasts(CityWeatherForecasts forecasts);
    public CityWeatherForecasts SelectForecasts(String city_name);
    public boolean UpdateForecasts(String city_name);

    public boolean DeleteForecasts(String city_name);
}
