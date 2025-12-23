package com.assignment.john.main

import com.assignment.john.udf.UiAction
import com.assignment.john.usecase.FetchWeatherForecastByCityUseCase
import com.assignment.john.usecase.GetTwCityListUseCase
import kotlinx.coroutines.flow.flow

sealed class MainAction : UiAction<MainState>

class LoadCityListAction(
    val getTwCityListUseCase: GetTwCityListUseCase,
) : MainAction() {
    override suspend fun perform(getPreviousState: () -> MainState) = flow {
        emit(MainState.Loading)
        val cityInTw = getTwCityListUseCase.call()
        val prevContent = getPreviousState()
            .extractContent()
        val newContent = prevContent?.copy(cityList = cityInTw) ?: Content(cityInTw, null)
        emit(MainState.Complete(newContent))
    }
}

class GetWeatherByCityAction (
    private val fetchWeatherForecastByCityUseCase: FetchWeatherForecastByCityUseCase,
    private val city: String,
) : MainAction() {
    override suspend fun perform(getPreviousState: () -> MainState) = flow {
        val weatherForecastUI = fetchWeatherForecastByCityUseCase.call(city)
        val prevContent = getPreviousState()
            .extractContent()
        val newContent = prevContent?.copy(weatherForecast = weatherForecastUI) ?: Content(emptyList(), weatherForecastUI)
        emit(MainState.Complete(newContent))
    }
}
