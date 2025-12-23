package com.assignment.john.udf

import kotlinx.coroutines.flow.Flow

interface UiAction<S : State> {
    suspend fun perform(getPreviousState: () -> S): Flow<S>
}

interface State {
    interface Loading: State
    interface Complete<T: Any?>: State {
        val content: T
    }
    interface Error: State {
        val throwable: Throwable?
    }
}

