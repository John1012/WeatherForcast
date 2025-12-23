package com.assignment.john.repository.weather

import com.assignment.john.repository.weather.model.ForecastBody
import com.assignment.john.repository.weather.model.WeatherBody
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query

interface OpenWeatherService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByCity(@Query("q") city: String): Response<WeatherBody>

    @GET("data/2.5/forecast")
    suspend fun getForecastByCity(@Query("q") city: String): Response<ForecastBody>

}