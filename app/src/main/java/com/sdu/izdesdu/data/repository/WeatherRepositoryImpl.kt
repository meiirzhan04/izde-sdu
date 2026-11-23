package com.sdu.izdesdu.data.repository

import com.sdu.izdesdu.data.mapper.WeatherMapper
import com.sdu.izdesdu.data.remote.api.WeatherApi
import com.sdu.izdesdu.domain.model.CampusWeather
import com.sdu.izdesdu.domain.model.WeatherErrorType
import com.sdu.izdesdu.domain.model.WeatherException
import com.sdu.izdesdu.domain.repository.WeatherRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherRepository {
    override suspend fun getCampusWeather(): Result<CampusWeather?> = withContext(ioDispatcher) {
        try {
            val response = weatherApi.getCampusWeather()
            val weather = WeatherMapper.toDomain(response)
            Result.success(weather)
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (throwable: Throwable) {
            val weatherException = when (throwable) {
                is WeatherException -> throwable
                is SocketTimeoutException -> WeatherException(WeatherErrorType.TIMEOUT, throwable)
                is IOException -> WeatherException(WeatherErrorType.NETWORK, throwable)
                is HttpException, is SerializationException ->
                    WeatherException(WeatherErrorType.SERVER, throwable)
                else -> WeatherException(WeatherErrorType.UNKNOWN, throwable)
            }
            Result.failure(weatherException)
        }
    }
}
