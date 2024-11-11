package com.meteorologia.ipma_weather_service.domain.service;

import com.meteorologia.ipma_weather_service.domain.DTOs.CachedWeatherResponse;
import com.meteorologia.ipma_weather_service.domain.DTOs.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherService {

    //Webclient is useful to make requests to extern APIs, is better than creating our own controllers
    @Autowired
    private final WebClient webClient;
    //Will use ConcurrentHashMap because it is thread safe, this means can be acessed by multiple requisitons
    private final ConcurrentHashMap<String, CachedWeatherResponse> cache = new ConcurrentHashMap<>();

    public WeatherService(WebClient webClient) {
        this.webClient = webClient;
    }

    //Method to check if the cache is outdated (in this case I've set a 12 hours validity)
    private boolean isCacheExpired(CachedWeatherResponse cachedResponse) {
        LocalDateTime lastUpdate = cachedResponse.timestamp();
        return lastUpdate.isBefore(LocalDateTime.now().minusHours(12));
    }


    //Mono is used to treat the data of 0 or 1 request (in this case the WebClient will return only 1 value, the JSON containing all the data)
    public Mono<Weather.WeatherResponse> getWeather(String globalLocalId) {

        CachedWeatherResponse cachedResponse = cache.get(globalLocalId);
        //Checks if the cache is empty or expired
        if (cachedResponse != null && !isCacheExpired(cachedResponse)) {
            //Returns the cache value as a mono
            return Mono.just(cachedResponse.weatherResponse());
        }

        return webClient.get()
                .uri("https://api.ipma.pt/open-data/forecast/meteorology/cities/daily/" + globalLocalId + ".json")//API request uri
                .retrieve() //Indicates that we are going to use HTTP methods
                .bodyToMono(Weather.WeatherResponse.class) //Converts the HTTP response to a WeatherRespose Object and returns as a Mono
                .doOnNext(response -> {
                    //Creates a new cache entry
                    CachedWeatherResponse newCache = new CachedWeatherResponse(response, LocalDateTime.now());
                    cache.put(globalLocalId, newCache); //Updates the cache
                });
    }

}