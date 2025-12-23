package com.assignment.john.repository.weather

import com.assignment.john.repository.RetrofitInstance
import com.assignment.john.repository.weather.model.ForecastBody
import com.assignment.john.repository.weather.model.WeatherBody
import javax.inject.Inject

class WeatherRepository @Inject constructor() {
    suspend fun getWeatherByCity(city: String): WeatherBody {
        val response = RetrofitInstance.api.getCurrentWeatherByCity(city)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            val errorCode = response.code()
            throw Exception("Error fetching weather data. Error code: $errorCode")
        }
    }

    suspend fun getForecastByCity(city: String): ForecastBody {
        val response = RetrofitInstance.api.getForecastByCity(city)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            val errorCode = response.code()
            throw Exception("Error fetching forecast data. Error code: $errorCode")
        }
    }
}