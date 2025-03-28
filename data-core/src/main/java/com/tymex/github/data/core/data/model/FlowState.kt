package com.tymex.github.data.core.data.model

sealed class FlowState {

    data object Loading : FlowState()

    data class Error(val message: String? = null) : FlowState()

    data class Success<T>(val data: T) : FlowState()
}