package com.example.chit_chat.utils

sealed interface Error {
    data class BackendError(
        val code: Int,
        val error: ErrorResponseModel
    ): Error
    data object NetworkError: Error
    data object UnknownError: Error
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class Failure(val error: Error): ResultWrapper<Nothing>()


    fun <R> mapSuccess(mapper: (T) -> R): ResultWrapper<R> {
        return when (this) {
            is Success -> { Success(mapper(this.value)) }
            is Failure -> this
        }
    }

    fun isSuccessful(): Boolean {
        return wrappedValue != null
    }

    fun isFailure(): Boolean {
        return wrappedError != null
    }

     val wrappedError: Error?
        get() {
            return if (this is Failure) this.error else null
        }

    private val wrappedValue: T?
        get() {
            return if (this is Success) this.value else null
        }
}

data class ErrorResponseModel(
    val code: Int,
    val message: String
)