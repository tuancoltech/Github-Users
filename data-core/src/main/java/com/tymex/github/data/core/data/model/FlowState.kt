package com.tymex.github.data.core.data.model

/**
 * A sealed hierarchy describing the state of the feed of users lists/user details
 */
sealed class FlowState {

    /**
     * User(s) data is loading
     */
    data object Loading : FlowState()

    /**
     * There's an error with loading user(s) data
     */
    data class Error(val message: String? = null) : FlowState()

    /**
     * User data is successfully loaded and ready to use
     */
    data class Success<T>(val data: T) : FlowState()
}