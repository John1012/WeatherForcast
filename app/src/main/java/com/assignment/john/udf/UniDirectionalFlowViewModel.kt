package com.assignment.john.udf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


abstract class UniDirectionalFlowViewModel<A: UiAction<S>, S: State>: ViewModel(), UiActionProcessor<A,S> {

    protected abstract val initialState: S
    protected abstract val errorHandler: CoroutineExceptionHandler

    protected val _viewState: MutableStateFlow<S> = initializeViewState()
    val viewState = _viewState.asStateFlow()

    final override val getPreviousState: () -> S
        get() = { _viewState.requireValue() }

    final override val actionChannel: Channel<A> = Channel()

    init {
        startActionProcessing()
    }

    private fun startActionProcessing() {
        viewModelScope.launch(errorHandler) {
            actionChannel.consumeAsStateFlow {
                _viewState.value = it
            }
        }
    }

    protected fun A.dispatch() {
        viewModelScope.launch(errorHandler) { dispatchAction(this@dispatch) }
    }

    private fun initializeViewState(): MutableStateFlow<S> = MutableStateFlow(initialState)

    private fun MutableStateFlow<S>.requireValue() = requireNotNull(value) { "State is null" }
}
