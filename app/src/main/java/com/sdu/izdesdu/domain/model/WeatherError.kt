package com.sdu.izdesdu.domain.model

import java.io.IOException
import java.net.SocketTimeoutException

enum class WeatherErrorType {
    NETWORK,
    TIMEOUT,
    SERVER,
    UNKNOWN
}

class WeatherException(
    val errorType: WeatherErrorType,
    cause: Throwable? = null
) : Exception(cause)

fun Throwable.toWeatherErrorType(): WeatherErrorType = when (this) {
    is WeatherException -> errorType
    is SocketTimeoutException -> WeatherErrorType.TIMEOUT
    is IOException -> WeatherErrorType.NETWORK
    else -> WeatherErrorType.UNKNOWN
}
