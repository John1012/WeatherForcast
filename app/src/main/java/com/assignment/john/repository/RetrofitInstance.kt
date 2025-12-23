package com.assignment.john.repository

import com.assignment.john.BuildConfig
import com.assignment.john.repository.weather.OpenWeatherService
import com.assignment.john.repository.weather.apiKeyInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.openweathermap.org/"

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor(BuildConfig.OPEN_WEATHER_API_KEY)) // Use the interceptor here
        .build()

    val api: OpenWeatherService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherService::class.java)
    }
}