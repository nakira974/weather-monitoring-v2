package com.example.weathermonitoringv2.services;

import com.example.weathermonitoringv2.models.remote.WeatherForecasts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class HttpClientService implements IHttpClientService {
    @Value("${WEATHER_FORECASTS_API_KEY}")
    private String _apiKey;

    @Value("${WEATHER_FORECASTS_API_BASE_URI}")
    private String _baseUri;
    private final ThreadPoolExecutor _executor;
    private final HttpClient _httpClient;

    public HttpClientService() {
        _executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        _httpClient = new DefaultHttpClient();
    }
    @Override
    public Future<Optional<WeatherForecasts>> getForecastByCityAsync(String city, String country) {
        Future<Optional<WeatherForecasts>> result = null;
        try{
            result = _executor.submit(() -> {
                Optional<WeatherForecasts> taskResult = Optional.empty();
                try{
                    var request = new HttpGet(getDefaultURI(city, country));
                    var response = _httpClient.execute(request);
                    var entity = response.getEntity();
                    if (entity == null)
                        throw new ClientProtocolException("Response contains no content");

                    ObjectMapper objectMapper = new ObjectMapper();
                    var forecasts =  objectMapper.readValue(response.getEntity().getContent(), WeatherForecasts.class);

                    taskResult = Optional.ofNullable(forecasts);

                }catch ( IOException| URISyntaxException ex ){
                    if(ex.getClass().equals(URISyntaxException.class))
                        Logger.getLogger("ROOT").log(Level.SEVERE, "URL format problem!");
                }
                return taskResult;
            });

        } catch (Exception ex){
            Logger.getLogger("ROOT").log(Level.SEVERE, String.format("Can't fetch %s %s weather forecasts!",city,country));
        }
        return result;
    }

    private URI getDefaultURI(String city, String country) throws URISyntaxException {
        return new URIBuilder(_baseUri)
                .addParameter("key", _apiKey)
                .addParameter("units", "I")
                .addParameter("days", "10")
                .addParameter("city", city)
                .addParameter("country", country)
                .build();
    }
}
