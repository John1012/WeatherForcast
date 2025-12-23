package com.assignment.john.usecase

import com.assignment.john.repository.weather.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class WeatherForecastUI(
    val city: String,
    val currentWeather: WeatherUI,
    val forecastList: List<WeatherUI>,
)

data class WeatherUI(
    val timestamp: Long,
    val weatherType: String,
    val icon: String,
)

class FetchWeatherForecastByCityUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
){
    suspend fun call(city: String): WeatherForecastUI = withContext(Dispatchers.IO) {
        val current = async { weatherRepository.getWeatherByCity(city) }.await()
        val forecast = async { weatherRepository.getForecastByCity(city) }.await()
        return@withContext WeatherForecastUI(
            city = city,
            currentWeather = WeatherUI(
                timestamp = requireNotNull(current.dt) { "current timestamp is null" },
                weatherType = requireNotNull(current.weather?.first()?.main) { "current weather type is null" },
                icon = requireNotNull(current.weather.first().icon) { "current icon is null" },
            ),
            forecastList = forecast.list?.mapIndexed{ idx, item ->
                WeatherUI(
                    timestamp = requireNotNull(item.dt) { "The $idx timestamp is null" },
                    weatherType = requireNotNull(item.weather?.first()?.main) { "The $idx weather type is null" },
                    icon = requireNotNull(item.weather.first().icon) { "The $idx icon is null" },
                )
            } ?: throw Exception("forecast list is null")
        )
    }
}