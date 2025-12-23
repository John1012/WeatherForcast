package com.assignment.john.repository.weather.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastItem(
    val clouds: Clouds?,
    val dt: Long?,
    @SerialName("dt_txt")
    val dtTxt: String?,
    val main: Main?,
    val pop: Double?,
    val sys: Sys?,
    val visibility: Int?,
    val weather: List<WeatherItem>?,
    val wind: Wind?
)