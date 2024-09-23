package com.example.chit_chat.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> {
                    ResultWrapper.Failure(Error.NetworkError)
                }

                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    if (errorResponse != null) {
                        ResultWrapper.Failure(
                            Error.BackendError(
                                code,
                                errorResponse
                            )
                        )
                    } else {
                        ResultWrapper.Failure(Error.UnknownError)
                    }
                }

                else -> {
                    ResultWrapper.Failure(Error.UnknownError)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponseModel? {
    return try {
        throwable.response()?.errorBody()?.source()?.let {
            JSONConverter().unpackObjectFromJSON<ErrorResponseModel>(it.readUtf8())
        }
    } catch (exception: Exception) {
        null
    }
}