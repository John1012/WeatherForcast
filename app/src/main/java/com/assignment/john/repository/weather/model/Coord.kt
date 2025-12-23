package com.assignment.john.repository.weather.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    val lat: Double?,
    val lon: Double?
)