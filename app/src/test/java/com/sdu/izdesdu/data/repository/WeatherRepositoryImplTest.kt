package com.sdu.izdesdu.data.repository

import com.sdu.izdesdu.data.remote.api.WeatherApi
import com.sdu.izdesdu.data.remote.dto.CurrentWeatherDto
import com.sdu.izdesdu.data.remote.dto.HourlyWeatherDto
import com.sdu.izdesdu.data.remote.dto.WeatherResponseDto
import com.sdu.izdesdu.domain.model.WeatherErrorType
import com.sdu.izdesdu.domain.model.WeatherException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException

class WeatherRepositoryImplTest {

    @Test
    fun getCampusWeather_mapsValidResponseToDomain() = runBlocking {
        val repository = WeatherRepositoryImpl(
            weatherApi = fakeApi {
                WeatherResponseDto(
                    current = CurrentWeatherDto(
                        time = "2026-11-23T13:00",
                        temperature2m = 15.2,
                        relativeHumidity2m = 45,
                        apparentTemperature = 13.8,
                        weatherCode = 0,
                        windSpeed10m = 6.4,
                        isDay = 1
                    ),
                    hourly = HourlyWeatherDto(
                        time = listOf("2026-11-23T13:00"),
                        temperature2m = listOf(15.2),
                        weatherCode = listOf(0),
                        isDay = listOf(1)
                    )
                )
            },
            ioDispatcher = Dispatchers.Unconfined
        )

        val result = repository.getCampusWeather()

        assertTrue(result.isSuccess)
        val weather = result.getOrNull()
        assertNotNull(weather)
        assertEquals(15, weather!!.temperatureC)
        assertEquals(1, weather.hourly.size)
    }

    @Test
    fun getCampusWeather_mapsExceptionsToTypedWeatherException() = runBlocking {
        val cases = listOf(
            IOException("offline") to WeatherErrorType.NETWORK,
            SocketTimeoutException("timeout") to WeatherErrorType.TIMEOUT,
            SerializationException("malformed") to WeatherErrorType.SERVER,
            IllegalStateException("boom") to WeatherErrorType.UNKNOWN
        )

        for ((exception, expectedType) in cases) {
            val repository = WeatherRepositoryImpl(
                weatherApi = fakeApi { throw exception },
                ioDispatcher = Dispatchers.Unconfined
            )

            val result = repository.getCampusWeather()

            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()
            assertTrue(error is WeatherException)
            assertEquals(expectedType, (error as WeatherException).errorType)
        }
    }

    @Test(expected = CancellationException::class)
    fun getCampusWeather_rethrowsCancellationException() = runBlocking {
        val repository = WeatherRepositoryImpl(
            weatherApi = fakeApi { throw CancellationException("cancelled") },
            ioDispatcher = Dispatchers.Unconfined
        )

        repository.getCampusWeather()
        Unit
    }

    private fun fakeApi(block: () -> WeatherResponseDto): WeatherApi = object : WeatherApi {
        override suspend fun getCampusWeather(
            latitude: Double,
            longitude: Double,
            current: String,
            hourly: String,
            timezone: String,
            forecastDays: Int
        ): WeatherResponseDto = block()
    }
}
