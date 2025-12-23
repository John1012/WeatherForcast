package com.assignment.john.udf

import kotlinx.coroutines.flow.flowOf

fun <S: State, A: UiAction<S>> A.toFlow() = flowOf(this)