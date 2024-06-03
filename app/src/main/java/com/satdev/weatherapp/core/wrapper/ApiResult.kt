package com.satdev.weatherapp.core.wrapper

import retrofit2.Response

sealed class ApiResult<T>(
    val data: T? = null,
    val errorWrapper: ErrorWrapper? = null
) {
    class Success<T>(data: T) : ApiResult<T>(data)
    class Error<T>(errorWrapper: ErrorWrapper, data: T? = null) : ApiResult<T>(data, errorWrapper)
}

fun <T, R> Response<T>.toApiResult(mapper: (T?) -> R?) : ApiResult<R?> {
    return if (this.isSuccessful){
        ApiResult.Success(mapper(this.body()))
    } else {
        ApiResult.Error(ErrorWrapper.ServiceInternalError(this.code(),this.message()))
    }
}

sealed class ErrorWrapper(val errorMessage: String?,
                          val statusCode: Int? = null) {

    data class ServiceInternalError(val statusCodeError: Int, val resultErrorMessage: String) : ErrorWrapper(resultErrorMessage,statusCode = statusCodeError)

    data object UnknownError : ErrorWrapper("Unknown Error")

}