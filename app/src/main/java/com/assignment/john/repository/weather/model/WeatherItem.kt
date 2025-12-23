package com.assignment.john.repository.weather.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherItem(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val main: String?
)