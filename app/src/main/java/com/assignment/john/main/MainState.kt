package com.assignment.john.main

import com.assignment.john.udf.State
import com.assignment.john.usecase.WeatherForecastUI

sealed class MainState : State {
    object Initial: MainState()
    object Loading: MainState(), State.Loading
    data class Complete(override val content: Content): MainState(), State.Complete<Content>
    data class Error(override val throwable: Throwable? = null): MainState(), State.Error

    fun extractContent() : Content? {
        return (this as? Complete)?.content
    }
}

data class Content(
    val cityList : List<String>,
    val weatherForecast: WeatherForecastUI?,
)
