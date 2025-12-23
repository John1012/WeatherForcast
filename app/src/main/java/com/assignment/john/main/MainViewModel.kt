package com.assignment.john.main

import com.assignment.john.udf.UniDirectionalFlowViewModel
import com.assignment.john.usecase.FetchWeatherForecastByCityUseCase
import com.assignment.john.usecase.GetTwCityListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTwCityListUseCase: GetTwCityListUseCase,
    private val fetchWeatherForecastByCityUseCase: FetchWeatherForecastByCityUseCase,
) : UniDirectionalFlowViewModel<MainAction, MainState>() {

    override val initialState: MainState
        get() = MainState.Initial

    override val errorHandler: CoroutineExceptionHandler
        get() = CoroutineExceptionHandler { _, throwable ->
            // 理論上，Action跟UseCase會需要處理錯誤情況
            // 會跑到這裡是因為Action跟UseCase都沒有處理
            _viewState.value = MainState.Error(throwable)
        }

    init {
        LoadCityListAction(getTwCityListUseCase).dispatch()
    }
    fun getWeatherByCity(city: String) {
        GetWeatherByCityAction(fetchWeatherForecastByCityUseCase, city).dispatch()
    }
}