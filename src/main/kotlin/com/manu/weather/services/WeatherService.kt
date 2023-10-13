package com.manu.weather.services

import com.manu.weather.models.DayForecast
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class WeatherService {

    private val webClient = WebClient.create("https://api.weather.gov")

    fun getForecastForToday(): Mono<DayForecast> {
        return webClient.get()
                .uri("/gridpoints/MLB/33,70/forecast")
                .retrieve()
                .bodyToMono(WeatherResponse::class.java)
                .flatMap { response ->
                    val currentDayPeriod = response.properties.periods.firstOrNull { it.isToday() }

                    if (currentDayPeriod != null) {
                        Mono.just(
                                DayForecast(
                                        dayName = currentDayPeriod.name,
                                        tempHighCelsius = currentDayPeriod.temperature,
                                        forecastBlurb = currentDayPeriod.shortForecast
                                )
                        )
                    } else {
                        Mono.empty()
                    }
                }
    }
}

data class WeatherResponse(
        val properties: WeatherProperties
)

data class WeatherProperties(
        val periods: List<WeatherPeriod>
)

data class WeatherPeriod(
        val name: String,
        val isDaytime: Boolean,
        val temperature: Double,
        val shortForecast: String
) {
    fun isToday(): Boolean {
        return  name in arrayOf("Today","Tonight")
    }
}