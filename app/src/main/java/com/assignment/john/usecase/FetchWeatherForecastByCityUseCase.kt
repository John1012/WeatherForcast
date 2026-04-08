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
    val temp: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
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
                temp = requireNotNull(current.main?.temp) { "current temp is null" },
                feelsLike = requireNotNull(current.main?.feelsLike) { "current feelsLike is null" },
                humidity = requireNotNull(current.main?.humidity) { "current humidity is null" },
                windSpeed = requireNotNull(current.wind?.speed) { "current windSpeed is null" },
            ),
            forecastList = forecast.list?.mapIndexed{ idx, item ->
                WeatherUI(
                    timestamp = requireNotNull(item.dt) { "The $idx timestamp is null" },
                    weatherType = requireNotNull(item.weather?.first()?.main) { "The $idx weather type is null" },
                    icon = requireNotNull(item.weather.first().icon) { "The $idx icon is null" },
                    temp = requireNotNull(item.main?.temp) { "The $idx temp is null" },
                    feelsLike = requireNotNull(item.main?.feelsLike) { "The $idx feelsLike is null" },
                    humidity = requireNotNull(item.main?.humidity) { "The $idx humidity is null" },
                    windSpeed = requireNotNull(item.wind?.speed) { "The $idx windSpeed is null" },
                )
            } ?: throw Exception("forecast list is null")
        )
    }
}