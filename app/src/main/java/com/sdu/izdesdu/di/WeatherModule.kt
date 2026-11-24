package com.sdu.izdesdu.di

import com.sdu.izdesdu.data.repository.WeatherRepositoryImpl
import com.sdu.izdesdu.domain.repository.WeatherRepository
import com.sdu.izdesdu.domain.usecase.GetCampusWeatherUseCase
import com.sdu.izdesdu.presentation.weather.WeatherViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val weatherModule = module {
    single<WeatherRepository> { WeatherRepositoryImpl(weatherApi = get()) }
    single { GetCampusWeatherUseCase(weatherRepository = get()) }
    viewModel { WeatherViewModel(getCampusWeatherUseCase = get()) }
}
