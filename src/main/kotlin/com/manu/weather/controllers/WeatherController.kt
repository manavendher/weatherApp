package com.manu.weather.controllers

import com.manu.weather.models.DayForecast
import com.manu.weather.services.WeatherService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/forecast")
class WeatherController(private val weatherService: WeatherService) {

    @GetMapping("/current-day")
    fun getCurrentDayForecast(): Mono<DayForecast> {
        return weatherService.getForecastForToday()
    }
}