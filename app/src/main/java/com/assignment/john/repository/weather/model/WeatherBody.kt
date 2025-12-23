package com.assignment.john.repository.weather.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherBody(
    val base: String?,
    val clouds: Clouds?,
    val cod: Int?,
    val coord: Coord?,
    val dt: Long?,
    val id: Int?,
    val main: Main?,
    val name: String?,
    val sys: Sys?,
    val timezone: Int?,
    val visibility: Int?,
    val weather: List<WeatherItem>?,
    val wind: Wind?
)