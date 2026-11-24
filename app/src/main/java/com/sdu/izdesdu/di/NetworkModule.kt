package com.sdu.izdesdu.di

import android.content.pm.ApplicationInfo
import com.sdu.izdesdu.data.remote.api.WeatherApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

private const val NETWORK_TIMEOUT_SECONDS = 15L
private const val JSON_MEDIA_TYPE = "application/json; charset=UTF-8"

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {
        val isDebuggable =
            (androidContext().applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (isDebuggable) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(WeatherApi.BASE_URL)
            .client(get())
            .addConverterFactory(get<Json>().asConverterFactory(JSON_MEDIA_TYPE.toMediaType()))
            .build()
    }

    single<WeatherApi> { get<Retrofit>().create(WeatherApi::class.java) }
}
