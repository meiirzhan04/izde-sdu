package com.sdu.izdesdu.data.repository

import com.sdu.izdesdu.data.mapper.WeatherMapper
import com.sdu.izdesdu.data.remote.api.WeatherApi
import com.sdu.izdesdu.domain.model.CampusWeather
import com.sdu.izdesdu.domain.repository.WeatherRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            Result.failure(throwable)
        }
    }
}
