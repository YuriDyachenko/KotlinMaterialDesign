package dyachenko.kotlinmaterialdesign.viewmodel

import dyachenko.kotlinmaterialdesign.model.PODResponseData

sealed class PODState {
    data class Success(val responseData: PODResponseData) : PODState()
    data class Error(val error: Throwable) : PODState()
    object Loading : PODState()
}
