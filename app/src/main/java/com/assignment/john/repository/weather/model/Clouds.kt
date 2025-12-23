package com.assignment.john.repository.weather.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Clouds(
    val all: Int?
)