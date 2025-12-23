package com.assignment.john.udf

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onEach

interface UiActionProcessor<A: UiAction<S>, S: State> {
    val getPreviousState: () -> S
    val actionChannel: Channel<A>

    suspend fun Channel<A>.consumeAsStateFlow(collectHandler: (state: S) -> (Unit)) {
        this.consumeAsFlow()
            .toState()
            .distinctUntilChanged()
            .onEach { println("State: $it") }
            .collect { collectHandler(it) }
    }

    suspend fun dispatchAction(action: A) {
        actionChannel.send(action)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<A>.toState(): Flow<S> = this.flatMapMerge { action -> action.perform(getPreviousState) }
 }
