package com.meteorologia.ipma_weather_service.domain.DTOs;

import java.util.Date;
import java.util.List;

public class Weather {
    //This record represents all the data needed to display on front
    public record WeatherResponse (
        List<WeatherDay> data, //This list will always be 5 because IPMA return 5 WeatherDays
        Integer globalIdLocal, //This id is unique for every district in Portugal
        Date dataUpdate //Timestamp of the last update made by IPMA
    ){}

    //This record represents the weather of a day
    public record WeatherDay (
        Double precipitaProb, //Probability of precipitation
        Double tMin, //Minimum temperature
        Double tMax, //Max temperature
        Integer idWeatherType, //id of the weather type
        Integer classWindSpeed, //id of the wind speed
        Date forecastDate //Respective day
    ){}
}
