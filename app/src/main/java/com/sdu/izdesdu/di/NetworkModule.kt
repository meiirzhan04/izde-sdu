package com.sdu.izdesdu.di

import com.sdu.izdesdu.data.remote.api.WeatherApi
import com.sdu.izdesdu.data.repository.WeatherRepositoryImpl
import com.sdu.izdesdu.domain.repository.WeatherRepository
import com.sdu.izdesdu.domain.usecase.GetCampusWeatherUseCase
import com.sdu.izdesdu.presentation.weather.WeatherViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    single {
        val json = get<Json>()
        Retrofit.Builder()
            .baseUrl(WeatherApi.BASE_URL)
            .client(get())
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
    }

    single<WeatherApi> { get<Retrofit>().create(WeatherApi::class.java) }

    single<WeatherRepository> { WeatherRepositoryImpl(get()) }

    single { GetCampusWeatherUseCase(get()) }

    viewModel { WeatherViewModel(getCampusWeatherUseCase = get()) }
}
