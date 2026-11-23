package com.sdu.izdesdu.presentation.weather

import com.sdu.izdesdu.domain.model.CampusWeather
import com.sdu.izdesdu.domain.model.HourlyWeather
import com.sdu.izdesdu.domain.model.WeatherCondition
import com.sdu.izdesdu.domain.model.WeatherErrorType
import com.sdu.izdesdu.domain.model.WeatherException
import com.sdu.izdesdu.domain.repository.WeatherRepository
import com.sdu.izdesdu.domain.usecase.GetCampusWeatherUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException

class WeatherViewModelTest {

    private val sampleWeather = CampusWeather(
        temperatureC = 14,
        feelsLikeC = 12,
        windSpeedKmh = 9,
        humidityPercent = 48,
        condition = WeatherCondition.CLEAR,
        iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
        isDay = true,
        updatedTime = "13:00",
        hourly = listOf(
            HourlyWeather(
                timeLabel = "13:00",
                temperatureC = 14,
                condition = WeatherCondition.CLEAR,
                iconUrl = "https://openweathermap.org/img/wn/01d@2x.png"
            )
        )
    )

    @Test
    fun init_emitsLoadingThenSuccess() {
        val deferred = CompletableDeferred<Result<CampusWeather?>>()
        val repository = object : WeatherRepository {
            override suspend fun getCampusWeather(): Result<CampusWeather?> = deferred.await()
        }

        val viewModel = WeatherViewModel(
            getCampusWeatherUseCase = GetCampusWeatherUseCase(repository),
            dispatcher = Dispatchers.Unconfined
        )

        assertEquals(WeatherUiState.Loading, viewModel.uiState.value)

        deferred.complete(Result.success(sampleWeather))

        val state = viewModel.uiState.value
        assertTrue(state is WeatherUiState.Success)
        assertEquals(sampleWeather, (state as WeatherUiState.Success).weather)
    }

    @Test
    fun loadWeather_whenEmptyHourlyOrNull_emitsEmpty() {
        val nullRepo = object : WeatherRepository {
            override suspend fun getCampusWeather(): Result<CampusWeather?> = Result.success(null)
        }
        val nullVm = WeatherViewModel(
            getCampusWeatherUseCase = GetCampusWeatherUseCase(nullRepo),
            dispatcher = Dispatchers.Unconfined
        )
        assertEquals(WeatherUiState.Empty, nullVm.uiState.value)

        val emptyHourlyRepo = object : WeatherRepository {
            override suspend fun getCampusWeather(): Result<CampusWeather?> =
                Result.success(sampleWeather.copy(hourly = emptyList()))
        }
        val emptyVm = WeatherViewModel(
            getCampusWeatherUseCase = GetCampusWeatherUseCase(emptyHourlyRepo),
            dispatcher = Dispatchers.Unconfined
        )
        assertEquals(WeatherUiState.Empty, emptyVm.uiState.value)
    }

    @Test
    fun loadWeather_whenFailure_emitsErrorAndRetryRecoversToSuccess() {
        var shouldFail = true
        val repository = object : WeatherRepository {
            override suspend fun getCampusWeather(): Result<CampusWeather?> {
                return if (shouldFail) {
                    Result.failure(IOException("No internet connection"))
                } else {
                    Result.success(sampleWeather)
                }
            }
        }

        val viewModel = WeatherViewModel(
            getCampusWeatherUseCase = GetCampusWeatherUseCase(repository),
            dispatcher = Dispatchers.Unconfined
        )

        val errorState = viewModel.uiState.value
        assertTrue(errorState is WeatherUiState.Error)
        assertEquals(WeatherErrorType.NETWORK, (errorState as WeatherUiState.Error).errorType)

        shouldFail = false
        viewModel.retry()

        val successState = viewModel.uiState.value
        assertTrue(successState is WeatherUiState.Success)
        assertEquals(sampleWeather, (successState as WeatherUiState.Success).weather)
    }

    @Test
    fun loadWeather_mapsTimeoutServerAndUnknownErrors() {
        val timeoutVm = WeatherViewModel(
            getCampusWeatherUseCase = GetCampusWeatherUseCase(
                object : WeatherRepository {
                    override suspend fun getCampusWeather(): Result<CampusWeather?> =
                        Result.failure(SocketTimeoutException("timeout"))
                }
            ),
            dispatcher = Dispatchers.Unconfined
        )
        assertEquals(WeatherUiState.Error(WeatherErrorType.TIMEOUT), timeoutVm.uiState.value)

        val serverVm = WeatherViewModel(
            getCampusWeatherUseCase = GetCampusWeatherUseCase(
                object : WeatherRepository {
                    override suspend fun getCampusWeather(): Result<CampusWeather?> =
                        Result.failure(WeatherException(WeatherErrorType.SERVER))
                }
            ),
            dispatcher = Dispatchers.Unconfined
        )
        assertEquals(WeatherUiState.Error(WeatherErrorType.SERVER), serverVm.uiState.value)

        val unknownVm = WeatherViewModel(
            getCampusWeatherUseCase = GetCampusWeatherUseCase(
                object : WeatherRepository {
                    override suspend fun getCampusWeather(): Result<CampusWeather?> =
                        Result.failure(IllegalStateException("unexpected"))
                }
            ),
            dispatcher = Dispatchers.Unconfined
        )
        assertEquals(WeatherUiState.Error(WeatherErrorType.UNKNOWN), unknownVm.uiState.value)
    }
}
