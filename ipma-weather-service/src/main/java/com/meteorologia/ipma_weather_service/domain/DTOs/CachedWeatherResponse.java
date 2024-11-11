package com.meteorologia.ipma_weather_service.domain.DTOs;


import java.time.LocalDateTime;

public record CachedWeatherResponse(Weather.WeatherResponse weatherResponse, LocalDateTime timestamp) {}

