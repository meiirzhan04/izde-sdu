package com.sdu.izdesdu.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdu.izdesdu.domain.model.WeatherErrorType
import com.sdu.izdesdu.domain.model.WeatherException
import com.sdu.izdesdu.domain.usecase.GetCampusWeatherUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.EmptyCoroutineContext

class WeatherViewModel(
    private val getCampusWeatherUseCase: GetCampusWeatherUseCase,
    private val dispatcher: CoroutineDispatcher? = null
) : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadWeather()
    }

    fun loadWeather() {
        loadJob?.cancel()
        _uiState.value = WeatherUiState.Loading
        loadJob = viewModelScope.launch(dispatcher ?: EmptyCoroutineContext) {
            val result = getCampusWeatherUseCase()
            result.fold(
                onSuccess = { weather ->
                    _uiState.value = if (weather == null || weather.hourly.isEmpty()) {
                        WeatherUiState.Empty
                    } else {
                        WeatherUiState.Success(weather)
                    }
                },
                onFailure = { throwable ->
                    val errorType = when (throwable) {
                        is WeatherException -> throwable.errorType
                        is SocketTimeoutException -> WeatherErrorType.TIMEOUT
                        is IOException -> WeatherErrorType.NETWORK
                        else -> WeatherErrorType.UNKNOWN
                    }
                    _uiState.value = WeatherUiState.Error(errorType)
                }
            )
        }
    }

    fun retry() {
        loadWeather()
    }
}
