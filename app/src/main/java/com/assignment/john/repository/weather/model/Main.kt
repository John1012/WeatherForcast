package com.assignment.john.repository.weather.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Main(
    @SerialName("feels_like")
    val feelsLike: Double?,
    @SerialName("grnd_level")
    val grndLevel: Int?,
    val humidity: Int?,
    val pressure: Int?,
    @SerialName("sea_level")
    val seaLevel: Int?,
    val temp: Double?,
    @SerialName("temp_kf")
    val tempKf: Double?,
    @SerialName("temp_max")
    val tempMax: Double?,
    @SerialName("temp_min")
    val tempMin: Double?
)