package com.sdu.izdesdu.domain.model

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
