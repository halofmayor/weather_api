package com.meteorologia.ipma_weather_service.domain.controller;

import com.meteorologia.ipma_weather_service.domain.DTOs.Weather;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.meteorologia.ipma_weather_service.domain.service.WeatherService;
import reactor.core.publisher.Mono;



@RestController
@RequestMapping("/weather")
public class WeatherController {


    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/{globalIdLocal}")
    public Mono<ResponseEntity<Weather.WeatherResponse>> getWeather(@PathVariable String globalIdLocal) {
        return weatherService.getWeather(globalIdLocal) //Executes the function to get the necessary data
                .map(ResponseEntity::ok)//weatherResponse -> ResponseEntity.ok(weatherResponse), the .map already know that it will inject a weatherResponse, so it's not needed. This method is called when mono gets a response
                .defaultIfEmpty(ResponseEntity.notFound().build());//if empty this will be called
    }
}
