package com.assignment.john.repository.weather.model


import kotlinx.serialization.Serializable

@Serializable
data class ForecastBody(
    val city: City?,
    val cnt: Int?,
    val cod: String?,
    val list: List<ForecastItem>?,
    val message: Int?
)